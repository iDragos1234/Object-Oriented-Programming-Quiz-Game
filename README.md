# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project

## Group members

| Profile Picture | Name | Email |
|---|---|---|
| <img src="https://i.imgur.com/XJ0PL4l.jpeg" width=60px> | Filip Dobrev | F.Dobrev@student.tudelft.nl |
| | Bertold B. Kovacs | B.B.Kovacs-1@student.tudelft.nl |
| | Dragoș Ileana | D.Ileana@student.tudelft.nl |
|<img src="https://i.imgur.com/rWRc1P6.jpg" width=60px>| Orhan Agaoglu | O.Agaoglu@student.tudelft.nl |
| | Yanzhi Chen | Y.Chen-116@student.tudelft.nl |
| | Bram Snelten | B.Snelten@student.tudelft.nl |
<!-- Instructions (remove once assignment has been completed -->
<!-- - Add (only!) your own name to the table above (use Markdown formatting) -->
<!-- - Mention your *student* email address -->
<!-- - Preferably add a recognizable photo, otherwise add your GitLab photo -->
<!-- - (please make sure the photos have the same size) --> 

## How to run it

 - Installing the game on Windows:

1. Download the latest version of Java, JavaFX and Gradle
2. Go to the oopp-group-50 repository and clone the main branch on your system from the clone button(make sure you have a SSH key):
 - Using a terminal go to the directory where you want to clone it and write "git clone //address//"
3. Open the `repository-template` folder and write "gradle build" in the terminal
4. After the process has finished, write "gradle run", this will run the server
5. You can open the `repository-template` folder in an IDE like IntelliJ, Eclipse etc.
6. From there if you haven't stopped the server, you can just find the "Main" in the client and run it (`client/src/main/java/client/Main.java`)
7. If the server is not running you can start it from the "Main" on the server side (`server/src/main/java/server/Main.java`)


 - Adding the images:

Download the latest release of the activity bank zip file.
After unzipping the file you should rename the folder to "Activity-Images".
Then, move the folder to "client/src/main/resources/images/".

 - Adding the database:

After adding the images you will find a json file called activities.json in the following directory: "client/src/main/resources/images/".
Move this json file to the following directory: "server/src/main/resources/"
Then you can start the server and make a GET request using Postman to the following url:"http://localhost:8080/api/activities/updateJsonFile".
This will automatically populate the database.


_**Enjoy, now you can play the game!**_

## How to contribute to it

## Copyright / License (opt.)
_&copy; Group-50 INC. All rights reserved!_
