package com.gamecity.scrabble.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "Board")
@Table(name = "boards")
@NamedQueries({
    @NamedQuery(name = "loadByUserAndStatus", query = "Select b from Board b where b.owner.id = :userId and b.status in :statusList"),
    @NamedQuery(name = "loadAllByStatus", query = "Select b from Board b where b.status in :statusList"),
    @NamedQuery(name = "stopExpired", query = "Update Board b set b.status = :status")
})
public class Board extends AbstractEntity implements Serializable
{
    private static final long serialVersionUID = 7586449964647443785L;

    @JoinColumn(name = "owner_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_BOARD_OWNER"))
    @ManyToOne(targetEntity = User.class)
    private User owner;

    @JoinColumn(name = "rule_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_BOARD_RULE"))
    @ManyToOne(targetEntity = Rule.class)
    private Rule rule;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_count", nullable = false)
    private Integer userCount;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "status", nullable = false)
    private BoardStatus status;

    @JoinColumn(name = "current_user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_BOARD_CURRENT_USER"))
    @ManyToOne(targetEntity = User.class)
    private User currentUser;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public Rule getRule()
    {
        return rule;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getUserCount()
    {
        return userCount;
    }

    public void setUserCount(Integer userCount)
    {
        this.userCount = userCount;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public BoardStatus getStatus()
    {
        return status;
    }

    public void setStatus(BoardStatus status)
    {
        this.status = status;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

    public Integer getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo)
    {
        this.orderNo = orderNo;
    }

    public Long getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Long startDate)
    {
        this.startDate = startDate;
    }

    public Long getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Long endDate)
    {
        this.endDate = endDate;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
