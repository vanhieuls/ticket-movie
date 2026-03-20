package com.example.english.Service.Implement;

import com.example.english.Dto.Request.RoleRequest;
import com.example.english.Dto.Response.RoleResponse;
import com.example.english.Entity.Role;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.RoleMapper;
import com.example.english.Repository.RoleRepository;
import com.example.english.Service.Interface.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsById(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(roleRequest);
        try {
            Role saved = roleRepository.save(role);
            return roleMapper.toRoleResponse(saved);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public RoleResponse updateRole(String name, RoleRequest roleRequest) {
        Role role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        
        roleMapper.updateRoleFromRequest(roleRequest, role);
        try {
            Role saved = roleRepository.save(role);
            return roleMapper.toRoleResponse(saved);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public RoleResponse getRole(String name) {
        Role role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toRoleResponses(roles);
    }

    @Override
    public void deleteRole(String name) {
        Role role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        try {
            roleRepository.delete(role);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }
}

