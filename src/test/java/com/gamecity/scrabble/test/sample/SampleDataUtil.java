package com.gamecity.scrabble.test.sample;

import org.apache.commons.lang.RandomStringUtils;

import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.model.UserParams;

public class SampleDataUtil
{
    public static BoardParams createSampleBoard(User user, Rule rule, Integer userCount)
    {
        BoardParams params = new BoardParams();
        params.setDuration(1);
        params.setName(RandomStringUtils.randomAlphabetic(10));
        params.setRuleId(rule.getId());
        params.setUserCount(userCount);
        params.setUserId(user.getId());
        return params;
    }

    public static UserParams createSampleUser()
    {
        UserParams params = new UserParams();
        params.setEmail(RandomStringUtils.randomAlphabetic(15).toLowerCase() + "@gmail.com");
        params.setName(RandomStringUtils.randomAlphabetic(5).toLowerCase());
        params.setSurname(RandomStringUtils.randomAlphabetic(10).toLowerCase());
        params.setUsername(RandomStringUtils.randomAlphabetic(8).toLowerCase());
        params.setPassword("Scrabble.102");
        return params;
    }
}
