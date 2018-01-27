package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.CellRule;

public interface CellRuleDao extends BaseDao<CellRule>
{
	List<CellRule> loadByRuleId(Long ruleId);
}
