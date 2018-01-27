package com.gamecity.scrabble.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.service.BoardService;

@Component
@Path("/boards")
public class BoardResource extends BaseResource<Board, BoardService>
{
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(BoardParams params)
    {
        return build(baseService.create(params));
    }

    @POST
    @Path("/{boardId}/user/{userId}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public Response join(@PathParam("boardId") Long boardId, @PathParam("userId") Long userId)
    {
        return build(baseService.join(boardId, userId));
    }

    @POST
    @Path("/{boardId}/user/{userId}/leave")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leave(@PathParam("boardId") Long boardId, @PathParam("userId") Long userId)
    {
        return build(baseService.leave(boardId, userId));
    }

    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveBoards()
    {
        return build(baseService.getActiveBoards());
    }

    @GET
    @Path("/user/{userId}/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveBoardsByUser(@PathParam("userId") Long userId)
    {
        return build(baseService.getActiveBoardsByUser(userId));
    }
}
