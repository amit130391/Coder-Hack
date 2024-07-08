package com.crio.CoderHack.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.crio.CoderHack.entities.User;
import com.crio.CoderHack.exchanges.UserRegistrationRequest;
import com.crio.CoderHack.repository.UserRepository;
import com.crio.CoderHack.service.UserService;
import com.crio.CoderHack.service.UserServiceImpl;
import com.crio.CoderHack.Exception.UserNotFoundException;
import com.crio.CoderHack.Exception.ScoreOutOfBoundException;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // Arrange
        UserRegistrationRequest userRequest = new UserRegistrationRequest("1", "John Doe");
        User user = new User("1", "John Doe", 0, new ArrayList<>());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.registerUser(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
                new User("1", "John Doe", 10, new ArrayList<>()),
                new User("2", "Jane Doe", 20, new ArrayList<>())
        );
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getallUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getUserName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUser_UserExists() {
        // Arrange
        User user = new User("1", "John Doe", 10, new ArrayList<>());
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUser("1");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getUserName());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    public void testGetUser_UserDoesNotExist() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUser("1");
        });
        assertEquals("User Not Found with id: 1", exception.getMessage());
    }

    @Test
    public void testUpdateUserScore_UserExists() throws ScoreOutOfBoundException {
        // Arrange
        User user = new User("1", "John Doe", 10, new ArrayList<>());
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Act
        userService.updateUserScore("1", 20);

        // Assert
        assertEquals(30, user.getScore());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUserScore_ExceedsMaxScore() throws ScoreOutOfBoundException {
        // Arrange
        User user = new User("1", "John Doe", 90, new ArrayList<>());
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Act and Assert
        ScoreOutOfBoundException exception = assertThrows(ScoreOutOfBoundException.class, () -> {
            userService.updateUserScore("1", 20);
        });
        assertEquals("Score should be less than 100", exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        doNothing().when(userRepository).deleteById("1");

        // Act
        userService.deleteUser("1");

        // Assert
        verify(userRepository, times(1)).deleteById("1");
    }
}

