package com.gamecity.scrabble.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UsedCell
{
    private Integer rowNumber;
    private Integer columnNumber;
    private String letter;

    public UsedCell(Integer rowNumber, Integer columnNumber, String letter)
    {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.letter = letter;
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

    public String getLetter()
    {
        return letter;
    }

    public void setLetter(String letter)
    {
        this.letter = letter;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == null || !(object instanceof UsedCell))
        {
            return false;
        }

        UsedCell cell = (UsedCell) object;
        return cell.getColumnNumber().equals(this.columnNumber) && cell.getRowNumber().equals(this.rowNumber);
    }

    @Override
    public int hashCode()
    {
        return Integer.valueOf(this.columnNumber + "0880" + this.rowNumber);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
