package com.gamecity.scrabble.resource;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.gamecity.scrabble.model.GameResponse;
import com.gamecity.scrabble.service.exception.GameException;

@Provider
public class GameExceptionMapper extends Exception implements ExceptionMapper<Throwable>
{
    private static final long serialVersionUID = 8887267511588413375L;

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable e)
    {
        if (e instanceof GameException)
        {
            return Response.ok(new GameResponse((GameException) e)).build();
        }
        else
        {
            return Response.ok().status(500).entity(e.getMessage()).build();
        }
    }
}
