package com.gamecity.scrabble.dao;

import com.gamecity.scrabble.entity.Role;

public interface RoleDao extends BaseDao<Role>
{
    Role findByRoleName(String rolename);
}
