package com.gamecity.scrabble.dao;

import com.gamecity.scrabble.entity.User;

public interface UserDao extends BaseDao<User>
{
    User findByUsername(String username);

    User findByEmail(String email);
}
