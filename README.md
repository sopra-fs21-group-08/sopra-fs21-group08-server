# SoPra FS21 - Scotland Yard Zürich Edition - Server

## Introduction
Hi there, we are five students from the University of Zurich, who developed this application for the Software Engineering Lab FS21. This is the server side implementation of the project. The aim of this project was to create an online version of the popular board game "Scotland Yard". The main feature of the project was to transfer the game settings from London (where the game is originally based) to Zurich. All the stations and lines displayed in the game are from the real public transport system of Zurich.

Users can register and login to their profile on the [website](https://sopra-fs21-group-08-client.herokuapp.com/), where they see their player statistics and other online users. From there, the users can create or join public lobbies to play together with their friends. After a game has started, the players get assigned a role (detective or Mister X) and they are randomly positioned on the map. The goal is then for the detectives to catch Mister X using the public transport system of Zurich. To enhance the user experience, the players can chat with each other during a game and replay after the game is over. Additionally, after each game the score gets added to the players' user statistics.

## Technologies used
- [Spring Boot](https://spring.io/projects/spring-boot) Application
- [JPA](https://www.oracle.com/java/technologies/persistence-jsp.html) and [Hibernate](https://hibernate.org) for the database management

Connection to the [frontend](https://github.com/sopra-fs21-group-08/sopra-fs21-group08-client/) works through representational state transfer.

## Main Components

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

## Authors and acknowledgement

### Backend developers
- Filip Dombos, BSc Computer Science, University of Zurich
- Maximilian Huwyler, BSc Computer Science, University of Zurich

## MIT Licence
Copyright (c) 2021 [Zoia Katashinskaia, Tobias Frauenfelder, Marvin Münger]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


