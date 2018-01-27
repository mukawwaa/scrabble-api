package com.gamecity.scrabble.util;

import java.util.stream.IntStream;

import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;

public class ValidationUtils
{
    public static void validateParameters(Object... params) throws GameException
    {
        IntStream.range(0, params.length).forEach(i -> {
            if (params[i] == null)
            {
                throw new GameException(GameError.INVALID_PARAMETERS);
            }
        });
    }
}
