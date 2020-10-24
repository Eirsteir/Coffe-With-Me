# Coffee With Me 
*This application was developed for learning purposes 
and some architectural decisions could and probably should otherwise 
have been taken differently.*  

# Table of Contents
1. [Application](#application)
2. [Running the application](#running-the-application)
    * [Running the client locally](#running-the-server-locally)
    * [Running the server locally](#running-the-client-locally)
    * [Running the application in Docker](#running-the-application-in-docker)
4. [Application structure](#application-structure)
4. [Technologies](#technologies)
5. [API Documentation](#api-documentation)

## Application
Keywords:
- Microservices
- Saga Pattern
- Event-driven
- Spring
- MySQL

The application consists of the following components:
- Discovery Server
- API Gateway  
- Auth Service 
- Social Service
- Notification Service 
- Documentation Service: API documentation

## Running the application 
To run the application, run eureka server and gateway first, then the three services.

The easiest approach is to build the services first:
 
 ```
mvn clean install -DskipTests
```

And then launch them with docker-compose:

```
docker-compose up --build 
``` 

The application will be available at `http://localhost:3000`.
 
This will also launch PHPMyAdmin at `http://localhost:8181`
 

### Running the server locally
In order to run the server locally you will first need to build it. 
To build and package a Spring Boot app into a single executable Jar file with Maven, you can use the command below. 
You will need to run it from the root project folder which contains the pom.xml file.

```
mvn package -DskipTests
```
or you can use

```
mvn install -DskipTests
```

To run the Spring Boot app from a terminal you can you the java -jar command. 
The executable jar files can then be run individually with
```
java -jar gateway\target\gateway-0.0.1-SNAPSHOT.jar
java -jar eureka-server\target\discovery-0.0.1-SNAPSHOT.jar
java -jar social-service\target\social-service-0.0.1-SNAPSHOT.jar
java -jar notification-service\target\notification-service-0.0.1-SNAPSHOT.jar
java -jar auth-service\target\auth-service-0.0.1-SNAPSHOT.jar
```

You can also use the Spring Boot Maven plugin to run the app. 
Then you can use the snippet below to run the application:

```
mvn spring-boot:run
```

#### IDEA
You can follow any/all of the above commands, or simply use the run configuration provided by your favorite IDE and
run/debug the application from there for development purposes.


### Local development
For local development it is recommended to start eventuate-tram, the message broker and RDBMS first by running

```
docker-compose -f docker-compose-dev.yml
```

And then starting the applications manually.

Once the application is running, the services can be accessed over the following base-path:

http://localhost:8080/api/

Some of the important API endpoints are as follows:

- http://localhost:8080/api/auth/login (HTTP:POST)

    Logs user in by receiving a JWT auth token. 
    Required POST data: 
    
    {
        "email": "email",
        "password": "password"
    } 
- http://localhost:8080/api/auth/register (HTTP:POST)
    
    Register a new user.

- http://localhost:8080/api/social/user/{id} (HTTP:GET)

    Retrieve the user with the given id.

- http://localhost:8080/api/social/friends (HTTP:GET,PUT, DELETE)

    List friendships belonging to the current user.
    Update a friendship, for instance accept a friend request.
    Delete a friendship.

- http://localhost:8080/api/social/friends?to_friend={id} (HTTP:POST)

    Send a friend request.

- http://localhost:8080/api/notifications (HTTP:GET,POST,PUT)

    List notifications belonging to the current user.

- http://localhost:8080/api/docs/swagger-ui.html (HTTP:GET)

    Swagger API documentation
    
  
## Application Structure
The application is built as several microservices with an API Gateway to proxy incoming requests
to the appropriate service using Spring Cloud Netflix Zuul and Eureka Server Registration and Discovery.
They communicate internally with the help of choreography-based sagas for distributed data management
with messaging through Apache Kafka.  

## Technologies
The following libraries, among others, were used during the development of this application:

- **Spring Boot** - Server side framework
- **Docker** - Containerizing framework
- **MySQL** - RDBMS
- **Apache Kafka**  - Distributed streaming platform
- **Eventuate Tram** - Framework for distributed data management  
- **Spring Cloud Netflix Zuul** - JVM based router and server side load balancer
- **Spring Cloud Netflix Eureka** - Client side service discovery
- **JWT** - Authentication mechanism for REST APIs

## API Documentation
API documentation via Swagger can be found at http://localhost:8080/api/docs/swagger-ui.html