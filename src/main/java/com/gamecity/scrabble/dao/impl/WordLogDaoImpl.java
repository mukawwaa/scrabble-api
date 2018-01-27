package com.gamecity.scrabble.dao.impl;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.WordLogDao;
import com.gamecity.scrabble.entity.WordLog;

@Repository(value = "wordLogDao")
public class WordLogDaoImpl extends AbstractDaoImpl<WordLog> implements WordLogDao
{

}
