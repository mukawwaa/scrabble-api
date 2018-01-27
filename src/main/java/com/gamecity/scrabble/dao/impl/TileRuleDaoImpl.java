package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.TileRuleDao;
import com.gamecity.scrabble.entity.TileRule;

@Repository(value = "letterRuleDao")
public class TileRuleDaoImpl extends AbstractDaoImpl<TileRule> implements TileRuleDao
{
    @Override
    public List<TileRule> loadByRuleId(Long ruleId)
    {
        return listByNamedQuery("loadAllTileRules", Arrays.asList("ruleId"), ruleId);
    }
}
