package org.example.basespringbootproject.domain.repository;

import org.example.basespringbootproject.domain.model.UserContextPermission;
import org.springframework.data.jpa.repository.Query;

public interface IUserContextPermissionRepository extends BaseRepository<UserContextPermission, Long> {

    @Query("""
        select case when count(ucp) > 0 then true else false end
        from UserContextPermission ucp
        join ucp.permission p
        where ucp.user.id = :userId and ucp.context.id = :contextId and p.code = :permissionCode
    """)
    boolean existsByUserIdAndContextIdAndPermissionCode(Long userId, Long contextId, String permissionCode);

}
