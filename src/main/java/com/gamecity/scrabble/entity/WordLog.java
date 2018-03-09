package com.gamecity.scrabble.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "WordLog")
@Table(name = "word_logs")
public class WordLog extends AbstractEntity
{
    @JoinColumn(name = "board_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_WORD_LOG_BOARD"))
    @ManyToOne(targetEntity = Board.class)
    private Board board;

    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_WORD_LOG_USER"))
    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "orderNo", nullable = false)
    private Integer orderNo;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "duration", nullable = false)
    private Integer duration;

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

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public Integer getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo)
    {
        this.orderNo = orderNo;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
