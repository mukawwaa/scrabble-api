package com.gamecity.scrabble.service.exception.error;

public enum UserError
{
    EMAIL_NOT_VALID(4001, "User e-mail is not valid!"), //
    EMAIL_ALREADY_IN_USE(4002, "E-mail already in use!"), //
    USERNAME_ALREADY_IN_USE(4003, "Username already in use!"), //
    NAME_SURNAME_LENGTH_NOT_ENOUGH(4004, "Name or surname length is not enough!"), //
    NAME_SURNAME_NOT_CONTAINS_VALID_CHARACTERS(4005, "Name or surname must contains valid characters!"), //
    PASSWORD_STRENGTH_NOT_ENOUGH(4006, "Password must contain 1 capital letter, 1 short letter, 1 number and 1 special character!"), //
    INVALID_USERID(4007, "Invalid user id!"), //
    INVALID_USERNAME(4008, "Invalid username!"), //
    USER_DISABLED(4009, "User is disabled!"), //
    USER_ACCOUNT_EXPIRED(4010, "User account is expired!"), //
    USER_ACCOUNT_LOCKED(4011, "User account is locked"), //
    USER_CREDENTIALS_EXPIRED(4012, "User credentials is expired");

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
