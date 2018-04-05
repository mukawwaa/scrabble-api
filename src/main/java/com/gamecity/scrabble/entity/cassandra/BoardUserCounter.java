package com.gamecity.scrabble.entity.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = BoardUserCounter.TABLE_NAME)
public class BoardUserCounter
{
    public static final String TABLE_NAME = "board_user_counter";
    public static final String BOARD_ID = "board_id";
    public static final String PLAYER_COUNT = "player_count";
    public static final String ACTION_COUNT = "action_count";

    @PartitionKey
    @Column(name = BoardUserCounter.BOARD_ID)
    private Long boardId;

    @Column(name = BoardUserCounter.PLAYER_COUNT)
    private Long playerCount;

    @Column(name = BoardUserCounter.ACTION_COUNT)
    private Long actionCount;

    public Long getBoardId()
    {
        return boardId;
    }

    public void setBoardId(Long boardId)
    {
        this.boardId = boardId;
    }

    public Long getPlayerCount()
    {
        return playerCount;
    }

    public void setPlayerCount(Long playerCount)
    {
        this.playerCount = playerCount;
    }

    public Long getActionCount()
    {
        return actionCount;
    }

    public void setActionCount(Long actionCount)
    {
        this.actionCount = actionCount;
    }

}
