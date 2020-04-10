import java.util.List;

/**
 * This class represents a tree data structure for all the possible
 * node states within n moves from a root state, each state having
 * no recollection of the state it came from.
 * 
 * The purpose of this class is to create a tree that can be searched
 * through to find the optimal path back to a home state, granted such
 * a path exists that is more optimal than the path took to get there
 * @author evanst.paul
 *
 */
public class SearchTree {
	private SearchNode root;	
	private int size;
	private boolean homeFound;
	
	
	public SearchTree(SearchNode root, MapGrid map) {
		this.root = root;
		this.homeFound = false;
		this.size = 0;	// not counting the root node
	}
	
	
	public int getSize() {
		return size;
	}
	
	
	public SearchNode getRoot() {
		return root;
	}
	
	public boolean isHomeFound() {
		return homeFound;
	}

	/**
	 * This class builds the tree recursively based on the data in map that the robot
	 * collected while finding its way to the original goal state.
	 * @param depthLimit - Represents how deep the tree will be built.
	 */
	public void buildTreeRecursive(SearchNode currentNode, MapGrid map, int depthLimit) {
		size = depthLimit;
		if (currentNode.getX() == 0 && currentNode.getY() == 0) {
			homeFound = true; 	// If home has not been found within depthLimit, then the reverse stack path is fastest route
			return; 			// If the current Node is the home node, don't build deeper
		} else if (currentNode.getDepth() + 1 > depthLimit) {	// if currentNode is at the depth limit, don't build further
			return;
		} else {
			int currentDepth = currentNode.getDepth();
			GridCell currentCell = getCellFromNodeXY(currentNode, map);
			List<Character> walls = currentCell.directionsWithoutWalls();
			for (char dir : walls) {				
				GridCell adjacentCell = map.getCellInDir(currentCell, dir);
				SearchNode adjacentNode = createNodeFromCell(adjacentCell);
				adjacentNode.setDepth(currentDepth + 1);
				currentNode.addNodeInDir(adjacentNode, dir);				
				buildTreeRecursive(adjacentNode, map, depthLimit);
			}
		}
		
		
	}
	
	
	private static SearchNode createNodeFromCell(GridCell cell) {
		int x = cell.getX();
		int y = cell.getY();
		SearchNode node = new SearchNode(x, y);
		return node;
	}
	
	private static GridCell getCellFromNodeXY(SearchNode node, MapGrid map) {
		int x = node.getX();
		int y = node.getY();
		GridCell cell = map.getCellAt(x, y);
		return cell;
		
	}
	
	
	
	
	
}
