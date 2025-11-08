package org.example.basespringbootproject.domain.repository;

import org.example.basespringbootproject.domain.model.Permission;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IPermissionRepository extends BaseRepository<Permission, Long> {
    Optional<Permission> findByCode(String code);

    @Query("""
                                select case when count(p) > 0 then true else false end
                                from Permission p
                                where p.code = :code
            """)
    boolean existsByCode(String code);
}
