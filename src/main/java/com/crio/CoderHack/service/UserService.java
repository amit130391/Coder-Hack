package com.crio.CoderHack.service;

import java.util.List;

import com.crio.CoderHack.Exception.ScoreOutOfBoundException;
import com.crio.CoderHack.entities.User;
import com.crio.CoderHack.exchanges.UserRegistrationRequest;

public interface UserService {
    User registerUser(UserRegistrationRequest userRequest);
    List<User> getallUsers();
    User getUser(String userId);
    void updateUserScore(String userId,int score) throws ScoreOutOfBoundException;
    void deleteUser(String userId);
}
