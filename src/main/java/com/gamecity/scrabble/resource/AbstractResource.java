package com.gamecity.scrabble.resource;

import javax.ws.rs.core.Response;

import com.gamecity.scrabble.model.GameResponse;

public abstract class AbstractResource
{
    protected Response build(Object data)
    {
        return Response.ok(new GameResponse(data)).build();
    }

    protected Response build()
    {
        return Response.ok(new GameResponse()).build();
    }
}
