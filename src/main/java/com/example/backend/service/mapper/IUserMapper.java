package com.example.backend.service.mapper;


import com.example.backend.domain.User;
import com.example.backend.service.user.UserDto;

import java.util.Collection;
import java.util.Set;

public interface IUserMapper {
    UserDto toService(User entity);

    User toEntity(UserDto dto);

    Set<UserDto> toServiceList(Collection<User> userList);

}
