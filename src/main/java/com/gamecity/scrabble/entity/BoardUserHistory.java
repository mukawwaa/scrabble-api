package com.gamecity.scrabble.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity(name = "BoardUserHistory")
@Table(name = "board_user_history")
@NamedQueries({
    @NamedQuery(name = "loadLastActionByUserId", query = "Select b from BoardUserHistory b where b.boardId = :boardId and b.userId = :userId order by id desc"),
    @NamedQuery(name = "loadWaitingUsersByBoardId", query = "Select b from BoardUserHistory b where b.boardId = :boardId"),
    @NamedQuery(name = "getWaitingUserCount", query = "Select count(b) from BoardUserHistory b where b.boardId = :boardId")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class BoardUserHistory extends AbstractEntity implements Serializable
{
    private static final long serialVersionUID = -3988293055132046848L;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action")
    private PlayerAction action;

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

    public PlayerAction getAction()
    {
        return action;
    }

    public void setAction(PlayerAction action)
    {
        this.action = action;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
