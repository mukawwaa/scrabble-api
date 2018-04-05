package com.gamecity.scrabble.service.exception.error;

public enum GenericError
{
    INVALID_PARAMETERS(1001, "Invalid parameters!");

    private int code;
    private String message;

    private GenericError(int code, String message)
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
