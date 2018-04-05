package com.gamecity.scrabble.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils
{
    public static Date nowAsDate()
    {
        return new Date();
    }

    public static long nowAsUnixTime()
    {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
