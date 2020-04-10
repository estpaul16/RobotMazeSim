
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.stonedahl.robotmaze.SimRobot;


public class SimMazeSolver {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		
		// NOTE: You can change the 500 ms animation delay to make the simulation run faster or slower.
		int animationDelay = 100;
		boolean animateNeckTurns = false; // show every time the robot rotates its neck?
		boolean animateTurns = false;     // show every time the robot body rotates?
		SimRobot simRobot = new SimRobot("maze6.txt", animationDelay, animateNeckTurns, animateTurns, new Scanner(System.in)); 
		solveMaze(simRobot);
	}
	
	public static void solveMaze(SimRobot simRobot) {
		// The RobotTracker keeps track of where the robot is and what direction it
		// is pointing, which allows you to control the simulated physics robot 
		//  all in terms of N/S/E/W, instead of worrying which way the robot is pointing.
		
		RobotTracker trackerBot = new RobotTracker(simRobot);
		Stack<Character> robotMoves = new Stack<Character>();
		MapGrid map = new MapGrid();
		while(!trackerBot.isSittingOnGoal()) {
			int x = trackerBot.getX();
			int y = trackerBot.getY();
			GridCell currentCell = map.getCellAt(x, y);
			List<Character> openDirs = trackerBot.findDirectionsWithoutWall();
			currentCell.setVisited();
			map.removeWalls(x, y, openDirs);
			
			
			moveToNextCell(trackerBot, map, currentCell, robotMoves); 
			if (trackerBot.isSittingOnGoal()) {
				// If the goal has been found after moving, we have to reassign currentCell and
				//  openDirs as well as marking walls again because we will not be re-entering the loop
				currentCell = map.getCellAt(trackerBot.getX(), trackerBot.getY());
				openDirs = trackerBot.findDirectionsWithoutWall();
				currentCell.setVisited();
				map.removeWalls(currentCell.getX(), currentCell.getY(), openDirs);
				System.out.println("I FOUND THE GOAL!");
				System.out.println("Calculating best route home...");
				simRobot.msDelay(2000);

				break; // break out of this exploration WHILE loop early
			}
		}
		System.out.println("Got it!");
		simRobot.waitForEnterKey();
		
		GridCell goalCell = map.getCellAt(trackerBot.getX(), trackerBot.getY());
		// Once a path it takes contains as many moves as the stack, we'd just take the reverse stack path
		int depthLimit = robotMoves.size() - 1; 
		returnHome(goalCell, map, depthLimit, robotMoves, trackerBot);
		
		
		
	}
	
	
	private static void moveToNextCell(RobotTracker trackerBot, MapGrid map, GridCell currentCell, Stack<Character> robotMoves) {
		GridCell northCell = map.getCellInDir(currentCell, 'N');
		GridCell eastCell = map.getCellInDir(currentCell, 'E');
		GridCell westCell = map.getCellInDir(currentCell, 'W');
		GridCell southCell = map.getCellInDir(currentCell, 'S');
		
		// Only moves in direction if there is no wall and new cell has not been visited.
		// No need to check if the GridCell is null due to being out of bounds, the current 
		// cell will have an outer wall in that direction, and will therefore not pass the if statement
		if (!currentCell.hasWall('N') && !northCell.hasBeenVisited()) {
			trackerBot.moveOneCell('N');
			robotMoves.push('N');
		} else if (!currentCell.hasWall('E') && !eastCell.hasBeenVisited()) {
			trackerBot.moveOneCell('E');
			robotMoves.push('E');
		} else if (!currentCell.hasWall('W') && !westCell.hasBeenVisited()) {
			trackerBot.moveOneCell('W');
			robotMoves.push('W');
		} else if (!currentCell.hasWall('S') && !southCell.hasBeenVisited()) {
			trackerBot.moveOneCell('S');
			robotMoves.push('S');
		} else {
			// If there are no open or  non-visited spaces, go back to previous cell
			char backDir = oppositeDirection(robotMoves.pop());
			trackerBot.moveOneCell(backDir);
		}
	}
	
	private static void returnHome(GridCell rootCell, MapGrid map, int depthLimit, 
			Stack<Character> robotMoves, RobotTracker trackerBot) {
		SearchNode root = createNodeFromCell(rootCell);
		SearchTree tree = new SearchTree(root, map);
		tree.buildTreeRecursive(root, map, depthLimit);
		
		if (!tree.isHomeFound()) {	
			// If a homeNode was not found within the depth limit of the tree, 
			//  then no other path will be better then the easy reverse stack one
			returnHomeEasy(robotMoves, trackerBot);
		} else {
			List<Character> directionsBack = treeDFS(tree, 0, 0);
			for (char dir : directionsBack) {
				trackerBot.moveOneCell(dir);
			}
		}
		System.out.println("You're Home!");
		
		
		
	}
	
	private static void returnHomeEasy(Stack<Character> robotMoves, RobotTracker trackerBot) {
		while(!robotMoves.isEmpty()) {
			char backDir = oppositeDirection(robotMoves.pop());
			trackerBot.moveOneCell(backDir);
		}
	}

	
	/**
	 * @param tree - SearchTree to search for the best path back home
	 * @return List of directions for the optimal path back
	 */
	private static List<Character> treeDFS(SearchTree tree, int homeX, int homeY) {
		SearchNode root = tree.getRoot();
		Stack<Character> directionsHome = new Stack<Character>();
		// iterative deepening
		for (int i = 1; i <= tree.getSize(); i++) {
			// If it finds a path home, return the list of directions home
			if(DFSRecursive(root, directionsHome, homeX, homeY, tree.getSize(), i)) { // returns true when DFS found a path home
				List<Character> list = new ArrayList<Character>(directionsHome);	// converts stack to ArrayList for easier iterating
				return list;
			}
		}
		
		return null; // This should never be reached, since there must be a path back in the tree 
	}

	/**
	 * Returns true if home has been found
	 * Pushes and pops to the stack passed in as a parameter
	 */
	private static boolean DFSRecursive(SearchNode current, Stack<Character> path, int homeX, int homeY, int treeSize, int iterDeepLvl) {
		if (current.getX() == homeX && current.getY() == homeY) {
			return true;
		}
		List<SearchNode> adjacentNodes = current.getChildren();
		boolean homeFound = false;
		for (int i = 0; i < adjacentNodes.size(); i++) {
			if (homeFound) {
				System.out.println("Home Found!");
				return true;
			} else {
				SearchNode adjNode = adjacentNodes.get(i);
				char dirOfAdjNode = indexToDirChar(i);
				if (adjNode == null) {	
					// Handle nullPointerException and move on to next element of adjacent List	
				} else if (adjNode.getDepth() > iterDeepLvl) {	
					// Move on to next element of adjacent List	
				} else {	// begin next recursive step
					path.push(dirOfAdjNode);
					homeFound = DFSRecursive(adjNode, path, homeX, homeY, treeSize, iterDeepLvl);
					if (homeFound == false) {
						path.pop();
					}
						
				}
			}
			
		}
		return homeFound;
	}
	
	
	/**
	 * Returns the opposite of the input direction
	 */
	private static char oppositeDirection(char dir) {
		if (dir == 'N') return 'S';
		if (dir == 'S') return 'N';
		if (dir == 'W') return 'E';
		if (dir == 'E') return 'W';
		return '0';
	}
	
	private static SearchNode createNodeFromCell(GridCell cell) {
		int x = cell.getX();
		int y = cell.getY();
		SearchNode node = new SearchNode(x, y);
		return node;
	}
	
	private static char indexToDirChar(int index) {
		switch(index) {
		case 0:
			return 'N';
		case 1:
			return 'E';
		case 2:
			return 'W';
		default:
			return 'S';
		}
	}

}

