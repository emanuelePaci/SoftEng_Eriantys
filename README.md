# Prova Finale Ingegneria del Software 2022
## About

Implementation of the Eriantys board game.

The project consists in the implementation of a distributed system using the MVC (Model-View-Controller) pattern.

The network is managed with the use of sockets.

Interaction and gameplay: command line (CLI) and graphics (GUI).

Final Score: 30/30

## Documentation

### UML
- [Initial UML](https://github.com/emanuelePaci/SoftEng_Eriantys/tree/main/Deliverables/UML/Initial)
- [Final UML](https://github.com/emanuelePaci/SoftEng_Eriantys/tree/main/Deliverables/UML/Final)

### JavaDoc
The following documentation includes a description of the classes and methods used, which can be found [here](https://github.com/emanuelePaci/SoftEng_Eriantys/tree/main/Deliverables/Code%20Coverage).

### Coverage report
At the following [link](https://github.com/emanuelePaci/SoftEng_Eriantys/tree/main/Deliverables/Code%20Coverage) you can consult the coverage report of the tests carried out with Junit.



**Coverage criteria: code lines**

| Package | Coverage |
|:-----------------------|:------------------------------------:|
| Controller | 432/451 (95%)
| Model | 637/643 (99%)


### Libraries & Plugins
|Library/Plugin|Description|
|---------------|-----------|
|__Maven__|Build automation tool used primarily for Java projects|
|__JavaFx__|Graphic library to develop user interfaces| 
|__JUnit__|Unit testing framework|
|__JAnsi__|Graphic library for Windows compatibility with AnsiCode|

## Functionalities
### Developed functionalities
- Complete rules
- CLI
- GUI
- Socket
- 2 AF (Advanced Function):
    - __Character cards:__ implemented all 12 character cards.
    - __Multiple games:__ server designed so that it can manage multiple games at the same time.

## Jar execution
In order to run correctly you should have JDK 17 or above. [Here](https://www.oracle.com/java/technologies/downloads/#java17) you can find the download page.  
The user has two jars available, one for the server and one for the client.
You can find them [here](https://github.com/emanuelePaci/SoftEng_Eriantys/tree/main/Deliverables/JAR).
### Server
The server jar can be executed either in Windows and Unix platforms with the following command:
```
java -jar Server.jar
```
The server asks the port to bind to. 

### Client
Both base_client and M1_client works in Windows and Linux.  
Due to some path conflict JavaFx is not able to generate a Jar working either for Intel Mac and Apple Silicon Mac; for the first one you have to launch base_client, for the other M1_client. 
#### CLI
CLI has full compatibility with Windows and Unix platforms. You can launch it with the following command:
```
java -jar Client.jar -c
```
or
```
java -jar Client.jar -cli
```
On all OS we suggest to open the terminal at full screen before launching the application.   
For best performance we suggest to use mono spaced font (12 size) and to have UTF-8 character set.  
To know how to check/set-up UTF-8 look to the following [instructions](https://github.com/LorenzoPaleari/ing-sw-2022-Paci-Paleari-Puppinato/wiki/CLI-Settings#utf-8).  
On Windows you should use CMD. 

#### GUI
You can double click on the jar file if your OS supports it or you can launch it with the terminal using the following command:
```
java -jar Client.jar
```  
In Windows if the GUI window exits the screen boarder we suggest to check Windows Scaling and to lower it:  
Settings > Display > Scale and Layout (For Example from 150% to 125%).

## HOW TO PLAY
#### Lobby Selection
During set-up phase you will be asked if you want to create a new game or not.  
By selecting "NO" the server will send all the available lobbies with NumPlayer, ExpertMode and the Partecipants: you have the possibility to choose in which lobby you would like to enter by scrolling throw them (GUI) or by changing page with "Next/Prev" (CLI).

#### CLI
You will find on screen the command summary.  
When selecting a command to use you will be guided towards its completition by the game itself.

#### GUI
The assistants cards are found in the bottom-left part of the screen, if you go over them with the mouse they will become bigger and more visible. 

Students and MotherNature can be moved by dragging and dropping them. If you go over a valid place (Island / Board) the boarder change color to make you aware that it is a valid place in which you can release your mouse.  

Clouds are choosed by clicking on them.

Character Card description and instruction will be displayed in a new window after clicking on them.  
In order to use a Character you should click on the Play button.
Some of the Characters are playable instantly, other ones requires the player to perform some actions either on the main game (Draggin students/do not entry tiles or clicking on a island) or selecting options int the Character window.


## About us
* Paci Emanuele ([@emanuelePaci](https://github.com/emanuelePaci))
* Paleari Lorenzo ([@LorenzoPaleari](https://github.com/LorenzoPaleari))
* Puppinato Thomas ([@PuppinatoThomas](https://github.com/PuppinatoThomas))
