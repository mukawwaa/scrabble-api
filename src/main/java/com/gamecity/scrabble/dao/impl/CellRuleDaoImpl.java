package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.CellRuleDao;
import com.gamecity.scrabble.entity.CellRule;

@Repository(value = "cellRuleDao")
public class CellRuleDaoImpl extends AbstractDaoImpl<CellRule> implements CellRuleDao
{
	@Override
	public List<CellRule> loadByRuleId(Long ruleId)
	{
		return listByNamedQuery("loadAllCellRules", Arrays.asList("ruleId"), ruleId);
	}
}
