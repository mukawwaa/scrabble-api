package com.gamecity.scrabble.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.gamecity.scrabble.entity.BoardChat;
import com.gamecity.scrabble.service.BoardChatService;

@Component
@Path("/chat/board/{boardId}")
public class ChatResource extends BaseResource<BoardChat, BoardChatService>
{
    @POST
    @Path("/user/{userId}/send")
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(@PathParam("boardId") Long boardId, @PathParam("userId") Long userId, String message)
    {
        baseService.send(boardId, userId, message);
        return build();
    }

    @GET
    @Path("/messages/{orderNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessages(@PathParam("boardId") Long boardId, @PathParam("orderNo") Integer orderNo)
    {
        return build(baseService.getMessages(boardId, orderNo));
    }
}
