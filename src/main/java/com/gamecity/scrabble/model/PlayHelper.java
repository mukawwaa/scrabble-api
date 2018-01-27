package com.gamecity.scrabble.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.Rule;

public class PlayHelper
{
    private static final Integer BASE_SCORE = 0;
    private Long boardId;
    private Rule rule;
    private Long userId;
    private Integer orderNo;
    private Integer score;
    private Word lastWord;
    private String[][] cells;
    private List<BoardCell> updatedCells;
    private List<Word> words;
    private List<Word> validatedWords;
    private List<Word> unvalidatedWords;
    private boolean startingCellUsed;

    public PlayHelper(Board board)
    {
        this.boardId = board.getId();
        this.rule = board.getRule();
        this.userId = board.getCurrentUser().getId();
        this.orderNo = board.getOrderNo();
        this.cells = new String[board.getRule().getRowSize()][board.getRule().getColumnSize()];
        this.score = BASE_SCORE;
        this.updatedCells = new ArrayList<BoardCell>();
        this.words = new ArrayList<Word>();
        this.lastWord = new Word();
        this.validatedWords = new ArrayList<Word>();
        this.unvalidatedWords = new ArrayList<Word>();
    }

    public void addScore(Integer score)
    {
        this.score = this.score + score;
    }

    public void updateCell(BoardCell cell)
    {
        this.updatedCells.add(cell);
    }

    public void setCellValue(Integer rowNumber, Integer columnNunber, String letter)
    {
        this.cells[rowNumber - 1][columnNunber - 1] = letter;
    }

    public void addWord(Word word)
    {
        this.words.add(word);
    }

    public void addValidatedWord(Word word)
    {
        this.validatedWords.add(word);
    }

    public void addUnvalidatedWord(Word word)
    {
        this.unvalidatedWords.add(word);
    }

    public Long getBoardId()
    {
        return boardId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public Rule getRule()
    {
        return rule;
    }

    public Integer getOrderNo()
    {
        return orderNo;
    }

    public String[][] getCells()
    {
        return cells;
    }

    public Integer getScore()
    {
        return score;
    }

    public List<BoardCell> getUpdatedCells()
    {
        return new ArrayList<BoardCell>(updatedCells);
    }

    public String getCellValue(Integer rowNumber, Integer columnNumber)
    {
        return cells[rowNumber - 1][columnNumber - 1];
    }

    public List<Word> getWords()
    {
        return new ArrayList<Word>(words);
    }

    public Word getLastWord()
    {
        return lastWord;
    }

    public List<Word> getValidatedWords()
    {
        return validatedWords;
    }

    public List<Word> getUnvalidatedWords()
    {
        return unvalidatedWords;
    }

    public boolean isStartingCellUsed()
    {
        return startingCellUsed;
    }

    public void setStartingCellUsed(boolean startingCellUsed)
    {
        this.startingCellUsed = startingCellUsed;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
