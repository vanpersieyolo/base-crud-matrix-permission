package org.example.basespringbootproject.infrastructure.security.permission;

import lombok.RequiredArgsConstructor;
import org.example.basespringbootproject.domain.repository.IUserContextPermissionRepository;
import org.example.basespringbootproject.domain.repository.IUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator {

    private final IUserRepository userRepository;
    private final IUserContextPermissionRepository ucpRepository;

    /**
     * Check if authentication user has given permission code in contextId.
     * permissionCode expected without PERM_ prefix; adapt if you store with/without prefix.
     */
    public boolean hasPermission(Authentication authentication, String permissionCode, Long contextId) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        String username = authentication.getName();

        return userRepository.findByUserNameAndDeletedFalse(username)
                .map(user -> ucpRepository.existsByUserIdAndContextIdAndPermissionCode(
                        user.getId(), contextId, permissionCode))
                .orElse(false);
    }
}
