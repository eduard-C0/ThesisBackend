package com.example.backend.service.mapper;


import com.example.backend.domain.User;
import com.example.backend.service.user.UserDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImplementation implements IUserMapper {
    @Override
    public UserDto toService(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

    @Override
    public User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    @Override
    public Set<UserDto> toServiceList(Collection<User> userList) {
        return userList
                .stream()
                .map(this::toService)
                .collect(Collectors.toSet());
    }
}
