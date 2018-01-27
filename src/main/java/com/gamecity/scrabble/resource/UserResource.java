package com.gamecity.scrabble.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.service.UserService;

@Component
@Path("/users")
public class UserResource extends BaseResource<User, UserService>
{
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserByUsername(@PathParam("username") String username)
    {
        return build(baseService.findByUsername(username));
    }
}
