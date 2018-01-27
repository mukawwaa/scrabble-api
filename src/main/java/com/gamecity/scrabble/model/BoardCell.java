package com.gamecity.scrabble.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gamecity.scrabble.entity.CellRule;

public class BoardCell implements Serializable
{
    private static final long serialVersionUID = 8890137690018655986L;
    private Long boardId;
    private CellRule rule;
    private String letter;
    private Integer score;
    private boolean used;

    public BoardCell(Long boardId, CellRule rule)
    {
        this.boardId = boardId;
        this.rule = rule;
    }

    public Long getBoardId()
    {
        return boardId;
    }

    public void setBoardId(Long boardId)
    {
        this.boardId = boardId;
    }

    public CellRule getRule()
    {
        return rule;
    }

    public void setRule(CellRule rule)
    {
        this.rule = rule;
    }

    public String getLetter()
    {
        return letter;
    }

    public void setLetter(String letter)
    {
        this.letter = letter;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public boolean isUsed()
    {
        return used;
    }

    public void setUsed(boolean used)
    {
        this.used = used;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
