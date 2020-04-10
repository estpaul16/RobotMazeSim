
import java.util.List;

/**
 * This class contains a 2d array of GridCells representing all of the data that 
 * the robot picked up on the way to its initial goal, including wall locations 
 * and which cells have been visited by the robot.
 * @author evanst.paul
 *
 */
public class MapGrid {
	private GridCell[][] map = new GridCell[5][3];
	
	public MapGrid() {
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				GridCell currentCell = new GridCell(); 
				currentCell.setX(i);
				currentCell.setY(j);
				map[i][j] = currentCell;
			}
		}
	}
	
	public boolean hasWall(int x, int y, char dir) {
		return map[x][y].hasWall(dir);
	}
	
	public boolean[] getWalls(int x, int y) {
		return map[x][y].getWallsSurrounding();
	}
	
	public boolean removeWall(int x, int y, char dir) {
		return map[x][y].removeWallInDir(dir);
	}
	
	public void removeWalls(int x, int y, List<Character> openDirs) {
		map[x][y].removeWallsInDirections(openDirs);
		// Removes the other side of the wall
		if (openDirs.contains('N')) {
			removeWall(x, y + 1, 'S');
		}
		if (openDirs.contains('E')) {
			removeWall(x + 1, y, 'W');
		}
		if (openDirs.contains('W')) {
			removeWall(x - 1, y, 'E');
		}
		if (openDirs.contains('S')) {
			removeWall(x, y - 1, 'N');
		}
	}
	
	public void addWall(int x, int y, char dir) {
		map[x][y].addWall(dir);
		// Might have to add wall at same spot in next cell
	}
	
	public void setVisited(int x, int y) {
		map[x][y].setVisited();
	}
	
	/**
	 * Returns null if the coordinates are out of bounds in the grid
	 */
	public GridCell getCellAt(int x, int y) {
		
		if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
			return null;
		}
		return map[x][y];
	}
	
	
	/**
	 * 
	 * @param currentCell - The current GridCell the robot is located
	 * @param dir - The direction from the current cell of the cell you want to find.
	 * @return the GridCell in the direction from the current GridCell.
	 * 		    If it is out of the boundaries of the grid it will return null
	 */
	public GridCell getCellInDir(GridCell currentCell, char dir) {
		int currentX = currentCell.getX();
		int currentY = currentCell.getY();
		GridCell nextCell;
		if (dir == 'N') {
			nextCell = getCellAt(currentX, currentY + 1);
			return nextCell;
		} else if (dir == 'E') {
			nextCell = getCellAt(currentX + 1, currentY);
			return nextCell;
		} else if (dir == 'W') {
			nextCell = getCellAt(currentX - 1, currentY);
			return nextCell;
		} else { // dir == 'S'
			nextCell = getCellAt(currentX, currentY - 1);
			return nextCell;
			
		}
	}
	
}
