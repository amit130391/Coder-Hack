package com.crio.CoderHack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crio.CoderHack.entities.User;

public interface UserRepository extends MongoRepository<User,String>{
    
}
