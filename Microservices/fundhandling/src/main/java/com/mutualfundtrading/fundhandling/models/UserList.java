package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<ImmutableUser> users=new ArrayList<>();

    public UserList(){
    }

    public List<ImmutableUser> getUsers() {
        return users;
    }

    public void setUsers(List<ImmutableUser> users) {
        this.users = users;
    }
}
