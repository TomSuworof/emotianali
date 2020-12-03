package com.dreamteam.emotianali.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "t_tones")
@NoArgsConstructor
public class Tone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String toneName;

    @Column
    private Float score;

    @Transient
    @ManyToMany(mappedBy = "tones")
    private Set<User> users;

    public Tone(String toneName, Float score) {
        this.toneName = toneName;
        this.score = score;
    }

    public void addScore(Float score) {
        this.score += score;
    }
}
