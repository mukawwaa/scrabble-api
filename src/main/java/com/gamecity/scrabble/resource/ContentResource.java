package com.gamecity.scrabble.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamecity.scrabble.service.ContentService;

@Component
@Path("/content/board/{boardId}")
public class ContentResource extends AbstractResource
{
    @Autowired
    private ContentService contentService;

    @GET
    @Path("/players/orderNo/{orderNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayers(@PathParam("boardId") Long boardId, @PathParam("orderNo") Integer orderNo)
    {
        return build(contentService.getPlayers(boardId, orderNo));
    }

    @GET
    @Path("/orderNo/{orderNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContent(@PathParam("boardId") Long boardId, @PathParam("orderNo") Integer orderNo)
    {
        return build(contentService.getContent(boardId, orderNo));
    }

    @GET
    @Path("/rack/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRack(@PathParam("boardId") Long boardId, @PathParam("userId") Long userId)
    {
        return build(contentService.getRack(boardId, userId));
    }
}
