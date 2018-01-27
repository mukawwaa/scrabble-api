package com.gamecity.scrabble.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Rack implements Serializable
{
	private static final long serialVersionUID = 4526277916796134387L;
	private Long boardId;
	private Long userId;
	private List<RackTile> tiles;

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

	public List<RackTile> getTiles()
	{
		return tiles;
	}

	public void setTiles(List<RackTile> tiles)
	{
		this.tiles = tiles;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
