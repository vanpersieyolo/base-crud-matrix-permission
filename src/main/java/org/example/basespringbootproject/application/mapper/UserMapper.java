package org.example.basespringbootproject.application.mapper;

import org.example.basespringbootproject.application.dto.UserDTO;
import org.example.basespringbootproject.domain.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> {

    @Override
    UserDTO toDto(User entity);

    @Override
    User toEntity(UserDTO dto);

    @Override
    List<UserDTO> toDtoList(List<User> entities);

    @Override
    List<User> toEntityList(List<UserDTO> dtos);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);
}