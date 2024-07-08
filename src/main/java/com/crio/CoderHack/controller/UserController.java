package com.crio.CoderHack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crio.CoderHack.Exception.ScoreOutOfBoundException;
import com.crio.CoderHack.Exception.UserNotFoundException;
import com.crio.CoderHack.entities.User;
import com.crio.CoderHack.exchanges.UserRegistrationRequest;
import com.crio.CoderHack.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getallUsers();
        if(users.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String userId){
        try {
            User user = userService.getUser(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        
        
    }
    
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRegistrationRequest userRequest,BindingResult result){
        User user = userService.registerUser(userRequest);
        if(result.hasErrors()){
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(error->errors.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(errors.toString(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object>  updateScore(@RequestParam(name="score") int score,@PathVariable("id") String userId){
        try {
            User user = userService.getUser(userId);
            if(score<1)
            return new ResponseEntity<>("Score must be greater than 0",HttpStatus.BAD_REQUEST);
            else{
                try {
                    userService.updateUserScore(userId, score);
                    return new ResponseEntity<>(HttpStatus.OK);
                } catch (ScoreOutOfBoundException e) {
                    return new ResponseEntity<>("Final Score should be less than 100",HttpStatus.BAD_REQUEST);
                }
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String userId){
        User user;
        try {
            user = userService.getUser(userId);
            userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
}
