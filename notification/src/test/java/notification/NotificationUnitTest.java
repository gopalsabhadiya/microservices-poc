package notification;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopal.poc.clients.user.UserClientService;
import com.gopal.poc.dto.NotificationDTO;
import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.entity.NotificationEntity;
import com.gopal.poc.mappers.NotificationMapper;
import com.gopal.poc.repository.NotificationRepository;
import com.gopal.poc.service.NotificationService;
import com.gopal.poc.util.KafkaProducer;
import com.gopal.poc.util.exception.PageNumberPageSizeException;
import com.gopal.poc.util.exception.UserNotFoundException;
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
import org.springframework.data.domain.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.util.List;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class NotificationUnitTest {

    @SpyBean
    NotificationService notificationService;

    @MockBean
    NotificationRepository notificationRepository;

    @MockBean
    NotificationMapper notificationMapper;

    @MockBean
    UserClientService userClientService;

    @MockBean
    KafkaProducer kafkaProducer;

    private NotificationDTO notificationDTO;

    private NotificationDTO notificationDTOFull;

    private NotificationEntity notificationEntity;

    private UserDTO userDTO;

    private List<NotificationEntity> notificationEntityList;

    @BeforeEach
    void setup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File jsonFile = new ClassPathResource("/json/notification-dto.json").getFile();
        notificationDTO = objectMapper.readValue(jsonFile, NotificationDTO.class);
        jsonFile = new ClassPathResource("/json/notification-entity.json").getFile();
        notificationEntity = objectMapper.readValue(jsonFile, NotificationEntity.class);
        jsonFile = new ClassPathResource("/json/user-dto.json").getFile();
        userDTO = objectMapper.readValue(jsonFile, UserDTO.class);
        jsonFile = new ClassPathResource("/json/notification-dto-full.json").getFile();
        notificationDTOFull = objectMapper.readValue(jsonFile, NotificationDTO.class);
        jsonFile = new ClassPathResource("/json/notification-entity-list.json").getFile();
        notificationEntityList = objectMapper.readValue(jsonFile, new TypeReference<List<NotificationEntity>>() {});

    }

    @Test
    @DisplayName("Add notification test")
    public void addNotificationTest() {
        try {
            Mockito.lenient().when(userClientService.userExists(1L)).thenReturn(true);
            Mockito.lenient().when(userClientService.userExists(2L)).thenReturn(true);
            Mockito.lenient().when(userClientService.fetchUserById(1L)).thenReturn(userDTO);
            Mockito.lenient().when(notificationMapper.mapNotificationEntityToDTO(notificationEntity)).thenReturn(notificationDTOFull);
            Mockito.lenient().when(notificationMapper.mapNotificationDTOToEntity(notificationDTO)).thenReturn(notificationEntity);
            Mockito.lenient().when(notificationRepository.saveAndFlush(notificationEntity)).thenReturn(notificationEntity);


            notificationDTO = notificationService.addNotification(notificationDTO);

            Assertions.assertEquals(notificationDTO.getId(), notificationEntity.getId());
            Assertions.assertEquals("System", notificationEntity.getUserType().getUserType());
            Assertions.assertEquals(false, notificationEntity.getIsRead());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Add Notification User Not Found exception")
    public void addNotificationUserNotFoundTest() {
        Mockito.lenient().when(userClientService.userExists(1L)).thenReturn(true);
        Mockito.lenient().when(userClientService.userExists(2L)).thenReturn(false);
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> notificationService.addNotification(notificationDTO));
        Assertions.assertEquals("Either toUser or fromUser not exist", userNotFoundException.getReason());
    }

    @Test
    @DisplayName(("Fetch Notification Page Number and Size exception test"))
    public void fetchNotificationExceptionTest() {
        PageNumberPageSizeException pageNumberPageSizeException = Assertions.assertThrows(PageNumberPageSizeException.class, () -> notificationService.fetchNotificationList(1L, 1L, false, 1, null));
        Assertions.assertEquals("Page Number and Page Size are mutually dependent. Please provide both or none", pageNumberPageSizeException.getReason());
    }

    @Test
    @DisplayName("Fetch Notification List Test")
    public void fetchNotificationListTest() throws PageNumberPageSizeException {

        Page<NotificationEntity> notificationEntityPage = new PageImpl<>(notificationEntityList);

        Mockito.lenient().when(notificationRepository.findAll(
                Mockito.any(Example.class), Mockito.any(PageRequest.class)
            )).thenReturn(notificationEntityPage);
        Mockito.lenient().when(notificationMapper.mapNotificationEntityToDTO(Mockito.any(NotificationEntity.class))).thenReturn(notificationDTOFull);

        List<NotificationDTO> notificationDTOList = notificationService.fetchNotificationList(1L, 1L, false, 1, 1);

        Assertions.assertEquals(6, notificationDTOList.size());
        Assertions.assertTrue( notificationDTOList.get(0).getCreatedAt().getTime() >= notificationDTOList.get(1).getCreatedAt().getTime());

    }

    @Test
    @DisplayName("Fetch Notification List Test")
    public void fetchNotificationListNoPageNumberAndSizeTest() throws PageNumberPageSizeException {

        Mockito.lenient().when(notificationRepository.findAll(
                Mockito.any(Example.class), Mockito.any(Sort.class)
        )).thenReturn(notificationEntityList);
        Mockito.lenient().when(notificationMapper.mapNotificationEntityToDTO(Mockito.any(NotificationEntity.class))).thenReturn(notificationDTOFull);

        List<NotificationDTO> notificationDTOList = notificationService.fetchNotificationList(1L, 1L, false, null, null);

        Assertions.assertEquals(6, notificationDTOList.size());
        Assertions.assertTrue( notificationDTOList.get(0).getCreatedAt().getTime() >= notificationDTOList.get(1).getCreatedAt().getTime());

    }

}
