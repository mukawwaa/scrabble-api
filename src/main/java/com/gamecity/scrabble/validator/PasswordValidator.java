package com.gamecity.scrabble.validator;

import java.util.regex.Pattern;

import com.gamecity.scrabble.service.exception.UserException;
import com.gamecity.scrabble.service.exception.error.UserError;
import com.gamecity.scrabble.util.ValidationUtils;

public class PasswordValidator implements RuleValidator
{
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\\\\\\\s]).{6,}";
    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void validate(Object item)
    {
        ValidationUtils.validateParameters(item);
        String password = (String) item;
        if (!pattern.matcher(password).matches())
        {
            throw new UserException(UserError.PASSWORD_STRENGTH_NOT_ENOUGH);
        }
    }
}
