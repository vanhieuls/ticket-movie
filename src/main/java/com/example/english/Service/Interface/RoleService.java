package com.example.english.Service.Interface;

import com.example.english.Dto.Request.RoleRequest;
import com.example.english.Dto.Response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(String name, RoleRequest roleRequest);
    RoleResponse getRole(String name);
    List<RoleResponse> getAllRoles();
    void deleteRole(String name);
}

