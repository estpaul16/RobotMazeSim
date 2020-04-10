# RobotMazeSim
Simulation of a roaming robot using AI to not only solve a maze without repeating any steps, but also find the best possible path back to the starting point given all the data it picked up along the way. A search tree is created based on the information the robot picked up by analyzing its surrounding on the way to the end goal. This tree is then searched using iterative deepening to find the most optimal solution back. The robot then takes this path back.

The entire simulation occurs in the console. 

To start the search, run SimMazeSolver.java. If you want to change the maze the robot is trying to solve, you can do so in the main method of SimMazeSolver.

The SimRobot.java and RobotTracker.java files as well as the maze.txt files were written by my professor Dr. Forrest Stonedahl as a base setup for this project. All the other files were written by me.

This project gave me a better understanding of important data structures such as stacks and trees, as well as useful search algorithms like Iterative Deepening DFS.

Feel free to create your own .txt maze files.

