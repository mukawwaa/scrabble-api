package com.gamecity.scrabble.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "BoardUser")
@Table(name = "board_users")
@NamedQueries({
    @NamedQuery(name = "loadUserByUserId", query = "Select b from BoardUser b where b.boardId = :boardId and b.userId = :userId"),
    @NamedQuery(name = "loadActiveUsersByBoardId", query = "Select b from BoardUser b where b.boardId = :boardId"),
    @NamedQuery(name = "loadNextUser", query = "Select b from BoardUser b where b.boardId = :boardId and b.orderNo = :orderNo")
})
public class BoardUser extends AbstractEntity
{
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_no")
    private Integer orderNo;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "join_date", nullable = false, columnDefinition = "bigint default 0")
    private Long joinDate;

    @Transient
    private boolean enabled;

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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
