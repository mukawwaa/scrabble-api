package com.gamecity.scrabble.util;

import java.util.stream.IntStream;

import com.gamecity.scrabble.service.exception.GenericException;
import com.gamecity.scrabble.service.exception.error.GenericError;

public class ValidationUtils
{
    public static void validateParameters(Object... params) throws GenericException
    {
        IntStream.range(0, params.length).forEach(i -> {
            if (params[i] == null)
            {
                throw new GenericException(GenericError.INVALID_PARAMETERS);
            }
        });
    }
}
