package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.CellRuleDao;
import com.gamecity.scrabble.dao.RuleDao;
import com.gamecity.scrabble.dao.TileRuleDao;
import com.gamecity.scrabble.entity.CellRule;
import com.gamecity.scrabble.entity.TileRule;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.service.RuleService;

@Service(value = "ruleService")
public class RuleServiceImpl extends AbstractServiceImpl<Rule, RuleDao> implements RuleService
{
    @Autowired
    private CellRuleDao cellRuleDao;

    @Autowired
    private TileRuleDao letterRuleDao;

    @Override
    public Rule get(Long id)
    {
        return baseDao.get(id);
    }

    @Override
    @Cacheable(value = Constants.Cache.CELL_RULES, key = "#ruleId")
    public List<CellRule> loadCellRules(Long ruleId)
    {
        return cellRuleDao.loadByRuleId(ruleId);
    }

    @Override
    @Cacheable(value = Constants.Cache.TILE_RULES, key = "#ruleId")
    public List<TileRule> loadTileRules(Long ruleId)
    {
        return letterRuleDao.loadByRuleId(ruleId);
    }
}
