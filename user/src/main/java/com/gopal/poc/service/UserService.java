package com.gopal.poc.service;

import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.entity.UserEntity;
import com.gopal.poc.mappers.UserMapper;
import com.gopal.poc.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    /**
     * This method saves user into DB
     * @param userDTO
     * @return UserDTO
     */
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Trying to register: {}", userDTO.toString());
        UserEntity userEntity = userRepository.saveAndFlush(userMapper.mapUserDTOToEntity(userDTO));
        return userMapper.mapUserEntityToDTO(userEntity);
    }

    /**
     * This method check is user exists in DB by Primary key
     * @param id
     * @return Boolean
     */
    public Boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * This method finds user from DB by id (Primary Key)
     * @param id
     * @return UserDTO
     */
    public UserDTO fetchUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userMapper.mapUserEntityToDTO(userEntity.orElse(null));
    }
}
