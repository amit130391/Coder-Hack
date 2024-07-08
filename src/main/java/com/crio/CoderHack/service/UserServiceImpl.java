package com.crio.CoderHack.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crio.CoderHack.Exception.ScoreOutOfBoundException;
import com.crio.CoderHack.Exception.UserNotFoundException;
import com.crio.CoderHack.entities.Badges;
import com.crio.CoderHack.entities.User;
import com.crio.CoderHack.exchanges.UserRegistrationRequest;
import com.crio.CoderHack.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User registerUser(UserRegistrationRequest userRequest) {
        User user=new User();
        user.setUserId(userRequest.getUserId());
        user.setUserName(userRequest.getUserName());
        user.setScore(0);
        user.setBadge(new ArrayList<>());
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public List<User> getallUsers() {
        List<User> allUsers = userRepository.findAll();
        List<User> users = allUsers.stream().sorted(Comparator.comparingInt(User::getScore)).collect(Collectors.toList());
        return users;
    }

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found with id: "+userId));
    }

    @Override
    public void updateUserScore(String userId,int userScore) throws ScoreOutOfBoundException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isPresent()){
        User user = optionalUser.get();
        int iScore = user.getScore();
        user.addScore(userScore);
        int fScore=user.getScore();
        List<Badges> badge = user.getBadge();
        if(iScore>=0 && iScore<30){
            if(badge.isEmpty()){
                if(fScore>=1 && fScore<30)
                badge.add(Badges.CodeNinja);
                if(fScore>=30 && fScore<60){
                    badge.add(Badges.CodeNinja);
                    badge.add(Badges.CodeChamp);
                }
                if(fScore>=60 && fScore<=100){
                    badge.add(Badges.CodeNinja);
                    badge.add(Badges.CodeChamp);
                    badge.add(Badges.CodeMaster);
                } 
            }
            else{
                if(fScore>=30 && fScore<60){
                    badge.add(Badges.CodeChamp);
                }
                if(fScore>=60 && fScore<=100){
                    badge.add(Badges.CodeChamp);
                    badge.add(Badges.CodeMaster);
                } 
            }
        }
        if(iScore>=30 && iScore<60){
            if(fScore>=1 && fScore<30){
                badge.remove(Badges.CodeChamp);
            }
            if(fScore>=60 && fScore<=100){
                badge.add(Badges.CodeMaster);
            }
        }
        if(iScore>=60 && iScore<=100){
            if(fScore>=1 && fScore<30){
                badge.remove(Badges.CodeMaster);
                badge.remove(Badges.CodeChamp);
            }
            if(fScore>=30 && fScore<60){
                badge.remove(Badges.CodeMaster);
            }
        }
        user.setBadge(badge);
        userRepository.save(user);
        }
        else{
            System.out.println("User not found");
        }
    }

    @Override
    public void deleteUser(String userId) {
       userRepository.deleteById(userId);
    }
    
}
