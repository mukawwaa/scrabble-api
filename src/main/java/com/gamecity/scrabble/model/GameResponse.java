package com.gamecity.scrabble.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gamecity.scrabble.service.exception.GameException;

public class GameResponse
{
    private int responseCode;
    private String responseMessage;
    private Object data;

    public GameResponse()
    {
        this.responseCode = 200;
    }

    public GameResponse(Object data)
    {
        this.data = data;
        this.responseCode = 200;
    }

    public GameResponse(GameException exception)
    {
        this.responseCode = exception.getErrorCode();
        this.responseMessage = exception.getErrorMessage();
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getResponseMessage()
    {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage)
    {
        this.responseMessage = responseMessage;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
