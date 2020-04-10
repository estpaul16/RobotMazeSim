import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a single node in the SearchTree class.
 * The contains information about its x and y coordinates,
 * depth level in the tree, and four child nodes, one for
 * each direction.
 * @author evanst.paul
 */
public class SearchNode {
	private int x;
	private int y;
	private int depthLevel; 
	private SearchNode north;
	private SearchNode east;
	private SearchNode west;
	private SearchNode south;
	
	public SearchNode(int x, int y) {
		this.x = x;
		this.y = y;
		this.depthLevel = 0;
	}

	
	public SearchNode getNorthNode() {
		return north;
	}

	public void addNorthNode(SearchNode north) {
		this.north = north;
	}

	public SearchNode getEastNode() {
		return east;
	}

	public void addEastNode(SearchNode east) {
		this.east = east;
	}

	public SearchNode getWestNode() {
		return west;
	}

	public void addWestNode(SearchNode west) {
		this.west = west;
	}

	public SearchNode getSouthNode() {
		return south;
	}

	public void addSouthNode(SearchNode south) {
		this.south = south;
	}
	
	public void addNodeInDir(SearchNode adjacentNode, char dir) {
		switch(dir) {
		case 'N':
			addNorthNode(adjacentNode);
			break;
		case 'E':
			addEastNode(adjacentNode);
			break;
		case 'W':
			addWestNode(adjacentNode);
			break;
		case 'S':
			addSouthNode(adjacentNode);
		}
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getDepth() {
		return depthLevel;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setDepth(int depthLevel) {
		this.depthLevel = depthLevel;
	}
	
	/**
	 * Includes null nodes for indexing purposes
	 */
	public List<SearchNode> getChildren() {
		List<SearchNode> children = new ArrayList<SearchNode>();
		children.add(north);
		children.add(east);
		children.add(west);
		children.add(south);
		return children;
	}
	
	public String toString() {
		String string = String.format("(%d, %d) Depth %d", x, y, depthLevel);
		return string;
	}
	

		
	
}
