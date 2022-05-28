package com.example.backend.service.user;

import com.example.backend.domain.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService {

    UserDto saveUser(UserDto user, String siteUrl) throws UnsupportedEncodingException, MessagingException;

    List<User> getAllUsers();

    UserDto findUser(UserDto user);
}
