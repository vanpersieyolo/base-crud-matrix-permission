package org.example.basespringbootproject.domain.repository;

import org.example.basespringbootproject.domain.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);


    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByUserNameAndDeletedFalse(String username);

}