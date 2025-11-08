package org.example.basespringbootproject.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.basespringbootproject.application.service.IAdminService;
import org.example.basespringbootproject.domain.model.Permission;
import org.example.basespringbootproject.domain.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IAdminService adminService;

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERM_ROLE_MANAGE')")
    public ResponseEntity<Role> createRole(@RequestParam String code, @RequestParam String name) {
        return ResponseEntity.ok(adminService.createRole(code, name));
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERM_PERMISSION_MANAGE')")
    public ResponseEntity<Permission> createPermission(@RequestParam String code, @RequestParam String desc) {
        return ResponseEntity.ok(adminService.createPermission(code, desc));
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERM_ROLE_MANAGE')")
    public ResponseEntity<Void> assignRole(@PathVariable Long userId, @RequestParam String roleCode) {
        adminService.assignRoleToUser(userId, roleCode);
        return ResponseEntity.ok().build();
    }

    //get roles and permissions
    @GetMapping("/roles-permissions")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getRolesAndPermissions() {
        // This is a placeholder implementation. Replace with actual logic to fetch roles and permissions.
        String rolesAndPermissions = "List of roles and permissions";
        return ResponseEntity.ok(rolesAndPermissions);
    }
}
