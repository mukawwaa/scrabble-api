package com.gamecity.scrabble.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.RuleService;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.test.sample.SampleDataUtil;

public abstract class AbstractScrabbleTest extends AbstractTestConfiguration
{
    protected Rule rule;

    @Autowired
    protected UserService userService;

    @Autowired
    protected BoardService boardService;

    @Autowired
    protected RuleService ruleService;

    protected void setUp()
    {
        rule = getDefaultRule();
    }

    protected User createUser()
    {
        return userService.createUser(SampleDataUtil.createSampleUser());
    }

    protected Board createBoard(User user, Integer playerCount)
    {
        return boardService.create(SampleDataUtil.createSampleBoard(user, rule, playerCount));
    }

    protected Rule getDefaultRule()
    {
        return ruleService.get(DEFAULT_RULE_ID);
    }

    protected void sleep(int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e)
        {
            // nothing to do
        }
    }
}
