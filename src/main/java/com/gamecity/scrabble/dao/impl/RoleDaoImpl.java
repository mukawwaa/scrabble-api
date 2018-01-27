package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.RoleDao;
import com.gamecity.scrabble.entity.Role;

@Repository(value = "roleDao")
public class RoleDaoImpl extends AbstractDaoImpl<Role> implements RoleDao
{
    @Override
    public Role findByRoleName(String rolename)
    {
        return findByNamedQuery("findByRoleName", Arrays.asList("rolename"), rolename);
    }
}
