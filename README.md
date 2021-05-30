# SoPra FS21 - Scotland Yard Zürich Edition - Server

## Introduction
Hi there, we are five students from the University of Zurich, who developed this application for the Software Engineering Lab FS21. This is the server side implementation of the project. The aim of this project was to create an online version of the popular board game "Scotland Yard". The main feature of the project was to transfer the game settings from London (where the game is originally based) to Zurich. All the stations and lines displayed in the game are from the real public transport system of Zurich.

Users can register and login to their profile on the [website](https://sopra-fs21-group-08-client.herokuapp.com/), where they see their player statistics and other online users. From there, the users can create or join public lobbies to play together with their friends. After a game has started, the players get assigned a role (detective or Mister X) and they are randomly positioned on the map. The goal is then for the detectives to catch Mister X using the public transport system of Zurich. To enhance the user experience, the players can chat with each other during a game and replay after the game is over. Additionally, after each game the score gets added to the players' user statistics.

## Technologies used
- [Spring Boot](https://spring.io/projects/spring-boot) Application
- [JPA](https://www.oracle.com/java/technologies/persistence-jsp.html) and [Hibernate](https://hibernate.org) for the database management

Connection to the [frontend](https://github.com/sopra-fs21-group-08/sopra-fs21-group08-client/) works through representational state transfer.

## Main Components


###[The Game](https://github.com/sopra-fs21-group-08/sopra-fs21-group08-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/GameEntities/Game.java)
As the center nervous system of the program, the `Game` class holds all the helper classes and 
manipulates them according to the needs. The static fields of the class define how the game runs dynamically. 
While not being a very complicated implementation it holds a lot of logic, such as the game termination and how the rounds and turns are handled.
The main helperclasses include: 
- The `Round` takes care of the behavior of each round, when MrX is visible, and keeps track of what round the game is currently in. Additionally, it collects all the Moves that have been made during the game.
- The `Move` is a small and very encapsulated piece of code. Its sole purpose is to capture a move, and it does exactly that.
- The `PlayerGroup` holds all the `Player`'s and knows whos turn it is.
- The `RoundHistory` holds all the past `Round`'s and could be used to reconstruct entire games from the start.
  Think of it like the Chess notation of our game.

Each helper class is iterable. 

After a Game is terminated, the `Game` Instance gets deleted and we "memorialize" it through a `GameSummary` class. 
This helps us keep the database clean.

###[The Lobby](https://github.com/sopra-fs21-group-08/sopra-fs21-group08-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entity/Lobby.java)
The lobby is the center of our community, players join and leave them to play games and to talk to eachother in chat.
Only Through the Lobby it is possible to create a game, and only if enough users have joined in on the fun.

Replaying is also only possible through the lobbies. The `LobbyConnector` is a detached class that connects one Lobby with the next one, without either lobby knowing about it.

When a `Lobby` is empty it gets deleted, the associated `Game` and `LobbyConnector` are also deleted. This was a challenge to implement.

###[Stations](https://github.com/sopra-fs21-group-08/sopra-fs21-group08-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/network/Station.java)

If the `Game` is our center nervous system, the Stations, or the Network, would be the circulatory system. 
You travel the city of Zurich as either the clever Detectives or the deceptive MrX. The Stations hold information on what moves are possible with what tickets.

We used public transportation data provided by the Kanton of Zürich, and preprocessed them with python scripts to meet our needs.
this proved to be one of the big challenges of this project.

## Launch and Deployment

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

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs21` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
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

The application is deployed on [heroku](https://sopra-fs21-group-08-client.herokuapp.com/)

## Roadmap

###Station Data
One could improve the station network. For example, the stations around Zürich HB can be merged into one station. Similarly, the stations around Bhf. Enge can be merged. Unfortunately, during the creating of the map Bhf. Altstetten was forgotten. It would add fun to the game and increase the authenticity of the game.

###Refactor
Often functions take an ID as an argument as for example in the function isUserInGame on line 54 in the GameService class within the service package. It would be better that this function takes a game and a user instance as arguments. This would reduce the complexity of the function and would make the code more uniform and therefore more readable. Such blemishes are found throughout the code and could be easily fixed while refactoring the code.

###Modes
A team of sophisticated developers could add different game modes. E.g. the number of stations could be changed before starting the game to make it easier or harder to catch Mr. X. It would be interesting to implement an option to cast a vote in the lobby to determine who is going to be Mr. X. We thought that it would also be fun if the different players could choose their starting position. A completely new game mode could be that Mr. X starts at Triemli and must reach the airport of Zürich to win the game.
## Authors and acknowledgement

### Backend developers
- [Filip "BigBoi" Dombos](https://github.com/dombosfi), BSc Computer Science, University of Zurich
- [Maximilian "Minimillian" Huwyler](https://github.com/maximilianhuwyler), BSc Computer Science, University of Zurich

## MIT Licence
Copyright (c) 2021 [Filip Dombos, Maximilian Huwyler]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


