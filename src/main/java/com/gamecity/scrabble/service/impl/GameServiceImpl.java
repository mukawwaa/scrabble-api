package com.gamecity.scrabble.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.BoardUser;
import com.gamecity.scrabble.entity.WordLog;
import com.gamecity.scrabble.model.BoardCell;
import com.gamecity.scrabble.model.BoardTile;
import com.gamecity.scrabble.model.PlayHelper;
import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.model.RackTile;
import com.gamecity.scrabble.model.UsedCell;
import com.gamecity.scrabble.model.Word;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.DictionaryService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.WordLogService;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;
import com.gamecity.scrabble.util.DateUtils;
import com.gamecity.scrabble.util.ValidationUtils;

@Service(value = "gameService")
public class GameServiceImpl implements GameService
{
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardUserService boardUserService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private WordLogService wordLogService;

    private enum Position
    {
        VERTICAL, HORIZONTAL
    }

    @Override
    public void start(Board board)
    {
        board.setStartDate(DateUtils.nowAsUnixTime());
        board.setStatus(BoardStatus.STARTED);
        board.setOrderNo(Constants.BoardSettings.FIRST_ROUND);
        boardService.save(board);
        contentService.updateContent(board.getId(), board.getOrderNo());
    }

    @Override
    @Transactional
    public void play(Rack rack)
    {
        ValidationUtils.validateParameters(rack, rack.getBoardId(), rack.getUserId(), rack.getTiles());

        Board board = boardService.checkBoardStarted(rack.getBoardId());
        if (!board.getCurrentUser().getId().equals(rack.getUserId()))
        {
            throw new GameException(GameError.NOT_YOUR_TURN);
        }

        validateRack(rack);
        calculateScore(board, rack);
        contentService.updateContent(board.getId(), board.getOrderNo());
    }

    // ---------------------------------------------------- private methods ----------------------------------------------------

    private void validateRack(Rack rack)
    {
        Rack currentRack = contentService.getRack(rack.getBoardId(), rack.getUserId());
        Map<Integer, String> tileMap = currentRack.getTiles().stream().collect(Collectors.toMap(RackTile::getTileNumber, RackTile::getLetter));
        Predicate<RackTile> filter =
            rackTile -> tileMap.containsKey(rackTile.getTileNumber()) && tileMap.get(rackTile.getTileNumber()).equals(rackTile.getLetter());
        long rackMatchCount = rack.getTiles().stream().filter(filter).count();
        if (rackMatchCount != currentRack.getTiles().size())
        {
            throw new GameException(GameError.INVALID_RACK);
        }
    }

    private void calculateScore(Board board, Rack rack)
    {
        Integer duration = Long.valueOf(DateUtils.nowAsUnixTime() - board.getLastUpdateDate()).intValue();
        PlayHelper helper = new PlayHelper(board);

        boolean isUsed = rack.getTiles().stream().anyMatch(tile -> tile.isUsed());
        if (isUsed)
        {
            locateRackTiles(helper, rack);
            findWords(helper);
            findUnvalidatedWords(helper);
            linkNewWords(helper);
            addScore(helper);
            logWords(helper, board, duration);
            updateUserScore(helper);
        }
        assignNextUser(board);
        updateBoard(helper, rack);
    }

    private void locateRackTiles(PlayHelper helper, Rack rack)
    {
        rack.getTiles().stream().filter(rackTile -> rackTile.isUsed()).forEach(rackTile -> {
            helper.setCellValue(rackTile.getRowNumber(), rackTile.getColumnNumber(), rackTile.getLetter());
        });
    }

    private void findWords(PlayHelper helper)
    {
        // horizontal words
        IntStream.range(1, helper.getRule().getRowSize() + 1).forEach(rowNumber -> {
            IntStream.range(1, helper.getRule().getColumnSize() + 1).forEach(columnNumber -> {
                findWordsInPosition(helper, rowNumber, columnNumber, Position.HORIZONTAL);
            });
        });

        // vertical words
        IntStream.range(1, helper.getRule().getColumnSize() + 1).forEach(columnNumber -> {
            IntStream.range(1, helper.getRule().getRowSize() + 1).forEach(rowNumber -> {
                findWordsInPosition(helper, rowNumber, columnNumber, Position.VERTICAL);
            });
        });

        validateWordsInDictionary(helper);
    }

    private void findWordsInPosition(PlayHelper helper, Integer rowNumber, Integer columnNumber, Position position)
    {
        BoardCell cell = contentService.getCell(helper.getBoardId(), helper.getRule().getId(), rowNumber, columnNumber);
        String addedLetter = helper.getCellValue(rowNumber, columnNumber);
        Word lastWord = helper.getLastWord();
        if (addedLetter != null)
        {
            if (cell.getLetter() != null)
            {
                throw new GameException(GameError.CELL_IS_NOT_EMPTY, rowNumber, columnNumber);
            }

            lastWord.setNewWordDetected(true);
            lastWord.getText().append(addedLetter);
            lastWord.getUsedCells().add(new UsedCell(rowNumber, columnNumber, addedLetter));
            if (Constants.BoardSettings.FIRST_ROUND.equals(helper.getOrderNo()) && cell.getRule().isStartingCell())
            {
                lastWord.setLinkedWithExistingLetter(true);
            }
        }
        else if (cell.getLetter() != null)
        {
            lastWord.getText().append(cell.getLetter());
            lastWord.getUsedCells().add(new UsedCell(rowNumber, columnNumber, cell.getLetter()));
            lastWord.setLinkedWithExistingLetter(true);
        }
        else if (Constants.BoardSettings.FIRST_ROUND.equals(helper.getOrderNo()) && cell.getRule().isStartingCell())
        {
            throw new GameException(GameError.STARTING_CELL_CANNOT_BE_EMPTY);
        }

        boolean isEmptyCell = (addedLetter == null && cell.getLetter() == null);
        boolean isHorizontalLastLine = (Position.HORIZONTAL.equals(position) && rowNumber == helper.getRule().getRowSize());
        boolean isVerticalLastLine = (Position.VERTICAL.equals(position) && columnNumber == helper.getRule().getColumnSize());

        if (isEmptyCell || isHorizontalLastLine || isVerticalLastLine)
        {
            if (lastWord.isNewWordDetected())
            {
                helper.addWord(new Word(lastWord.getUsedCells(), lastWord.getText(), lastWord.isLinkedWithExistingLetter()));
            }
            lastWord.reset();
        }
    }

    private void validateWordsInDictionary(PlayHelper helper)
    {
        helper.getWords().forEach(word -> {
            if (dictionaryService.isWordValid(word.getText().toString(), helper.getRule().getLanguage()))
            {
                helper.addValidatedWord(word);
            }
            else
            {
                helper.addUnvalidatedWord(word);
            }
        });
    }

    private void findUnvalidatedWords(PlayHelper helper)
    {
        if (!helper.getUnvalidatedWords().isEmpty())
        {
            List<Word> unvalidatedWords = helper.getUnvalidatedWords().stream().filter(unvalidatedWord -> {
                if (unvalidatedWord.getText().length() > 1)
                {
                    return true;
                }

                Predicate<Word> predicate =
                    validatedWord -> validatedWord.getUsedCells().contains(unvalidatedWord.getUsedCells().stream().findFirst().get());
                long matchCount = helper.getValidatedWords().stream().filter(predicate).count();
                return matchCount == 0;
            }).collect(Collectors.toList());

            if (!unvalidatedWords.isEmpty())
            {
                throw new GameException(GameError.WORD_IS_NOT_DEFINED, unvalidatedWords.toString(), helper.getRule().getLanguage());
            }
        }
    }

    private void linkNewWords(PlayHelper helper)
    {
        List<Word> linkedWords = helper.getValidatedWords().stream().filter(word -> word.isLinkedWithExistingLetter()).collect(Collectors.toList());
        linkedWords.forEach(word -> followLinks(word, helper.getValidatedWords()));

        List<Word> unlinkedWords =
            helper.getValidatedWords().stream().filter(word -> !word.isLinkedWithExistingLetter()).collect(Collectors.toList());
        if (!unlinkedWords.isEmpty())
        {
            throw new GameException(GameError.WORD_IS_NOT_LINKED, unlinkedWords.toString());
        }
    }

    private void followLinks(Word existingWord, List<Word> words)
    {
        words.stream().filter(word -> !word.isLinkedWithExistingLetter()).forEach(word -> findLink(word, existingWord, words));
    }

    private void findLink(Word word, Word existingWord, List<Word> words)
    {
        long linkCount = existingWord.getUsedCells().stream().filter(usedCell -> word.getUsedCells().contains(usedCell)).count();
        if (linkCount > 0L)
        {
            word.setLinkedWithExistingLetter(true);
            followLinks(word, words);
        }
    }

    private void addScore(PlayHelper helper)
    {
        helper.getValidatedWords().forEach(word -> {
            word.getUsedCells().forEach(usedCell -> {
                calculateMove(helper, word, usedCell);
            });
            helper.addScore(word.getScore() * word.getWordMultiplier());
        });
    }

    private void calculateMove(PlayHelper helper, Word word, UsedCell usedCell)
    {
        BoardCell cell = contentService.getCell(helper.getBoardId(), helper.getRule().getId(), usedCell.getRowNumber(), usedCell.getColumnNumber());
        BoardTile tile = contentService.getTile(helper.getBoardId(), helper.getRule().getId(), usedCell.getLetter());

        if (cell.getLetter() == null)
        {
            cell.setScore(tile.getRule().getScore());
            cell.setLetter(usedCell.getLetter());
            cell.setUsed(true);
            helper.updateCell(cell);
        }

        word.addScore(cell.getRule().getLetterMultiplier() * tile.getRule().getScore());
        word.addMultiplier(cell.getRule().getWordMultiplier());
    }

    private void logWords(PlayHelper helper, Board board, Integer duration)
    {
        helper.getWords().forEach(word -> {
            WordLog log = new WordLog(board, board.getCurrentUser(), helper.getOrderNo(), word.getText().toString(), word.getScore(), duration);
            wordLogService.save(log);
        });
    }

    private void updateBoard(PlayHelper helper, Rack rack)
    {
        helper.getUpdatedCells().stream().filter(cell -> cell != null && cell.isUsed()).forEach(cell -> contentService.updateCell(cell));
        contentService.updateRack(rack);
    }

    private void updateUserScore(PlayHelper helper)
    {
        BoardUser boardUser = boardUserService.loadByUserId(helper.getBoardId(), helper.getUserId());
        Integer finalScore = boardUser.getScore() + helper.getScore();
        boardUser.setScore(finalScore);
        boardUserService.save(boardUser);
    }

    private void assignNextUser(Board board)
    {
        BoardUser nextUser = boardUserService.getNextUser(board.getId());
        board.setCurrentUser(nextUser.getUser());
        board.setOrderNo(board.getOrderNo() + 1);
        boardService.save(board);
    }
}
