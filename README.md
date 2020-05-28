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

## Application
Keywords:
- Microservices
- Saga Pattern
- Event-driven
- Spring
- React.js

The application consists of the following components:
- Client: Frontend
- Discovery Server:
- API Gateway:  
- Auth Service: 
- Social Service: 
- Notification Service: 

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
 
### Running the client locally
The client can be run with npm by entering the snippet below:

```
npm start
```
The application is then available at `http://localhost:3000`

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

Once the application is running, the services can be accessed over the following base-path:

http://localhost:8080/api/

Some of the important API endpoints are as follows:

- http://localhost:8080/api/auth (HTTP:POST)
- http://localhost:8080/api/auth/register (HTTP:POST)
- http://localhost:8080/api/social/user/{id} (HTTP:GET)
- http://localhost:8080/api/social/friends (HTTP:GET,POST,PUT)
- http://localhost:8080/api/social/friends?to_friend (HTTP:POST)
- http://localhost:8080/api/notifications (HTTP:GET,POST,PUT)

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
- **React.js** - Library for building user interfaces
- **Material UI** - UI design
