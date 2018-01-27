package com.gamecity.scrabble.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "BoardChat")
@Table(name = "board_chats")
@NamedQueries({
    @NamedQuery(name = "countByBoardId", query = "Select count(1) from BoardChat b where b.board.id = :boardId"),
    @NamedQuery(name = "loadMessagesByBoardId", query = "Select b from BoardChat b where b.board.id = :boardId order by id asc") })
public class BoardChat extends AbstractEntity implements Serializable
{
    private static final long serialVersionUID = -6046345121561887057L;

    @JoinColumn(name = "board_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = Board.class)
    private Board board;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "message", nullable = false)
    private String message;

    public BoardChat()
    {
        // base constructor
    }

    public BoardChat(Board board, User user, String message)
    {
        this.board = board;
        this.user = user;
        this.message = message;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
