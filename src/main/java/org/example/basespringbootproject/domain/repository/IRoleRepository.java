package org.example.basespringbootproject.domain.repository;

import org.example.basespringbootproject.domain.model.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRoleRepository extends BaseRepository<Role, Long> {
    Optional<Role> findByCode(String code);

    @Query("""
                    select case when count(r) > 0 then true else false end
                    from Role r
                    where r.code = :code
            """)
    boolean existsByCode(String code);
}
