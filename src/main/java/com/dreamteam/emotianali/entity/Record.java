package com.dreamteam.emotianali.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "t_records")
@NoArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Tone> tones;

    public Record(String text, Set<Tone> tones) {
        this.text = text;
        this.tones = tones;
    }
}
