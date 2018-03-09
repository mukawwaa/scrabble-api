package com.gamecity.scrabble.service;

import com.gamecity.scrabble.model.BoardCell;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.BoardPlayer;
import com.gamecity.scrabble.model.BoardTile;
import com.gamecity.scrabble.model.Rack;

public interface ContentService
{
    BoardCell getCell(Long boardId, Long ruleId, Integer rowNumber, Integer columnNumber);

    BoardCell updateCell(BoardCell cell);

    BoardTile getTile(Long boardId, Long ruleId, String letter);

    BoardTile updateTile(BoardTile tile);

    BoardPlayer getPlayers(Long boardId, Integer orderNo);

    void updatePlayers(Long boardId, Integer orderNo, boolean started);

    BoardContent getContent(Long boardId, Integer orderNo);

    void updateContent(Long id, Integer orderNo);

    Rack getRack(Long boardId, Long userId);

    Rack updateRack(Rack rack);
}
