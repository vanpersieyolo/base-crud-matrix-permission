package org.example.basespringbootproject.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basespringbootproject.application.service.IAdminService;
import org.example.basespringbootproject.domain.model.Permission;
import org.example.basespringbootproject.domain.model.Role;
import org.example.basespringbootproject.domain.model.User;
import org.example.basespringbootproject.domain.repository.IPermissionRepository;
import org.example.basespringbootproject.domain.repository.IRoleRepository;
import org.example.basespringbootproject.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements IAdminService {
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final IUserRepository userRepository;

    @Override
    public Role createRole(String code, String name) {
        if (roleRepository.existsByCode(code)) throw new IllegalArgumentException("Role exists");
        Role r = new Role();
        r.setCode(code);
        r.setName(name);
        return roleRepository.save(r);
    }
    @Override
    public Permission createPermission(String code, String description) {
        if (permissionRepository.existsByCode(code)) throw new IllegalArgumentException("Permission exists");
        Permission p = new Permission();
        p.setCode(code);
        p.setDescription(description);
        return permissionRepository.save(p);
    }

    @Override
    public void assignRoleToUser(Long userId, String roleCode) {
        User u = userRepository.findById(userId).orElseThrow();
        Role r = roleRepository.findByCode(roleCode).orElseThrow();
        Set<Role> roles = u.getRoles() == null ? new HashSet<>() : new HashSet<>(u.getRoles());
        roles.add(r);
        u.setRoles(roles);
        userRepository.save(u);
    }
}
