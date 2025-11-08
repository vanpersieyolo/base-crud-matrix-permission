package org.example.basespringbootproject.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basespringbootproject.application.dto.UserDTO;
import org.example.basespringbootproject.application.mapper.BaseMapper;
import org.example.basespringbootproject.application.mapper.UserMapper;
import org.example.basespringbootproject.application.service.IUserService;
import org.example.basespringbootproject.domain.model.User;
import org.example.basespringbootproject.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserDTO, IUserRepository, BaseMapper<User, UserDTO>>
        implements IUserService {

    public UserServiceImpl(IUserRepository repository, UserMapper mapper) {
        super(repository, mapper, User.class);
    }

    @Override
    public UserDTO getByUserName(String username) {
        Optional<User> userOptional = repository.findByUserName(username);
        return userOptional.map(mapper::toDto).orElse(null);
    }
}