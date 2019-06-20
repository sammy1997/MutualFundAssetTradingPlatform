package com.sapient.galleryservice.controllers;

import com.sapient.galleryservice.entities.Gallery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

//@RestController
//@RequestMapping("/")
@Path("/")
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

//    @RequestMapping("/")
    @GET
    @Produces("text/plain")
    public String home() {
        // This is useful for debugging
        // When having multiple instance of gallery service running at different ports.
        // We load balance among them, and display which instance received the request.

        return "Hello from Gallery Service running at port: " + env.getProperty("local.server.port");
    }

//    @RequestMapping("/{id}")
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Gallery getGallery(@PathVariable final int id) {
        // create gallery object
        Gallery gallery = new Gallery();
        gallery.setId(id);

        // get list of available images
        List<Object> images = restTemplate.getForObject("http://image-service/images/", List.class);
        gallery.setImages(images);

        return gallery;
    }

    // -------- Admin Area --------
    // This method should only be accessed by users with role of 'admin'
//    @RequestMapping("/admin")
    @GET
    @Path("/admin")
    @Produces("text/plain")
    public String homeAdmin() {
        return "This is the admin area of Gallery service running at port: " + env.getProperty("local.server.port");
    }

//    @RequestMapping("/trader")
    @GET
    @Path("/trader")
    @Produces("text/plain")
    public String homeTrader() {
        return "This is the trade area of Gallery service running at port: " + env.getProperty("local.server.port");
    }

//    @RequestMapping("/viewer")
    @GET
    @Path("/viewer")
    @Produces("text/plain")
    public String homeViewer() {
        return "This is the view area of Gallery service running at port: " + env.getProperty("local.server.port");
    }
}
