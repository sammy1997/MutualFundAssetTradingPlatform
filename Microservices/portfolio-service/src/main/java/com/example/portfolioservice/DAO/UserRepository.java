package com.example.portfolioservice.DAO;
import com.example.portfolioservice.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String>
{
}
