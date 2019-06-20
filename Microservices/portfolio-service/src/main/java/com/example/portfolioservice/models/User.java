package com.example.portfolioservice.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
@Document(collection = "User")
public class User {
    @Id
    private String userId;

   // String username;
    ArrayList<Fund> all_funds = new ArrayList<>();

    public User()
    {

    }
    public User(String id)
    {
        userId = id;
     //   username = name;
    }

    public ArrayList<Fund> getAll_funds()
    {
        return all_funds;
    }

    public void setAll_funds(ArrayList<Fund> all_funds)
    {
        this.all_funds = all_funds;
    }


    public String getUserid()
    {
        return userId;
    }

    public void setUserid(String userid)
    {
        this.userId = userid;
    }

}
