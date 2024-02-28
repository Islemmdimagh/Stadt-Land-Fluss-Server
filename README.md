# Categories!
Welcome to our Project *Categories!*

Quick-Access: [Client](https://sopra-fs22-group28-client.herokuapp.com/) - [Server](https://sopra-fs22-group28-server.herokuapp.com/)

## Introduction
Categories (“Stadt-Land-Fluss”) is a fun game which can be played by 2 or more players. There is an amount of rounds and some Categories which can be chosen individually. In each round a new random letter is set and every player has to find words for the specific categories which start with the given letter. As soon as a player submits all his answers for this letter the round will be closed for everyone. For each valid answer you will get a point. For the predefined categories the game will automatically validate each submitted answer. There is also the possibility for each player to vote for an invalid answer of someone else to be counted as valid in case the automatic validation is faulty. Also, the players vote for all answers submitted in custom categories. At the end all the points from each round will be added up and the 3 best players will be displayed.

### Main Goal
Everyone played “Stadt-Land-Fluss” as a child or still does now, with pen and paper and after every round we had to search online if some of those words do exist.
So we created an online version of the game called “Categories!”, where it doesn’t matter in which language you type in a word, it automatically checks for pre-defined categories if it is valid or not and even gives you a wikipedia link to check for yourself.

### Motivation
As a team we wanted to create a game everyone can play in their free time with many friends together. Categories is a game that almost everyone played at least once in a lifetime so it is very popular and it will be played for sure in the future. Our motivation was to combine the idea of a popular game with our challenge of developing it with our own imagination in order to expand our knowledge about software developing and creating a game everyone can profit from.

## Technologies

The Technologies we used in our server repository are the following:

- IntelliJ as our IDE
- Git and GitHub for the project organization and version control
- Heroku for the deoplyment
- Sonarcloud for code quality
- Postman for testing and illustrating endpoints
- Gradle as build manager
- Spring as application framework

## High-level components
Our main components of our backend are the following:

1. The [Lobby](https://github.com/sopra-fs22-group28/sopra-fs22-group28-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/entity/Lobby.java) class, responsible for storing all important information of a lobby in the repository, like for example the LobbyId and the HostToken.
2. The [LobbyService](https://github.com/sopra-fs22-group28/sopra-fs22-group28-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/service/LobbyService.java) class is responsible for managing the lobby which is the base of our project. This means creating the lobby and getting important information from the lobby.
3. The [LobbyController](https://github.com/sopra-fs22-group28/sopra-fs22-group28-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/LobbyController.java) class is responsible for managing all our endpoints, which are important for our connection to the frontend and the execution of our game.
4. The [Validator](https://github.com/sopra-fs22-group28/sopra-fs22-group28-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/utils/Validator.java) class is responsible for validating the answers, connecting us to an external API and is important for the sense of our game.

# Launch and deployment
In the following there are some important steps and prerequisites that you should have heard of in order to join and work on our project. This should help you to get started with our application.

## Getting started with Spring Boot
We highly recommend to get started with Spring Boot if not already done.

-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: http://spring.io/guides/tutorials/bookmarks/

## Setup this Template with your IDE of choice

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)), [Visual Studio Code](https://code.visualstudio.com/) and make sure Java 15 is installed on your system (for Windows-users, please make sure your JAVA_HOME environment variable is set to the correct version of Java).

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions will help you to run it more easily:
-   `pivotal.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`
-   `richardwillis.vscode-gradle`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs22` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
In our project we wrote many different testCases in order to test our functionality and to improve quality of our code. There are Unit-, Integration- and REST interface tests. They will run everytime you build the application, but can also be run individually in the [Tests](https://github.com/sopra-fs22-group28/sopra-fs22-group28-server/tree/master/src/test/java/ch/uzh/ifi/hase/soprafs22) folder.

If you want to get more familiar with writing tests on your own please have a 
look here: https://www.baeldung.com/spring-boot-testing

We highly recommend to use [Sonarcloud](https://sonarcloud.io/) in order to test your code quality.

We highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.

## Roadmap
There are still many improvements possible to our game. Some of them are the following:
- Create a Live-Chat in between the rounds, so the players can interact with each other in order to discuss the answers & the voting.
- Getting extra points for unique and rare answers and/or for superfast answers.
- Make it possible to create and play in teams against each other.

## Authors and acknowledgment
### Authors
- [Timothy-Till Näscher](https://github.com/tnaescher)
- [Witold Rozek](https://github.com/wrozek)
- [Mohamed Islem Mdimagh](https://github.com/Islemmdimagh)

### Acknowledgment
We wanted to thank our TA [Tarek Alakmeh](https://github.com/Taremeh) for helping and bringing us through this very interesting and instructive project!

## License
This project is licensed under the MIT license.

See [LICENSE](https://github.com/sopra-fs22-group28/sopra-fs22-group28-server/blob/master/LICENSE) for more information.






