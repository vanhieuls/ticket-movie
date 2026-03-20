package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.RoleRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.RoleResponse;
import com.example.english.Service.Interface.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Role Admin Controller", description = "APIs quản lý vai trò (roles) cho admin")
@PreAuthorize("hasRole('ADMIN')")
public class RoleAdminController {
    RoleService roleService;

    @PostMapping
    @Operation(summary = "Create Role", description = "API tạo mới vai trò")
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Create role successfully")
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @PutMapping("/{name}")
    @Operation(summary = "Update Role", description = "API cập nhật vai trò")
    public ApiResponse<RoleResponse> updateRole(@PathVariable String name, @Valid @RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Update role successfully")
                .result(roleService.updateRole(name, roleRequest))
                .build();
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get Role", description = "API lấy thông tin vai trò theo tên")
    public ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Get role successfully")
                .result(roleService.getRole(name))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get All Roles", description = "API lấy tất cả vai trò")
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .message("Get all roles successfully")
                .result(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete Role", description = "API xóa vai trò")
    public ApiResponse<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete role successfully")
                .build();
    }
}

