package com.dreamteam.emotianali.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "t_user")
public class User implements UserDetails {

    @Id
    private Long id;

    @Column
    private String imageLink;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column
    private String userGroup;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String secretQuestion;

    @Column
    private String secretAnswer;

    @Transient
    private String passwordConfirm;

    @Transient
    private String passwordNew;

    @Transient
    private String passwordNewConfirm;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Record> records;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !roles.contains(new Role(0L, "ROLE_BLOCKED"));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<Tone> getTones() {
        Tone anger = new Tone("Anger", (float) 0);
        Tone fear = new Tone("Fear", (float) 0);
        Tone joy = new Tone("Joy", (float) 0);
        Tone sadness = new Tone("Sadness", (float) 0);
        Tone analytical = new Tone("Analytical", (float) 0);
        Tone confident = new Tone("Confident", (float) 0);
        for (Record userRecord : this.getRecords()) {
            for (Tone userTone : userRecord.getTones()) {
                if (userTone.getToneName().equals("Anger")) {
                    anger.addScore(userTone.getScore());
                }
                if (userTone.getToneName().equals("Fear")) {
                    fear.addScore(userTone.getScore());
                }
                if (userTone.getToneName().equals("Joy")) {
                    joy.addScore(userTone.getScore());
                }
                if (userTone.getToneName().equals("Sadness")) {
                    sadness.addScore(userTone.getScore());
                }
                if (userTone.getToneName().equals("Analytical")) {
                    analytical.addScore(userTone.getScore());
                }
                if (userTone.getToneName().equals("Confident")) {
                    confident.addScore(userTone.getScore());
                }
            }
        }
        return Arrays.asList(anger, fear, joy, sadness, analytical, confident);
    }
}
