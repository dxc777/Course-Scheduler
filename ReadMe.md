# Course Scheduler

## What is it?
This project uses a graph and the topological sort algorithm to allow students plan their course schedule.

## How does it work?
It takes in a textfile that contains classes a student plans on taking.
The file takes on this format: 
* Each line contains information for one class
* Each line must use this format: [course identifier],[course name],[unit worth of class], [list of prerequisites for course]
* the course identifier is a string of characters that is used to uniquely identify a class 
* the course name is a string of characters that is meant for the user to identify a class it does not matter what you put for the course name
* unit worth are the amount of units the class is worth it can be entered as 4.00 or 4 for each class
* the last requirement is a list of prerequisites for the class. This is a comma seperated list of course identifiers not course names. 
* the prerequisite list is comma seperated for each course identifier
* spacing does not matter and neither does uppercase or lowercase. (spacing will be removed and all characters will be converted to uppercase) except for the course 
name as that will be left alone
