package com.gamecity.scrabble.service.impl;

import org.springframework.stereotype.Service;

import com.gamecity.scrabble.dao.WordLogDao;
import com.gamecity.scrabble.entity.WordLog;
import com.gamecity.scrabble.service.WordLogService;

@Service(value = "wordLogService")
public class WordLogServiceImpl extends AbstractServiceImpl<WordLog, WordLogDao> implements WordLogService
{

}
