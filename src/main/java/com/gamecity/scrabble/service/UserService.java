package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.UserParams;

public interface UserService extends BaseService<User>
{
    User createUser(UserParams params);

    User findByUsername(String username);

    List<String> findRolesByUsername(String username);

    User validateUser(Long id);
}
