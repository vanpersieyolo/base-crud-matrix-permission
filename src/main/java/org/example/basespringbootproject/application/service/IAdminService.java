package org.example.basespringbootproject.application.service;

import org.example.basespringbootproject.domain.model.Permission;
import org.example.basespringbootproject.domain.model.Role;

public interface IAdminService {
    Role createRole(String code, String name);

    Permission createPermission(String code, String description);

    void assignRoleToUser(Long userId, String roleCode);

}
