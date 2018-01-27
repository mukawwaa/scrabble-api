package com.gamecity.scrabble.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "Rule")
@Table(name = "rules")
public class Rule extends AbstractEntity implements Serializable
{
    private static final long serialVersionUID = 4057213934436135913L;

    @Column(name = "row_size", nullable = false)
    private Integer rowSize;

    @Column(name = "column_size", nullable = false)
    private Integer columnSize;

    @Column(name = "cell_size", nullable = false)
    private Integer cellSize;

    @Column(name = "rack_size", nullable = false)
    private Integer rackSize;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "enabled", nullable = false, columnDefinition = "tinyint default 1")
    private boolean enabled;

    public Integer getRowSize()
    {
        return rowSize;
    }

    public void setRowSize(Integer rowSize)
    {
        this.rowSize = rowSize;
    }

    public Integer getColumnSize()
    {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize)
    {
        this.columnSize = columnSize;
    }

    public Integer getCellSize()
    {
        return cellSize;
    }

    public void setCellSize(Integer cellSize)
    {
        this.cellSize = cellSize;
    }

    public Integer getRackSize()
    {
        return rackSize;
    }

    public void setRackSize(Integer rackSize)
    {
        this.rackSize = rackSize;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
