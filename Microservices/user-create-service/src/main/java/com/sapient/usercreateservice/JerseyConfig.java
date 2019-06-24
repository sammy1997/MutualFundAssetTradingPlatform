package com.sapient.usercreateservice;

import com.sapient.usercreateservice.controller.HomeController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(HomeController.class);
    }
}
