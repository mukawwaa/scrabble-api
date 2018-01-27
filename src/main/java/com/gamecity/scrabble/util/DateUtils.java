package com.gamecity.scrabble.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils
{
    public static long nowAsUnixTime()
    {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
