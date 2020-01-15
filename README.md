# API of a game of n-stone Kalah.

This is a Java 8 project of a game of n-stones Kalah. It uses:

* [Spring Boot](https://spring.io/projects/spring-boot) 

* [Springfox](http://springfox.github.io/springfox/)

* [JUnit](https://junit.org/junit5), [Mockito](https://site.mockito.org/) and [Jacoco](https://www.eclemma.org/jacoco/)

## About the game

[Kalah][https://en.wikipedia.org/wiki/Kalah] is a two players board game where each player has 6 small pits, called houses, on each side; and a big pit, called kalah. The objective of the game is to capture more stones than the opponent.

The pits in the board are numbered from 1 to 14 where 7 and 14 are the kalah. The northern player owns the pits 1 to 7 and the southern the pits 8 to 14. The board is prepared as follows:

                     Northern Player pits
     <- (06) <- (05) <- (04) <- (03) <- (02) <- (01) <-
     |                                               / \
    \ /                                               |
    (07) << Northern Kalah         Southern Kalah >> (14)
     |                                               / \
    \ /                                               |
     -> (08) -> (09) -> (10) -> (11) -> (12) -> (13) ->
                     Southern Player pits

At the start of the game, n stones are put in each pit.

The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, 
one in each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last
stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
player's turn.

When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.

The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

## Running with Maven

To run using maven just run:


    mvn clean package spring-boot:run

The default number of stones is 6. You can change it in the properties at application.yml file or you can run the application using the follow command:


    clean package spring-boot:run -Dspring-boot.run.arguments=--application.amountOfStones=3  

## Running with Docker

* To generate the image from docker via maven just run:


    mvn clean package docker:removeImage docker:build 
	
* To execute the application:


    docker-compose [-f src/main/docker/docker-compose.yml] down && docker-compose [-f src/main/docker/docker-compose.yml] up -d
	
* Viewing log:


    docker logs -f kalah

    
## See API methods

To see the API methods, run the application and go to:

    http://localhost:8080/swagger-ui.html
	
## Structure
### main/java
#### config
Project Spring context settings.

#### domain
Entities of the project. 

#### handler
Interface of the handler present in the project.

* It should end with the expression Handler. 

#### handler.impl
Concrete implementation of the handler present in the project.

* It process requests.

* It should be annotated with @ Service.

* It should end with the expression HandlerImpl.

#### rest
REST Services Interfaces

* Only DTOs should be exported.

* It should end with the expression RestService.

#### rest.dto
Data Transfer Objects using to moving the backend data to the client.

#### rest.impl
REST Services Implementation

* It should be annotated with @ RestController and @ RequestMapping ("/").

* It should end with the expression RestServiceImpl.

#### service
Interface of the services present in the project.

* It should end with the expression Service. 

#### service.impl
Concrete implementation of the services present in the project.

* Manipulates DTOs and transforms them into the corresponding entity.

* It should be annotated with @ Service.

* It should end with the expression ServiceImpl.

### main/resources
Project Resources.

#### config
Spring configuration files.

* application.yml: Project Settings.

### test/java
#### domain, handler, rest e service
Test for the REST webservice and Services in this project.

### test/resources
Configuration to execute the tests in this project.

## Future improvements

* Create an authentication system (using [Spring Security](https://spring.io/projects/spring-security) and [JWT](https://jwt.io/))

* Include security in the requests (using SSL/TLS)

* Introduce a persistence layer to register the past games (using [Spring Data](https://spring.io/projects/spring-data))

