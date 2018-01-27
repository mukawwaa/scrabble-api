package com.gamecity.scrabble.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gamecity.scrabble.config.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({ @ContextConfiguration(classes = WebConfig.class) })
public abstract class AbstractTestConfiguration
{
    protected static final Long CREATOR_ID = 1L;
    protected static final Long ADMIN_ID = 2L;
    protected static final Long PLAYER_ID = 3L;
    protected static final Long DEFAULT_RULE_ID = 1L;
}
