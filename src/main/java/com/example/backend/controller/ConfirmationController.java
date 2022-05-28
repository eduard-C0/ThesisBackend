package com.example.backend.controller;

import com.example.backend.service.user.UserServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class ConfirmationController {

    private final UserServiceImplementation userService;

    @GetMapping(path = "/verify")
    public String verifyUser(@RequestParam("code") String code) {
        return userService.verify(code);
    }
}
