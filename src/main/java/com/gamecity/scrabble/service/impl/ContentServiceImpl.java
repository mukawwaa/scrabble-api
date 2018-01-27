package com.gamecity.scrabble.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.RedisRepository;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardUser;
import com.gamecity.scrabble.entity.CellRule;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.TileRule;
import com.gamecity.scrabble.model.BoardCell;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.BoardTile;
import com.gamecity.scrabble.model.Player;
import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.model.RackTile;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.RuleService;

@Service(value = "contentService")
public class ContentServiceImpl implements ContentService
{
    @Autowired
    private RuleService ruleService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardUserService boardUserService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private RedisRepository redisRepository;

    @Override
    @Cacheable(value = Constants.Cache.BOARD_CELL, key = "{#boardId, #rowNumber, #columnNumber}")
    public BoardCell getCell(Long boardId, Long ruleId, Integer rowNumber, Integer columnNumber)
    {
        Rule rule = ruleService.get(ruleId);
        List<CellRule> cellRules = ruleService.loadCellRules(rule.getId());
        Predicate<CellRule> filter = cr -> cr.getRowNumber().equals(rowNumber) && cr.getColumnNumber().equals(columnNumber);
        CellRule cellRule = cellRules.stream().filter(filter).findFirst().get();
        return new BoardCell(boardId, cellRule);
    }

    @Override
    @CachePut(value = Constants.Cache.BOARD_CELL, key = "{#cell.boardId, #cell.rule.rowNumber, #cell.rule.columnNumber}")
    public BoardCell updateCell(BoardCell cell)
    {
        return cell;
    }

    @Override
    @Cacheable(value = Constants.Cache.BOARD_TILE, key = "{#boardId, #letter}")
    public BoardTile getTile(Long boardId, Long ruleId, String letter)
    {
        List<BoardTile> tiles = getTiles(boardId, ruleId);
        BoardTile boardTile = tiles.stream().filter(tile -> tile.getRule().getLetter().equals(letter)).findFirst().get();
        return boardTile;
    }

    @Override
    @CachePut(value = Constants.Cache.BOARD_TILE, key = "{#tile.boardId, #tile.rule.letter}")
    public BoardTile updateTile(BoardTile tile)
    {
        return tile;
    }

    @Override
    public BoardContent getContent(Long boardId, Integer orderNo)
    {
        Board board = boardService.get(boardId);
        if (orderNo > board.getOrderNo())
        {
            return null;
        }

        BoardContent content = redisRepository.getBoardContent(boardId, orderNo);
        if (content == null)
        {
            return null;
        }

        return content;
    }

    @Override
    public void updateContent(Long boardId, Integer orderNo)
    {
        Board board = boardService.get(boardId);
        BoardContent content = new BoardContent();
        content.setBoardId(boardId);
        content.setOrderNo(orderNo);
        content.setCurrentUser(board.getCurrentUser().getUsername());
        content.setRuleId(board.getRule().getId());
        content.setStatus(board.getStatus());

        List<BoardCell> cells = getCells(boardId, board.getRule().getId());
        content.setCells(cells);

        List<Player> players = getPlayers(board);
        content.setPlayers(players);
        redisRepository.sendBoardContent(content);
    }

    @Override
    @Cacheable(value = Constants.Cache.RACK, key = "{#boardId, #userId}")
    public Rack getRack(Long boardId, Long userId)
    {
        Rack rack = new Rack();
        rack.setBoardId(boardId);
        rack.setUserId(userId);

        Board board = boardService.get(boardId);
        Rule rule = board.getRule();

        List<BoardTile> boardTiles = getTiles(boardId, rule.getId());
        IntFunction<RackTile> map = rackNumber -> createRackTile(rule, rackNumber, boardTiles);
        List<RackTile> rackTiles = IntStream.range(1, rule.getRackSize() + 1).mapToObj(map).collect(Collectors.toList());

        boardTiles.forEach(boardTile -> contentService.updateTile(boardTile));
        rack.setTiles(rackTiles);
        return rack;
    }

    @Override
    @CachePut(value = Constants.Cache.RACK, key = "{#rack.boardId, #rack.userId}")
    public Rack updateRack(Rack rack)
    {
        Board board = boardService.get(rack.getBoardId());
        Rule rule = board.getRule();

        List<BoardTile> boardTiles = getTiles(board.getId(), rule.getId());
        rack.getTiles().stream().filter(rackTile -> rackTile.isUsed()).forEach(rackTile -> useRackTile(rackTile, rule, boardTiles));
        boardTiles.forEach(boardTile -> contentService.updateTile(boardTile));

        return rack;
    }

    // ---------------------------------------------------- private methods ----------------------------------------------------

    private List<BoardTile> getTiles(Long boardId, Long ruleId)
    {
        List<TileRule> tileRules = ruleService.loadTileRules(ruleId);
        List<BoardTile> tiles = tileRules.stream().map(tileRule -> new BoardTile(boardId, tileRule)).collect(Collectors.toList());
        return tiles;
    }

    private RackTile createRackTile(Rule rule, Integer rackNumber, List<BoardTile> boardTiles)
    {
        int random = 0;
        while (true)
        {
            random = new Random().nextInt(boardTiles.size() - 1);
            BoardTile boardTile = boardTiles.get(random);
            if (boardTile.getRule().getPiece() > 0)
            {
                boardTile.getRule().setPiece(boardTile.getRule().getPiece() - 1);
                return new RackTile(rackNumber, boardTile.getRule().getLetter(), boardTile.getRule().getScore());
            }
        }
    }

    private void useRackTile(RackTile rackTile, Rule rule, List<BoardTile> boardTiles)
    {
        RackTile newRackTile = createRackTile(rule, rackTile.getTileNumber(), boardTiles);
        BeanUtils.copyProperties(newRackTile, rackTile);
    }

    private List<BoardCell> getCells(Long boardId, Long ruleId)
    {
        Rule rule = ruleService.get(ruleId);
        BoardCell[] cells = new BoardCell[rule.getCellSize()];
        IntStream.range(1, rule.getRowSize() + 1).forEach(rowNumber -> {
            IntStream.range(1, rule.getColumnSize() + 1).forEach(columnNumber -> {
                BoardCell cell = contentService.getCell(boardId, ruleId, rowNumber, columnNumber);
                cells[cell.getRule().getCellNumber() - 1] = cell;
            });
        });

        return Arrays.asList(cells);
    }

    private List<Player> getPlayers(Board board)
    {
        List<BoardUser> activeUsers = boardUserService.loadAllActiveUsers(board.getId());
        Function<BoardUser, Player> mapper = player -> new Player(player.getUser(), player.getScore(), board.getCurrentUser().getId().equals(player.getId()));
        return activeUsers.stream().map(mapper).collect(Collectors.toList());
    }
}
