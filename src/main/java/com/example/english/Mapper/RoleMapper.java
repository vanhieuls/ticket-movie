package com.example.english.Mapper;

import com.example.english.Dto.Request.RoleRequest;
import com.example.english.Dto.Response.RoleResponse;
import com.example.english.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest roleRequest);
    RoleResponse toRoleResponse(Role role);
    List<RoleResponse> toRoleResponses(List<Role> roles);
    void updateRoleFromRequest(RoleRequest roleRequest, @MappingTarget Role role);
}

