package com.gamecity.scrabble.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BoardPlayer implements Serializable
{
    private static final long serialVersionUID = 3059128891594812290L;
    private Long boardId;
    private Integer orderNo;
    private Long currentUserId;
    private String currentUsername;
    private List<Player> players;

    public BoardPlayer()
    {
        super();
    }

    public Long getBoardId()
    {
        return boardId;
    }

    public void setBoardId(Long boardId)
    {
        this.boardId = boardId;
    }

    public Integer getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo)
    {
        this.orderNo = orderNo;
    }

    public Long getCurrentUserId()
    {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId)
    {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUsername()
    {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername)
    {
        this.currentUsername = currentUsername;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Player> players)
    {
        this.players = players;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
