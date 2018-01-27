package com.gamecity.scrabble.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_date", nullable = false, columnDefinition = "bigint default 0")
    private Long createDate;

    @Column(name = "last_update_date")
    private Long lastUpdateDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }

    public Long getLastUpdateDate()
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate)
    {
        this.lastUpdateDate = lastUpdateDate;
    }
}
