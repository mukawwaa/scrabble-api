package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.TileRule;

public interface TileRuleDao extends BaseDao<TileRule>
{
    List<TileRule> loadByRuleId(Long ruleId);
}
