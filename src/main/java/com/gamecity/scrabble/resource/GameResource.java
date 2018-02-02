package com.gamecity.scrabble.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.service.GameService;

@Component
@Path("/game")
public class GameResource extends AbstractResource
{
    @Autowired
    private GameService gameService;

    @POST
    @Path("/play")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Rack rack)
    {
        gameService.play(rack);
        return build();
    }
}
