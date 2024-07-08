package com.crio.CoderHack.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crio.CoderHack.Exception.ScoreOutOfBoundException;
import com.crio.CoderHack.Exception.UserNotFoundException;
import com.crio.CoderHack.controller.UserController;
import com.crio.CoderHack.entities.User;
import com.crio.CoderHack.exchanges.UserRegistrationRequest;
import com.crio.CoderHack.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User("1", "John Doe", 10, new ArrayList<>());
        user2 = new User("2", "Jane Doe", 20, new ArrayList<>());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getallUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].userName").value(user1.getUserName()))
                .andExpect(jsonPath("$[1].userName").value(user2.getUserName()));
    }

    @Test
    public void testGetAllUsers_NoContent() throws Exception {
        when(userService.getallUsers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetUser_UserExists() throws Exception {
        when(userService.getUser("1")).thenReturn(user1);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(user1.getUserName()));
    }

    @Test
    public void testGetUser_UserNotFound() throws Exception {
        when(userService.getUser("1")).thenThrow(new UserNotFoundException("User Not Found with id: 1"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        UserRegistrationRequest userRequest = new UserRegistrationRequest("1", "John Doe");
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value(user1.getUserName()));
    }

    @Test
    public void testUpdateScore() throws Exception {
        when(userService.getUser("1")).thenReturn(user1);
        doNothing().when(userService).updateUserScore("1", 10);

        mockMvc.perform(put("/users/1")
                .param("score", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateScore_UserNotFound() throws Exception {
        when(userService.getUser("1")).thenThrow(new UserNotFoundException("User Not Found with id: 1"));

        mockMvc.perform(put("/users/1")
                .param("score", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateScore_ScoreOutOfBound() throws Exception {
        when(userService.getUser("1")).thenReturn(user1);
        doThrow(new ScoreOutOfBoundException("Final Score should be less than 100")).when(userService).updateUserScore("1", 101);

        mockMvc.perform(put("/users/1")
                .param("score", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Final Score should be less than 100"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.getUser("1")).thenReturn(user1);
        doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        when(userService.getUser("1")).thenThrow(new UserNotFoundException("User Not Found with id: 1"));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}

