package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.UserParams;
import com.gamecity.scrabble.service.exception.UserException;
import com.gamecity.scrabble.service.exception.error.UserError;

public class UserTest extends AbstractScrabbleTest
{
    private UserParams params;

    @Before
    public void before()
    {
        super.setUp();
        createSampleUser();
    }

    @Test
    public void testValidateUserEmail()
    {
        params.setEmail("test");
        try
        {
            userService.createUser(params);
        }
        catch (UserException e)
        {
            assertEquals(e.getErrorCode(), UserError.EMAIL_NOT_VALID.getCode());
        }
    }

    @Test
    public void testValidateUniqueUsername()
    {
        params.setUsername("admin");
        try
        {
            userService.createUser(params);
        }
        catch (UserException e)
        {
            assertEquals(e.getErrorCode(), UserError.USERNAME_ALREADY_IN_USE.getCode());
        }
    }

    @Test
    public void testValidateNameAndSurnameLength()
    {
        params.setName("");
        params.setSurname("");
        try
        {
            userService.createUser(params);
        }
        catch (UserException e)
        {
            assertEquals(e.getErrorCode(), UserError.NAME_SURNAME_LENGTH_NOT_ENOUGH.getCode());
        }
    }

    @Test
    public void testValidateNameAndSurnameChars()
    {
        params.setName("12313");
        params.setSurname("jsdfdsfjskf");
        try
        {
            userService.createUser(params);
        }
        catch (UserException e)
        {
            assertEquals(e.getErrorCode(), UserError.NAME_SURNAME_NOT_CONTAINS_VALID_CHARACTERS.getCode());
        }
    }

    @Test
    public void testValidatePasswordStrength()
    {
        params.setPassword("test");
        try
        {
            userService.createUser(params);
        }
        catch (UserException e)
        {
            assertEquals(e.getErrorCode(), UserError.PASSWORD_STRENGTH_NOT_ENOUGH.getCode());
        }
    }

    @Test
    public void testCreateUser()
    {
        params.setUsername(RandomStringUtils.randomAlphabetic(20));
        User user = userService.createUser(params);
        assertNotNull(user.getId());
    }

    private void createSampleUser()
    {
        params = new UserParams();
        params.setEmail("mukawwaa_by_scrabble" + new Random().nextInt(1000) + "@gmail.com");
        params.setName("Jonas");
        params.setSurname("Hector");
        params.setUsername("mukawwaaa");
        params.setPassword("Scrabble.102");
    }

}
