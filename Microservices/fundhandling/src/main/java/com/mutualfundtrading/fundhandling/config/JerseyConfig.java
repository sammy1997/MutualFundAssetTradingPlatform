package com.mutualfundtrading.fundhandling.config;

import com.mutualfundtrading.fundhandling.controllers.EntitlementController;
import com.mutualfundtrading.fundhandling.controllers.FundController;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(FundController.class);
        register(EntitlementController.class);
        register(MultiPartFeature.class);
    }
}
