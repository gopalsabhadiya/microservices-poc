package com.gopal.poc.mappers;

import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Thie method maps Entity to DTO
     * @param userEntity
     * @return UserDTO
     */
    UserDTO mapUserEntityToDTO(UserEntity userEntity);

    /**
     * This method maps DTO to ENTITY
     * @param userDTO
     * @return UserEntity
     */
    UserEntity mapUserDTOToEntity(UserDTO userDTO);
}
