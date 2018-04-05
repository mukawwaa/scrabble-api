package com.gamecity.scrabble.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.UserDao;
import com.gamecity.scrabble.dao.UserRoleDao;
import com.gamecity.scrabble.entity.BaseAuthority;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.entity.UserRole;
import com.gamecity.scrabble.model.UserParams;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.UserException;
import com.gamecity.scrabble.service.exception.error.UserError;
import com.gamecity.scrabble.validator.EmailValidator;
import com.gamecity.scrabble.validator.NameSurnameValidator;
import com.gamecity.scrabble.validator.PasswordValidator;

@Service(value = "userService")
public class UserServiceImpl extends AbstractServiceImpl<User, UserDao> implements UserService
{
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    @Transactional
    public User createUser(UserParams params)
    {
        new EmailValidator().validate(params.getEmail());
        new PasswordValidator().validate(params.getPassword());
        new NameSurnameValidator().validate(params.getName());
        new NameSurnameValidator().validate(params.getSurname());

        User existingUser = baseDao.findByUsername(params.getUsername());
        if (existingUser != null)
        {
            throw new UserException(UserError.USERNAME_ALREADY_IN_USE);
        }

        User user = new User();
        user.setEmail(params.getEmail());
        user.setName(params.getName());
        user.setPassword(params.getPassword());
        user.setSurname(params.getSurname());
        user.setUsername(params.getUsername());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        user = baseDao.save(user);

        UserRole userRole = new UserRole();
        userRole.setRolename(Constants.Role.ROLE_USER);
        userRole.setUsername(user.getUsername());
        userRoleDao.save(userRole);

        return user;
    }

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
            throw new UserException(UserError.INVALID_USER_ID);
        }
        return user;
    }
}
