package com.gamecity.scrabble.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gamecity.scrabble.entity.BoardChat;

public class ChatMessage implements Serializable
{
    private static final long serialVersionUID = 1007364598716286710L;
    private Long boardId;
    private Long userId;
    private String username;
    private String message;
    private Long createDate;

    public ChatMessage(BoardChat chat)
    {
        this.boardId = chat.getBoard().getId();
        this.userId = chat.getUser().getId();
        this.username = chat.getUser().getUsername();
        this.message = chat.getMessage();
        this.createDate = chat.getCreateDate();
    }

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

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Long getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
