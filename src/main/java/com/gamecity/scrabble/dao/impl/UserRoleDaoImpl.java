package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.UserRoleDao;
import com.gamecity.scrabble.entity.UserRole;

@Repository(value = "userRoleDao")
public class UserRoleDaoImpl extends AbstractDaoImpl<UserRole> implements UserRoleDao
{
    @Override
    public List<String> findByUsername(String username)
    {
        return listGenericByNamedQuery("findRolesByUsername", Arrays.asList("username"), username);
    }
}
