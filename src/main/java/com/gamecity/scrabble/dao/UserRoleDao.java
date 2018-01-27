package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.UserRole;

public interface UserRoleDao extends BaseDao<UserRole>
{
    List<String> findByUsername(String username);
}
