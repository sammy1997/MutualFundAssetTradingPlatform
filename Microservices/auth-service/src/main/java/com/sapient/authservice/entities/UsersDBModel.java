package com.sapient.authservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

@Value.Immutable
@Mongo.Repository(collection = "Users")
@JsonSerialize(as = ImmutableUsersDBModel.class)
@JsonDeserialize(as = ImmutableUsersDBModel.class)
public interface UsersDBModel {
    @Mongo.Id
    String userId();
    String password();
    String fullName();
    String role();
}
