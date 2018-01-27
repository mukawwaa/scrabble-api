package com.gamecity.scrabble.service.exception;

import java.util.stream.IntStream;

public class GameException extends RuntimeException
{
    private static final long serialVersionUID = -3257022882385328074L;
    private int errorCode;
    private String errorMessage;

    public GameException(GameError error)
    {
        this.errorCode = error.getCode();
        this.errorMessage = error.getMessage();
    }

    public GameException(GameError error, Object... params)
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
