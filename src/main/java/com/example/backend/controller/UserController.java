package com.example.backend.controller;

import com.example.backend.config.JwtTokenService;
import com.example.backend.domain.User;
import com.example.backend.service.mapper.UserMapperImplementation;
import com.example.backend.service.user.UserDto;
import com.example.backend.service.user.UserServiceImplementation;
import com.example.backend.utils.enums.AppRoles;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserMapperImplementation userMapper;
    private final UserServiceImplementation userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping({"/createUser"})
    public ResponseEntity<ResponseMessage> addUser(@RequestBody UserDto user, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        ResponseMessage responseMessage = new ResponseMessage("", "");
        System.out.println("here");
        UserDto savedUser = userService.saveUser(user, getSiteURL(request));
        if (savedUser == null) {
            System.out.println("Existing user!");
            responseMessage.setMessage("Existing user!");
            responseMessage.setCode("400");
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("Account created successfully");
        responseMessage.setMessage("Account created successfully");
        responseMessage.setCode("200");
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = "text/plain")
    @SneakyThrows
    public ResponseEntity<String> login(@RequestBody UserDto userPasswordDto) {

        UserDto user = userService.findUser(userPasswordDto);
        String jwt;
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        if (Objects.isNull(user))
            return ResponseEntity.internalServerError().body("Incorrect email!");
        else {
            if (Objects.isNull(user.getPassword()))
                return ResponseEntity.internalServerError().body("Incorrect password!");
            else
                jwt = jwtTokenService.createJwtToken(user, Collections.singleton(AppRoles.valueOf("USER")));
            return ResponseEntity.ok(jwt);
        }
    }

    @GetMapping({"/getUser"})
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token) {
        User user = jwtTokenService.getUserFromToken(token);
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        UserDto userDto = userMapper.toService(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
