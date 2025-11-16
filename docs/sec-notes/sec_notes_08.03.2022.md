## Agenda for week 5


Location:	Drebbelweg, PC hall 1, cubicle 13
Date:           14.03.2022\
Time:		13:45\
Main focus:     Solo Game, Multiplayer Design\
Chair:          Orhan Agaoglu\
Note taker:     Bertold B. Kovacs\
Attendees: 	Bertold B. Kovacs, Bram Snelten, Dragoș Ileana, Filip Dobrev, Orhan Agaoglu, Yanzhi Chen

#Agenda-Items
We started a bit late at 13.50, with the stand up meeting.

[13:56 – 13:57] Just a quick check about the milestones.
We have to discuss how to implement multi, and game session. We are done otherwise with milestones.

[13:57 – 14:12] Finalizing the Solo Game
Finalization of the solo game: get the polling working. Index the table. We can implement a bad way as well, but this might be the best.
We discussed how we generate questions: by getting 3 random activities and checking the most expensive one for MC questions.
Dragos explained the Question types and his explanatory PDF.
Everyone should read the backlog.

[14:12 - 14:34] Nada feedback
	- We need a tasklist.
	- Tasks are not distributed equally.  
	- Weights are nice! We should use the time estimate feature as well.
	- Milestones exist.
	- Tests are not enough. People should have more merge requests.
	- We need better commit messages, but otherwise nice.
	- Less folders, if we have a folder it needs to have a point. No folder for a single class.
	- Some methods (client side) need JavaDOC.
	- Maybe split the server utils to player utils, leaderboard utils...
	- Some controllers missing tests. Client side does not need tests for the scenes, but server side does.
	- We should merge to main atleast twice a week not only Sunday
	- Close an issue in 3 days from when you are assigned to it. Work more!
	- More code reviews. Some code reviews are not very helpful.
	- Fix checkstyle before pushing, on your laptop.
	- Set an internal deadline for the completion of the MUST HAVEs.
	- Orhan, Bertold, Tom, Bram: a descriptive plan sent on Mattermost to Nada about what we are going to work on.

Admin interface? What is that?
A button with which we can go to the admin interface. There you can see and modify the activities (edit, delete, add).

[14:34 – 14:38] Reviewing Action Points
Index the database; until then, we have a (badly) working version.
Do the websockets. 

[14:38 – 14:39] Feedback Round – tips & tops for last week
We will do it later.
Tops: it is nice that we still have a great team spirit. We help each other, keep this up! Orhan is great at structuring the agenda, and the meeting.
Tips: we should not be disencouraged by problems, we should start working harder. We should do extra work not just aim for the minimum line but exceed it.

From Nada: fix the red flags. If we do: we pass! If we do not: most likely, we will fail.