package com.dreamteam.emotianali.entity;

import lombok.*;

@Data
@AllArgsConstructor
public class InstagramPost {
    long id;
    String caption;

    @Override
    public String toString() {
        return id + ": " + caption;
    }
}
