package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.CellRule;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.TileRule;

public interface RuleService extends BaseService<Rule>
{
    List<CellRule> loadCellRules(Long ruleId);

    List<TileRule> loadTileRules(Long ruleId);
}
