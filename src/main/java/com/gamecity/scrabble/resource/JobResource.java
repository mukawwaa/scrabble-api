package com.gamecity.scrabble.resource;

import javax.annotation.PostConstruct;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamecity.scrabble.service.BoardService;

@Component
@Path("/jobs")
public class JobResource extends AbstractResource
{
    @Autowired
    private BoardService boardService;

    @PostConstruct
    private void init()
    {
        boardService.stopExpired();
    }

    @POST
    @Path("/boards/stopExpired")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopExpiredBoards()
    {
        return build(boardService.stopExpired());
    }
}
