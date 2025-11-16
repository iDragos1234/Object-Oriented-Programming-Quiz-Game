# Backlog Group 50 CSE1105

# Contents:

**Functional requirements:**

 Must haves:

 Should haves:

 Could haves:

 Won&#39;t haves:

**Development requirements:**

## **Functional requirements**

**Must Have:**

Playable game

- The user shall be able to see and read the text-based questions

- The user shall be able to play a game consisting of 20 questions (1)

- The user shall be able to quickly pick up the game without additional tutorials

Questions

- The user should be able to answer multiple choice questions consisting of three possible answers

- The user shall be able to see the correct answer after each question

- The user shall be able to answer approximation questions there they have to guess the correct number (3)

- The user should be able to get more points the closer he is to the answer on the estimate questions

- The user shall be able to see questions and answers at the same time, in order to answer as quickly as possible

Leaderboard (8)

- The user should be able to compare their single player score with other players' on a global leaderboard

- The users should be able to see a leaderboard of all the players when playing multiplayer, at the end of the game.

- The user shall communicate with the server to exchange information(4)

Overview screen

- The user shall be able to input their name at the beginning before joining any game

- The user shall have a "Solo" button that allows them to play alone

- The user shall have a "Multiplayer" button that allows them to play with other players

Multiplayer interactions

- The user shall be able to play with at least 5 other players

- The user shall be able to enter a waiting lobby where other people can join and group up

- The user shall be able to see how many people are waiting in the lobby

- The user shall have a "Start" button which can be clicked by every player in the lobby to start the game

- The users shall be able so answer simultaneously in order to track who is the fastest

- The users shall have unique names when entering a lobby, otherwise they will be prompted to change it(2)

Timer

- The user shall be able to see how much time is left for each question

Database

- The user’s score shall be saved in the database to be used in the leaderboard

- The user’s score shall be used in the construction of the leaderboard

- The questions shall be stored in a database, to use for question generation during the game


**Should Have:**

Questions

- The user should be able to see the current question number.

- The user should see hard to guess answers, that can’t be derived from the question itself.

Leaderboard

- The user should be able to see their own score.

- The user should be able to see the leaderboard at half-time in order to see where they are placed (10)

Game

- The user should be able to start a game even when other games are already in progress

Powerups

- The user should be able to use prower-ups only while the timer hasn’t ran out

- The user should be able to use certain power-ups only when the question is compatible

- The user should be able to use a power up “Joker” in order to eliminate one wrong answer

- The user should be able to use a power up “Double up” in order to double up their points if the answer is correct

- The user should be able to use a power up “Time Attack” in order to shorten the time left for other players (7)

Synchronous questions

- The user should be able to get more points, the more time is left for the question

End of the game

- The user should be able to click a “Replay” button in order to instantly get to the waiting lobby

- The user should be able to click a “Main Menu” to get back to the splash screen

Design

- The user should be able to see an image to help them understand the question

**Could Have:**

Playable multiplayer game

- The user could be able to play with at least 19 other players

Client-Server communication

- The user could be kicked after stop playing mid-game, as to not slow down the game (6)

Questions

- The user could check hints in order to help them navigate the game

Emoji reactions (multiplayer only)

- The user could be able to use emojis to communicate with other players

- The user could see all of the reactions in the bottom corner of the screen

**Won&#39;t Have:**

The user won’t chat during the game, because it is fast paced

**Developer requirements:**

Should be developed in Java with the help of Spring

Won&#39;t use web sockets for the server

Rest api should be used

No demanding hardware requirements

**Notes:**

(1) The same amount of questions across all games for leaderboard purposes.

(2) First come first server, last one gets an error and cannot join the waiting room.

(3) more points for a closer answer

(4) Only 1 server, that each client talks to.

(5) Limited amount of time to answer each question. Time over? -\&gt; Answer revealed; People answer at the same time

(6) Since the game is synchronous this is a could have.

(7) When someones time has run out, but the question timer is still running the answer controls are disabled

(8) Same names can appear multiple times on the leaderboard

(9) This is a just-played-game leaderboard, not a global multiplayer score leaderboard.

(10) Of 20 questions after the first 10 the leaderboard is shown and then we continue.

