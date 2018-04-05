package com.gamecity.scrabble.validator;

import java.util.regex.Pattern;

import com.gamecity.scrabble.service.exception.UserException;
import com.gamecity.scrabble.service.exception.error.UserError;
import com.gamecity.scrabble.util.ValidationUtils;

public class NameSurnameValidator implements RuleValidator
{
    private static final String NAME_PATTERN = "[a-zA-Z]+";
    private final Pattern pattern = Pattern.compile(NAME_PATTERN);

    @Override
    public void validate(Object item)
    {
        ValidationUtils.validateParameters(item);
        String name = (String) item;
        if (name == null || name.trim().isEmpty() || name.trim().length() <= 1)
        {
            throw new UserException(UserError.NAME_SURNAME_LENGTH_NOT_ENOUGH);
        }

        if (!pattern.matcher(name).matches())
        {
            throw new UserException(UserError.NAME_SURNAME_NOT_CONTAINS_VALID_CHARACTERS);
        }
    }
}
