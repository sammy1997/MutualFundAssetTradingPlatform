package com.mutualfundtrading.fundhandling.config;

import com.mutualfundtrading.fundhandling.controllers.FundController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(FundController.class);
    }
}
