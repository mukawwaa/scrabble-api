package com.gamecity.scrabble.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity(name = "CellRule")
@Table(name = "cell_rules")
@NamedQuery(name = "loadAllCellRules", query = "Select cr from CellRule cr where cr.rule.id = :ruleId")
public class CellRule extends AbstractEntity implements Serializable
{
    private static final long serialVersionUID = -3184684992531889421L;

    @Column(name = "cell_number")
    private Integer cellNumber;

    @Column(name = "row_number")
    private Integer rowNumber;

    @Column(name = "column_number")
    private Integer columnNumber;

    @Column(name = "color")
    private String color;

    @Column(name = "letter_multiplier")
    private Integer letterMultiplier;

    @Column(name = "word_multiplier")
    private Integer wordMultiplier;

    @Column(name = "has_right", nullable = false, columnDefinition = "tinyint default 1")
    private boolean hasRight;

    @Column(name = "has_left", nullable = false, columnDefinition = "tinyint default 1")
    private boolean hasLeft;

    @Column(name = "has_top", nullable = false, columnDefinition = "tinyint default 1")
    private boolean hasTop;

    @Column(name = "has_bottom", nullable = false, columnDefinition = "tinyint default 1")
    private boolean hasBottom;

    @Column(name = "starting_cell", nullable = false, columnDefinition = "tinyint default 0")
    private boolean startingCell;

    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = Rule.class)
    private Rule rule;

    public Integer getCellNumber()
    {
        return cellNumber;
    }

    public void setCellNumber(Integer cellNumber)
    {
        this.cellNumber = cellNumber;
    }

    public Integer getRowNumber()
    {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber)
    {
        this.rowNumber = rowNumber;
    }

    public Integer getColumnNumber()
    {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber)
    {
        this.columnNumber = columnNumber;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public Integer getLetterMultiplier()
    {
        return letterMultiplier;
    }

    public void setLetterMultiplier(Integer letterMultiplier)
    {
        this.letterMultiplier = letterMultiplier;
    }

    public Integer getWordMultiplier()
    {
        return wordMultiplier;
    }

    public void setWordMultiplier(Integer wordMultiplier)
    {
        this.wordMultiplier = wordMultiplier;
    }

    public boolean isHasRight()
    {
        return hasRight;
    }

    public void setHasRight(boolean hasRight)
    {
        this.hasRight = hasRight;
    }

    public boolean isHasLeft()
    {
        return hasLeft;
    }

    public void setHasLeft(boolean hasLeft)
    {
        this.hasLeft = hasLeft;
    }

    public boolean isHasTop()
    {
        return hasTop;
    }

    public void setHasTop(boolean hasTop)
    {
        this.hasTop = hasTop;
    }

    public boolean isHasBottom()
    {
        return hasBottom;
    }

    public void setHasBottom(boolean hasBottom)
    {
        this.hasBottom = hasBottom;
    }

    public boolean isStartingCell()
    {
        return startingCell;
    }

    public void setStartingCell(boolean startingCell)
    {
        this.startingCell = startingCell;
    }

    public Rule getRule()
    {
        return rule;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
