package com.gamecity.scrabble.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player implements Serializable
{
    private static final long serialVersionUID = 4440513193729254918L;
    private Long boardId;
    private Long userId;
    private String username;
    private Integer score;
    private boolean enabled;
    private boolean ownTurn;

    public Long getBoardId()
    {
        return boardId;
    }

    public void setBoardId(Long boardId)
    {
        this.boardId = boardId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isOwnTurn()
    {
        return ownTurn;
    }

    public void setOwnTurn(boolean ownTurn)
    {
        this.ownTurn = ownTurn;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
