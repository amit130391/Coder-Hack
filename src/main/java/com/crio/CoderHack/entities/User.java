package com.crio.CoderHack.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.crio.CoderHack.Exception.ScoreOutOfBoundException;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String userId;

    @NotEmpty(message = "Name is required")
    private String userName;

    @Min(0)
    @Max(100)
    private int score;

    private List<Badges> badge;

    public void addScore(int score) throws ScoreOutOfBoundException {
        this.score += score;
        if(this.score>100){
            throw new ScoreOutOfBoundException("Score should be less than 100");
        }
    }

}
