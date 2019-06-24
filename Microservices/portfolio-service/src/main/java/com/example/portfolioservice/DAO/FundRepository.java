package com.example.portfolioservice.DAO;
import com.example.portfolioservice.models.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FundRepository extends MongoRepository<Fund, String>
{

}
