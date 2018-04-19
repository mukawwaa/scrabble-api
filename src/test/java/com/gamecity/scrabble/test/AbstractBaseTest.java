package com.gamecity.scrabble.test;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.model.UserParams;

public abstract class AbstractBaseTest
{
    protected static final Long DEFAULT_RULE_ID = 1L;
    protected static final Long DEFAULT_BOARD_ID = 1L;

    protected UserParams createSampleUser()
    {
        UserParams userParams = new UserParams();
        userParams.setEmail("mukawwaa_by_scrabble" + new Random().nextInt(1000) + "@gmail.com");
        userParams.setName("Jonas" + RandomStringUtils.randomAlphabetic(5));
        userParams.setSurname("Hector");
        userParams.setUsername("mukawwaaa");
        userParams.setPassword("Scrabble.102");
        return userParams;
    }

    protected BoardParams createSampleBoard(Long userId, Integer userCount)
    {
        BoardParams params = new BoardParams();
        params.setDuration(2);
        params.setName(RandomStringUtils.randomAlphabetic(10));
        params.setRuleId(DEFAULT_RULE_ID);
        params.setUserCount(userCount);
        params.setUserId(userId);
        return params;
    }

    protected Board createMockBoard(User creator, Integer userCount)
    {
        Board board = new Board();
        board.setCurrentUser(creator);
        board.setDuration(2);
        board.setId(DEFAULT_BOARD_ID);
        board.setName(RandomStringUtils.randomAlphabetic(10));
        board.setOwner(creator);
        board.setRule(createSampleRule());
        board.setStatus(BoardStatus.WAITING_PLAYERS);
        board.setUserCount(userCount);
        return board;
    }

    protected Rule createSampleRule()
    {
        Rule rule = new Rule();
        rule.setCellSize(255);
        rule.setColumnSize(15);
        rule.setId(DEFAULT_RULE_ID);
        rule.setLanguage("tr");
        rule.setRackSize(7);
        rule.setRowSize(15);
        return rule;
    }
}
