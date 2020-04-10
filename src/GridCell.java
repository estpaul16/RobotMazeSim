import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents one cell state of the grid.
 * Every cell has x and y coordinates, four walls surrounding it,
 * and a state isVisted which represents whether the robot has been
 * in it or not.
 * @author evanst.paul
 *
 */
public class GridCell {
	private int x;
	private int y;
	private boolean[] wallsSurrounding = new boolean[4];
	private boolean isVisited;	
	
	public GridCell() {
		boolean[] temp = {true,true,true,true}; //starting by assuming walls are everywhere
		this.wallsSurrounding = temp;
		isVisited = false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean[] getWallsSurrounding() {
		return wallsSurrounding;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * This method converts direction char to index for use with wallsSurrounding array 
	 * @param wallDir - Direction of the wall we're finding index for
	 * @return - index value of wallDir
	 */
	public int boolIndexOfDir(char wallDir) {
		int wallDirIndex;
		
		switch (wallDir) {
		case 'N':
			wallDirIndex = 0;
			break;
		case 'E':
			wallDirIndex = 1;
			break;
		case 'W':
			wallDirIndex = 2;
			break;
		default:
			wallDirIndex = 3;
		}
		
		return wallDirIndex;
	}
	
	public boolean hasBeenVisited() {
		return isVisited;
	}
	
	public void setVisited() {
		isVisited = true;
	}
	
	
	public boolean hasWall(char wallDir) {
		int wallDirIndex = boolIndexOfDir(wallDir);
		return wallsSurrounding[wallDirIndex];
	}
	
	/**
	 * Removes grid wall in the specified direction (N,E,W,S)
	 * @param wallDir
	 * @return *true* when a wall has been removed
	 */
	public boolean removeWallInDir(char wallDir) {
		if (!hasWall(wallDir)) {
			return false;
		}
		int wallDirIndex = boolIndexOfDir(wallDir);
		wallsSurrounding[wallDirIndex] = false;
		return true;
	}
	
	public void removeWallsInDirections(List<Character> openDirs) {
		for (Character dir : openDirs) {
			removeWallInDir(dir);
		}
	}
	
	public void addWall(char wallDir) {
		int wallDirIndex = boolIndexOfDir(wallDir);
		wallsSurrounding[wallDirIndex] = true;
	}
	
	/**
	 * @return List of the directions within the grid that don't have walls
	 */
	public List<Character> directionsWithoutWalls() {
		List<Character> retList = new ArrayList<Character>();
		boolean[] wallsSurrounding = getWallsSurrounding();
		if (wallsSurrounding[0] == false) {
			retList.add('N');
		}
		if (wallsSurrounding[1] == false) {
			retList.add('E');
		}
		if (wallsSurrounding[2] == false) {
			retList.add('W');
		}
		if (wallsSurrounding[3] == false) {
			retList.add('S');
		}
		return retList;
		
	}
}
