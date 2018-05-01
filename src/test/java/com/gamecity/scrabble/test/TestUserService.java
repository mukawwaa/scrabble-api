package com.gamecity.scrabble.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.gamecity.scrabble.dao.UserDao;
import com.gamecity.scrabble.dao.UserRoleDao;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.UserParams;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.UserException;
import com.gamecity.scrabble.service.exception.error.UserError;
import com.gamecity.scrabble.service.impl.UserServiceImpl;

public class TestUserService extends AbstractMockTest
{
    private static final long DEFAULT_USERID = 1L;
    private UserParams userParams;

    @InjectMocks
    public UserService userService = new UserServiceImpl();

    @Mock
    public UserDao userDao;

    @Mock
    public UserRoleDao userRoleDao;

    @Before
    public void before()
    {
        MockitoAnnotations.initMocks(userService);
        userParams = createSampleUser();
    }

    @Test
    public void testValidateUserEmail()
    {
        userParams.setEmail("tester");
        try
        {
            userService.createUser(userParams);
            fail("Failed to validate user email.");
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.EMAIL_NOT_VALID.getCode()));
        }
    }

    @Test
    public void testValidateUniqueEmail()
    {
        userParams.setEmail("tester@gmail.com");
        when(userDao.findByEmail(eq("tester@gmail.com"))).thenReturn(mock(User.class));
        try
        {
            userService.createUser(userParams);
            fail("Failed to validate unique email.");
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.EMAIL_ALREADY_IN_USE.getCode()));
        }
    }

    @Test
    public void testValidateUniqueUsername()
    {
        userParams.setUsername("admin");
        when(userDao.findByUsername(eq("admin"))).thenReturn(mock(User.class));
        try
        {
            userService.createUser(userParams);
            fail("Failed to validate unique username.");
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.USERNAME_ALREADY_IN_USE.getCode()));
        }
    }

    @Test
    public void testValidateNameAndSurnameLength()
    {
        userParams.setName("");
        userParams.setSurname("");
        try
        {
            userService.createUser(userParams);
            fail("Failed to validate name and surname are length.");
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.NAME_SURNAME_LENGTH_NOT_ENOUGH.getCode()));
        }
    }

    @Test
    public void testValidateNameAndSurnameAlphabetic()
    {
        userParams.setName("12313");
        userParams.setSurname("jsdfdsfjskf");
        try
        {
            userService.createUser(userParams);
            fail("Failed to validate name and surname are alphabetic.");
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.NAME_SURNAME_NOT_CONTAINS_VALID_CHARACTERS.getCode()));
        }
    }

    @Test
    public void testValidatePasswordStrength()
    {
        userParams.setPassword("test");
        try
        {
            userService.createUser(userParams);
            fail("Failed to validate password strength.");
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.PASSWORD_STRENGTH_NOT_ENOUGH.getCode()));
        }
    }

    @Test
    public void testCreateUser()
    {
        userParams.setUsername(RandomStringUtils.randomAlphabetic(20));
        when(userDao.save(any(User.class))).thenReturn(mock(User.class));
        assertThat(userService.createUser(userParams), is(notNullValue()));
    }

    @Test
    public void testFindExistingUserByUsername()
    {
        String username = "tester";
        when(userDao.findByUsername(eq(username))).thenReturn(mock(User.class));
        when(userRoleDao.findByUsername(eq(username))).thenReturn(Arrays.asList("ROLE_USER"));
        assertThat(userService.findByUsername(username), is(notNullValue()));
    }

    @Test
    public void testValidateFindInvalidUsername()
    {
        String username = "tester";
        try
        {
            userService.findByUsername(username);
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.INVALID_USERNAME.getCode()));
        }
    }

    @Test
    public void testValidateFindInvalidUserid()
    {
        try
        {
            userService.validateAndGetUser(DEFAULT_USERID);
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.INVALID_USERID.getCode()));
        }
    }

    @Test
    public void testValidateFindDisabledUser()
    {
        when(userDao.get(eq(DEFAULT_USERID))).thenAnswer(new Answer<User>()
        {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable
            {
                User user = new User();
                user.setEnabled(false);
                return user;
            }
        });
        try
        {
            userService.validateAndGetUser(DEFAULT_USERID);
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.USER_DISABLED.getCode()));
        }
    }

    @Test
    public void testValidateFindAccountExpiredUser()
    {
        when(userDao.get(eq(DEFAULT_USERID))).thenAnswer(new Answer<User>()
        {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable
            {
                User user = new User();
                user.setAccountNonExpired(false);
                return user;
            }
        });
        try
        {
            userService.validateAndGetUser(DEFAULT_USERID);
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.USER_ACCOUNT_EXPIRED.getCode()));
        }
    }

    @Test
    public void testValidateFindAccountLockedUser()
    {
        when(userDao.get(eq(DEFAULT_USERID))).thenAnswer(new Answer<User>()
        {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable
            {
                User user = new User();
                user.setAccountNonLocked(false);
                return user;
            }
        });
        try
        {
            userService.validateAndGetUser(DEFAULT_USERID);
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.USER_ACCOUNT_LOCKED.getCode()));
        }
    }

    @Test
    public void testValidateFindCredentialsExpiredUser()
    {
        when(userDao.get(eq(DEFAULT_USERID))).thenAnswer(new Answer<User>()
        {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable
            {
                User user = new User();
                user.setCredentialsNonExpired(false);
                return user;
            }
        });
        try
        {
            userService.validateAndGetUser(DEFAULT_USERID);
        }
        catch (UserException e)
        {
            assertThat(e.getErrorCode(), equalTo(UserError.USER_CREDENTIALS_EXPIRED.getCode()));
        }
    }
}
