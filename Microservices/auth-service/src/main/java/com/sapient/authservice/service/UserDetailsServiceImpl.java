package com.sapient.authservice.service;


import com.sapient.authservice.dao.UserDAO;
import com.sapient.authservice.entities.ImmutableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDAO dao;

    public ImmutableUser findByUserId(String userId){
        return dao.getUserByUserId(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        ImmutableUser user = findByUserId(userId);
        String id = user.userId();

        if(id == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
            if (id.equals(userId)) {

                // Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
                // So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN") or hasAnyRole("TRADER", "ADMIN")).
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.role());
                System.out.println(user.role());
                // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
                // And used by auth manager to verify and check user authentication.
                return new User(user.userId(), user.password(), grantedAuthorities);
            }


        // If user not found, throw this exception.
        throw new UsernameNotFoundException("Username: " + userId + " not found");
    }


}
