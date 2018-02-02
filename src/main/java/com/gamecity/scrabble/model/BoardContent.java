package com.gamecity.scrabble.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gamecity.scrabble.entity.BoardStatus;

public class BoardContent implements Serializable
{
	private static final long serialVersionUID = 8509578828710644245L;
	private Long boardId;
	private Long ruleId;
	private List<BoardCell> cells;
	private BoardStatus status;
	private Integer orderNo;
	private String currentUser;

	public Long getBoardId()
	{
		return boardId;
	}

	public void setBoardId(Long boardId)
	{
		this.boardId = boardId;
	}

	public Long getRuleId()
	{
		return ruleId;
	}

	public void setRuleId(Long ruleId)
	{
		this.ruleId = ruleId;
	}

	public List<BoardCell> getCells()
	{
		return cells;
	}

	public void setCells(List<BoardCell> cells)
	{
		this.cells = cells;
	}

	public BoardStatus getStatus()
	{
		return status;
	}

	public void setStatus(BoardStatus status)
	{
		this.status = status;
	}

	public Integer getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(Integer orderNo)
	{
		this.orderNo = orderNo;
	}

	public String getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(String currentUser)
	{
		this.currentUser = currentUser;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
