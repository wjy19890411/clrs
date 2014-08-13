
public class Node {

	/**
	 * @param args
	 */
	
	//treenode properties
	public int key;
	public Node left = null;
	public Node right = null;
	public Node parent = null;
	
	//black-red tree properties
	public int color; //0 red, 1 black
	
	
	//tree constructor
	public Node(int key){
		this.key = key;
	}
	
	//black-red tree constructor
	public Node(int key, int color){
		this.key = key;
		this.color = color;
	}

}
