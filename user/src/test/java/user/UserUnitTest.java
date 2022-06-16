package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.entity.UserEntity;
import com.gopal.poc.mappers.UserMapper;
import com.gopal.poc.repository.UserRepository;
import com.gopal.poc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class UserUnitTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserMapper userMapper;

    @SpyBean
    UserService userService;

    private UserDTO userDTO;
    private UserDTO userDTOFull;
    private UserEntity userEntity;
    private UserEntity userEntity2;

    @BeforeEach
    void setup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File jsonFile = new ClassPathResource("/json/user-dto.json").getFile();
        userDTO = objectMapper.readValue(jsonFile, UserDTO.class);
        jsonFile = new ClassPathResource("/json/user-dto-full.json").getFile();
        userDTOFull = objectMapper.readValue(jsonFile, UserDTO.class);
        jsonFile = new ClassPathResource("/json/user-entity.json").getFile();
        userEntity = objectMapper.readValue(jsonFile, UserEntity.class);
        jsonFile = new ClassPathResource("/json/user-entity-2.json").getFile();
        userEntity2 = objectMapper.readValue(jsonFile, UserEntity.class);
    }

    @Test
    @DisplayName("Save User Test")
    public void saveUserTest() {
        Mockito.lenient().when(userMapper.mapUserDTOToEntity(Mockito.any(UserDTO.class))).thenReturn(userEntity);
        Mockito.lenient().when(userRepository.saveAndFlush(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        Mockito.lenient().when(userMapper.mapUserEntityToDTO(Mockito.any(UserEntity.class))).thenReturn(userDTOFull);
        UserDTO userDTOFromService = userService.saveUser(userDTO);
        Assertions.assertEquals(1, userDTOFromService.getId());
        Assertions.assertEquals("Gopal", userDTOFromService.getUserName());
    }

    @Test
    @DisplayName("Fetch User By ID Test")
    public void fetchUserByIdTest() {
        Mockito.lenient().when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(userEntity));
        Mockito.lenient().when(userMapper.mapUserEntityToDTO(Mockito.any(UserEntity.class))).thenReturn(userDTOFull);
        UserDTO userDTOFromService = userService.fetchUserById(1L);
        Assertions.assertEquals(1, userDTOFromService.getId());
        Assertions.assertEquals("Gopal", userDTOFromService.getUserName());
    }

}

