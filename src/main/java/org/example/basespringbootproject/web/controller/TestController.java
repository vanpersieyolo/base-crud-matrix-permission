package org.example.basespringbootproject.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController - dùng để kiểm tra authentication & authorization
 * cho các trường hợp RBAC + Matrix.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    // ======================
    // PUBLIC (no token required)
    // ======================
    @GetMapping("/public")
    public ResponseEntity<String> publicAccess() {
        return ResponseEntity.ok("✅ Public access: no authentication required.");
    }


    // ======================
    // PROTECTED (login required)
    // ======================
    @GetMapping("/protected")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> protectedAccess() {
        return ResponseEntity.ok("🔐 Authenticated access: valid token required.");
    }

    // ======================
    // ROLE/PERMISSION CHECKS
    // ======================
    @GetMapping("/read")
    @PreAuthorize("hasAuthority('PERM_USER_READ')")
    public ResponseEntity<String> readAccess() {
        return ResponseEntity.ok("✅ Authorized: you have PERM_USER_READ");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PERM_USER_CREATE')")
    public ResponseEntity<String> createAccess() {
        return ResponseEntity.ok("✅ Authorized: you have PERM_USER_CREATE");
    }

    // ======================
    // MATRIX (context-based permission)
    // ======================
    @PutMapping("/project/{contextId}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, 'USER_UPDATE', #contextId)")
    public ResponseEntity<String> updateProjectUser(@PathVariable Long contextId) {
        return ResponseEntity.ok("✅ Context-based authorized: USER_UPDATE in project " + contextId);
    }

    @DeleteMapping("/project/{contextId}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, 'USER_DELETE', #contextId)")
    public ResponseEntity<String> deleteProjectUser(@PathVariable Long contextId) {
        return ResponseEntity.ok("✅ Context-based authorized: USER_DELETE in project " + contextId);
    }
}
