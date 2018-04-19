package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.UserDao;
import com.gamecity.scrabble.entity.User;

@Repository(value = "userDao")
public class UserDaoImpl extends AbstractDaoImpl<User> implements UserDao
{
    @Override
    public User findByUsername(String username)
    {
        return findByNamedQuery("findByUsername", Arrays.asList("username"), username);
    }

    @Override
    public User findByEmail(String email)
    {
        return findByNamedQuery("findByEMail", Arrays.asList("email"), email);
    }
}
