package com.gamecity.scrabble.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "BoardUser")
@Table(name = "board_users")
@NamedQueries({
    @NamedQuery(name = "loadByUserId", query = "Select b from BoardUser b where b.board.id = :boardId and b.user.id = :userId"),
    @NamedQuery(name = "loadUsersByBoardId", query = "Select b from BoardUser b where b.board.id = :boardId and leaveDate is null"),
    @NamedQuery(name = "loadNextUser", query = "Select b from BoardUser b where b.board.id = :boardId and b.orderNo = :orderNo and leaveDate is null")
})
public class BoardUser extends AbstractEntity
{
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = Board.class)
    private Board board;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "order_no")
    private Integer orderNo;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "join_date", nullable = false, columnDefinition = "bigint default 0")
    private Long joinDate;

    @Column(name = "leave_date")
    private Long leaveDate;

    @Transient
    private boolean enabled;

    public BoardUser()
    {
        // base constructor
    }

    public BoardUser(Board board, User user)
    {
        this.board = board;
        this.user = user;
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

    public Long getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(Long joinDate)
    {
        this.joinDate = joinDate;
    }

    public Long getLeaveDate()
    {
        return leaveDate;
    }

    public void setLeaveDate(Long leaveDate)
    {
        this.leaveDate = leaveDate;
    }

    public boolean isEnabled()
    {
        return leaveDate == null;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
