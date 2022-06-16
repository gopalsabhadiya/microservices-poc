package com.gopal.poc.controller;

import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * This HTTP method creates user. User can be System or Regular
     * @param userDTO
     * @return UserDTO
     */
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    /**
     * This HTTP method checks if user exists by id (Primary key)
     * @param id
     * @return Boolean
     */
    @GetMapping("/exists/{id}")
    public Boolean userExists(@PathVariable Long id) {
        return userService.userExists(id);
    }

    /**
     * This method return user with id (Primary key)
     * @param id
     * @return UserDTO
     */
    @GetMapping("/{id}")
    public UserDTO fetchUser(@PathVariable Long id) {
        return userService.fetchUserById(id);
    }


}
