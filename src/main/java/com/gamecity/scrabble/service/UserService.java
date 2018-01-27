package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.User;

public interface UserService extends BaseService<User>
{
    User findByUsername(String username);

    List<String> findRolesByUsername(String username);

    User checkValidUser(Long id);
}
