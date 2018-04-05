package com.gamecity.scrabble.service.exception;

import java.util.stream.IntStream;

import com.gamecity.scrabble.service.exception.error.BoardError;

public class BoardException extends RuntimeException
{
    private static final long serialVersionUID = -4806117829334593407L;
    private int errorCode;
    private String errorMessage;

    public BoardException(BoardError error)
    {
        this.errorCode = error.getCode();
        this.errorMessage = error.getMessage();
    }

    public BoardException(BoardError error, Object... params)
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
