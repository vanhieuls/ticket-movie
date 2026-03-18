package com.example.english.Controller.Admin;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Entity.User;
import com.example.english.Service.Implement.UserSpecification;
import com.example.english.Service.Interface.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Admin Controller", description = "APIs quản lý tài khoản người dùng")
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

}
