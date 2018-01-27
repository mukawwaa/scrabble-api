package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.BoardUser;

public interface BoardUserService extends BaseService<BoardUser>
{
    BoardUser loadByUserId(Long boardId, Long userId);

    BoardUser getNextUser(Long boardId);

    List<BoardUser> loadAllActiveUsers(Long boardId);
}
