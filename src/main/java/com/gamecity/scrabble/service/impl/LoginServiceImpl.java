package com.gamecity.scrabble.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.service.UserService;

@Service(value = "userDetailsService")
public class LoginServiceImpl implements UserDetailsService
{
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userService.findByUsername(username);
        if (user == null || !user.isEnabled() || !user.isAccountNonExpired() || !user.isAccountNonLocked())
        {
            throw new UsernameNotFoundException("Invalid user!");
        }
        return user;
    }
}
