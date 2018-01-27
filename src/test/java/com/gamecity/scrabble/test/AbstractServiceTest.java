package com.gamecity.scrabble.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.service.RuleService;
import com.gamecity.scrabble.service.UserService;

public abstract class AbstractServiceTest extends AbstractTestConfiguration
{
    @Autowired
    protected UserService userService;

    @Autowired
    protected RuleService ruleService;

    protected User getCreator()
    {
        return userService.get(CREATOR_ID);
    }

    protected User getPlayer()
    {
        return userService.get(PLAYER_ID);
    }

    protected User getAdminUser()
    {
        return userService.get(ADMIN_ID);
    }

    protected Rule getDefaultRule()
    {
        return ruleService.get(DEFAULT_RULE_ID);
    }
}
