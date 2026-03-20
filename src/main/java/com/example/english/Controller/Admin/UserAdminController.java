package com.example.english.Controller.Admin;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Entity.User;
import com.example.english.Service.Implement.UserSpecification;
import com.example.english.Service.Interface.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Admin Controller", description = "APIs quản lý tài khoản người dùng")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {
    UserService userService;

    @GetMapping("/get-list")
    public ApiResponse<Page<UserResponse>> getAllUsersAdmin(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,@RequestParam(required = false, defaultValue = "10") Integer pageSize,@RequestParam(required = false, defaultValue = "id") String sortBy,
                                                            @RequestParam(required = false, defaultValue = "desc") String sortDir,@RequestParam(required = false) String username,@RequestParam(required = false) String fullName,@RequestParam(required = false) String email) {
        Page<UserResponse> userPage = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir, username, fullName, email);
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("User list retrieved")
                .result(userPage)
                .build();
    }

    @PostMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Assign Role to User", description = "API gán vai trò cho người dùng")
    public ApiResponse<UserResponse> assignRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Role assigned to user successfully")
                .result(userService.assignRoleToUser(userId, roleName))
                .build();
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Remove Role from User", description = "API xóa vai trò khỏi người dùng")
    public ApiResponse<UserResponse> removeRoleFromUser(@PathVariable Long userId, @PathVariable String roleName) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Role removed from user successfully")
                .result(userService.removeRoleFromUser(userId, roleName))
                .build();
    }

}
