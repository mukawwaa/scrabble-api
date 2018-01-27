package com.gamecity.scrabble.model;

import java.util.ArrayList;
import java.util.List;

public class Word
{
    private static final Integer BASE_SCORE = 0;
    private static final Integer BASE_MULTIPLIER = 1;
    private List<UsedCell> usedCells;
    private StringBuilder text;
    private boolean newWordDetected;
    private boolean linkedWithExistingLetter;
    private Integer score;
    private Integer wordMultiplier;

    public Word()
    {
        reset();
    }

    public Word(List<UsedCell> usedCells, StringBuilder text, boolean linkedWithExistingLetter)
    {
        this.usedCells = usedCells;
        this.text = text;
        this.linkedWithExistingLetter = linkedWithExistingLetter;
        this.score = BASE_SCORE;
        this.wordMultiplier = BASE_MULTIPLIER;
    }

    public void reset()
    {
        this.usedCells = new ArrayList<>();
        this.text = new StringBuilder();
        this.newWordDetected = false;
        this.linkedWithExistingLetter = false;
    }

    public List<UsedCell> getUsedCells()
    {
        return usedCells;
    }

    public StringBuilder getText()
    {
        return text;
    }

    public boolean isNewWordDetected()
    {
        return newWordDetected;
    }

    public void setNewWordDetected(boolean newWordDetected)
    {
        this.newWordDetected = newWordDetected;
    }

    public boolean isLinkedWithExistingLetter()
    {
        return linkedWithExistingLetter;
    }

    public void setLinkedWithExistingLetter(boolean linkedWithExistingLetter)
    {
        this.linkedWithExistingLetter = linkedWithExistingLetter;
    }

    public void addScore(Integer score)
    {
        this.score = this.score + score;
    }

    public void addMultiplier(Integer wordMultiplier)
    {
        this.wordMultiplier = this.wordMultiplier * wordMultiplier;
    }

    public Integer getScore()
    {
        return score;
    }

    public Integer getWordMultiplier()
    {
        return wordMultiplier;
    }

    @Override
    public String toString()
    {
        return this.text.toString();
    }
}
