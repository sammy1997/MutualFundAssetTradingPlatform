package com.sapient.galleryservice;

import com.sapient.galleryservice.controllers.HomeController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(HomeController.class);
    }
}
