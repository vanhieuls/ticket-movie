package com.example.english.Mapper;

import com.example.english.Dto.Request.UserRequest;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest user);
    UserResponse toResponse(User user);
    void updateUserFromDto(UserRequest userRequest, @MappingTarget User user);
}
