# microservices-poc
Microservices POC With Kafka and Service Registry

Project dependencies:
1. Docker
2. Java 11
3. maven


Below are the given steps to start the project:
1. Open terminal in project base folder `microservices-poc`
2. Run command: `mvn clean install`
   1. This command build the project and runs unit test cases automatically.
3. Run `docker compose up -d` and wait for a minute after this command finishes to give containers some time to start everything
4. Project has 3 sub-projects
   1. eureka-server
   2. user
   3. notification
5. Open terminal in `eureka-server` and run below given command
   1. >mvn -pl com.gopal.app:eureka-server -am org.springframework.boot:spring-boot-maven-plugin:run
6. Let `eureka-server` start and then open new terminal in `user` and run below given command
   1. >mvn -pl com.gopal.app:user -am org.springframework.boot:spring-boot-maven-plugin:run
7. Let `user`start and then open new terminal in `notification` and run below given command
   1. >mvn -pl com.gopal.app:notification -am org.springframework.boot:spring-boot-maven-plugin:run

In order to get live notification:
Follow below given steps (after all the servers has started) to get live notification whenever new notification is created:
1. Open new tab in browser and enter url: `http://localhost:8082/api/v1/notification/live` and let the window finish loading (It'll give error but no worries)
2. Open javascript console in the debug tools and enter below given scripts to get notifications logged in console:
   1. >const eventSource = new EventSource("http://localhost:8082/api/v1/notification/live");
   2. >eventSource.onmessage = function(e) {console.log(e.data)}


Below given are some useful links to know more about the development:
Eureka Server: http://localhost:8761/
User Server Documentation: http://localhost:8081/api/v1/swagger-ui/index.html#/
Notification Server Documentation: http://localhost:8082/api/v1/swagger-ui/index.html#/

Let me know in case anything doesn't work.
