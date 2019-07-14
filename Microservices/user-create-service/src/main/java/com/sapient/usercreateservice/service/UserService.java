package com.sapient.usercreateservice.service;

import com.google.common.base.Optional;
import com.sapient.usercreateservice.dao.UserDAO;
import com.sapient.usercreateservice.entities.ImmutableUser;
import com.sapient.usercreateservice.entities.ParsedUser;
import com.sapient.usercreateservice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;


@Service
public class UserService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserDAO dao;

    public Response addUser(ParsedUser users){
        return dao.insert(users);
    }


    public List<ImmutableUser> getAllUsers() {
        return dao.getAllUsers();
    }

    public Response updateUser(ParsedUser user){
        Optional<User> userOptional = dao.getUser(user.userId());
        if (!userOptional.isPresent()){
            return Response.status(404)
            .entity("User doesn't exist in our database").build();
        }
        User updatedUser = ImmutableUser.builder().userId(user.userId())
                .password(user.password().equals("")? userOptional.get().password() : encoder.encode(user.password()))
                .role(user.role().isPresent()? user.role().get() : userOptional.get().role())
                .fullName(user.fullName().isPresent()? user.fullName().get() : userOptional.get().role())
                .build();
        dao.updateUser(updatedUser);
        return Response.status(200).entity("User details for "
        + user.userId() + "updated").build();
    }
}
