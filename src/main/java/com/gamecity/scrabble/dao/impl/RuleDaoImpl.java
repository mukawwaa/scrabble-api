package com.gamecity.scrabble.dao.impl;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.RuleDao;
import com.gamecity.scrabble.entity.Rule;

@Repository(value = "ruleDao")
public class RuleDaoImpl extends AbstractDaoImpl<Rule> implements RuleDao
{

}
