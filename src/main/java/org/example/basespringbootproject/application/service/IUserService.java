package org.example.basespringbootproject.application.service;

import org.example.basespringbootproject.application.dto.UserDTO;

public interface IUserService extends IBaseService<UserDTO, Long> {
	UserDTO getByUserName(String username);
}