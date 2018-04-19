package com.gamecity.scrabble.service;

import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.UserParams;

public interface UserService extends BaseService<User>
{
    User createUser(UserParams params);

    User findByUsername(String username);

    User validateAndGetUser(Long id);
}
