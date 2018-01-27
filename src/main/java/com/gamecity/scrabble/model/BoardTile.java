package com.gamecity.scrabble.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gamecity.scrabble.entity.TileRule;

public class BoardTile implements Serializable
{
	private static final long serialVersionUID = -7096800875582382732L;
	private Long boardId;
	private TileRule rule;

	public BoardTile(Long boardId, TileRule rule)
	{
		this.boardId = boardId;
		this.rule = rule;
	}

	public Long getBoardId()
	{
		return boardId;
	}

	public void setBoardId(Long boardId)
	{
		this.boardId = boardId;
	}

	public TileRule getRule()
	{
		return rule;
	}

	public void setRule(TileRule rule)
	{
		this.rule = rule;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
