package com.gamecity.scrabble.service.exception;

import java.util.stream.IntStream;

import com.gamecity.scrabble.service.exception.error.UserError;

public class UserException extends RuntimeException
{
    private static final long serialVersionUID = -3754201035104668476L;
    private int errorCode;
    private String errorMessage;

    public UserException(UserError error)
    {
        this.errorCode = error.getCode();
        this.errorMessage = error.getMessage();
    }

    public UserException(UserError error, Object... params)
    {
        this(error);
        if (params != null)
        {
            IntStream.range(0, params.length).forEach(index -> errorMessage = errorMessage.replace("{" + index + "}", String.valueOf(params[index])));
        }
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
