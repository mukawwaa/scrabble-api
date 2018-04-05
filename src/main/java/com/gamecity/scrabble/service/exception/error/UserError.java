package com.gamecity.scrabble.service.exception.error;

public enum UserError
{
    EMAIL_NOT_VALID(4001, "User email is not valid!"), //
    USERNAME_ALREADY_IN_USE(4002, "Username already in use!"), //
    NAME_SURNAME_LENGTH_NOT_ENOUGH(4003, "Name or surname length is not enough!"), //
    NAME_SURNAME_NOT_CONTAINS_VALID_CHARACTERS(4004, "Name or surname must contains valid characters!"), //
    PASSWORD_STRENGTH_NOT_ENOUGH(4005, "Password strength is not enough!"), //
    INVALID_USER_ID(4006, "Invalid user id!");

    private int code;
    private String message;

    private UserError(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
