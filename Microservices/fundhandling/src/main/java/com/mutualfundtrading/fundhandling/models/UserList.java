package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableUserList.class)
@JsonDeserialize(as = ImmutableUserList.class)
public interface UserList {
    List<ImmutableUser> users();
}
