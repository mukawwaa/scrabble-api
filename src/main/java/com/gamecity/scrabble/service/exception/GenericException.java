package com.gamecity.scrabble.service.exception;

import java.util.stream.IntStream;

import com.gamecity.scrabble.service.exception.error.GenericError;

public class GenericException extends RuntimeException
{
    private static final long serialVersionUID = -6914909313518472710L;
    private int errorCode;
    private String errorMessage;

    public GenericException(GenericError error)
    {
        this.errorCode = error.getCode();
        this.errorMessage = error.getMessage();
    }

    public GenericException(GenericError error, Object... params)
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
