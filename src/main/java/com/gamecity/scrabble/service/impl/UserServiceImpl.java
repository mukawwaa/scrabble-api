package com.gamecity.scrabble.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.dao.UserDao;
import com.gamecity.scrabble.dao.UserRoleDao;
import com.gamecity.scrabble.entity.BaseAuthority;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;

@Service(value = "userService")
public class UserServiceImpl extends AbstractServiceImpl<User, UserDao> implements UserService
{
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public User findByUsername(String username)
    {
        User user = baseDao.findByUsername(username);
        List<String> roleList = findRolesByUsername(username);
        List<BaseAuthority> authorities = new ArrayList<BaseAuthority>();
        for (String role : roleList)
        {
            authorities.add(new BaseAuthority(role));
        }
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public List<String> findRolesByUsername(String username)
    {
        return userRoleDao.findByUsername(username);
    }

    @Override
    public User validateUser(Long id)
    {
        User user = get(id);
        if (user == null || !user.isEnabled() || !user.isAccountNonExpired() || !user.isAccountNonLocked() || !user.isCredentialsNonExpired())
        {
            throw new GameException(GameError.INVALID_USER_ID);
        }
        return user;
    }
}
