package com.gopal.poc.clients.user;

import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.util.RestAPIConstants;
import com.netflix.discovery.EurekaClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class UserClientService {
    private final EurekaClient discoveryClient;

    private final RestTemplate restTemplate;

    /**
     * This method checks whether user exists in DB by ID (Primary Key)
     * @param id
     * @return Long
     */
    public Boolean userExists(Long id) {

        log.debug("Trying to find USER client");
        String clientBaseUrl =
                discoveryClient.getNextServerFromEureka(RestAPIConstants.EUREKA_USER_SERVER_NAME, false)
                        .getHomePageUrl();
        log.debug("Found USER Client: {}", clientBaseUrl);

        return restTemplate.getForEntity(
                clientBaseUrl + RestAPIConstants.USER_EXISTS_URL + id.toString(), Boolean.class
        ).getBody();
    }

    /**
     * This method gives user by ID
     * @param id
     * @return UserDTO
     */
    public UserDTO fetchUserById(Long id) {

        log.debug("Trying to find USER client");
        String clientBaseUrl =
                discoveryClient.getNextServerFromEureka(RestAPIConstants.EUREKA_USER_SERVER_NAME, false)
                        .getHomePageUrl();
        log.debug("Found USER Client: {}", clientBaseUrl);

        return restTemplate
                .getForEntity(clientBaseUrl + RestAPIConstants.USER_FETCH_URL + id.toString(), UserDTO.class)
                .getBody();
    }
}
