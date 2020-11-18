package com.dreamteam.emotianali.controller;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ErrorController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String return404() {
        return "../public/error/404";
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String return403() {
        return "../public/error/403";
    }
}
