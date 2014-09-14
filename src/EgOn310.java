import java.util.ArrayDeque;
import java.util.Deque;


public class EgOn310 extends Rbtree{

	/**
	 * @param args
	 */
	public static void complete(Node root, Node nil){
		Deque<Node> parentstack = new ArrayDeque<Node>();
		Node node = new Node(0);
		parentstack.push(root);
		while(!parentstack.isEmpty()){
			node = parentstack.pop();
			if(node.left != null){
				node.left.parent = node;
				parentstack.push(node.left);

			}
			else{
				node.left = nil;
			}
			if(node.right != null){
				node.right.parent = node;
				parentstack.push(node.right);

			}
			else{
				node.right = nil;
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//build tree on 310, 0 read, 1 black
		EgOn310 tree = new EgOn310();
		tree.nil.key = 0;
		tree.root.key = 26;
		Node n17 = new Node(17, 0);
		Node n41 = new Node(41, 1);
		Node n14 = new Node(14, 1);
		Node n21 = new Node(21, 1);
		Node n30 = new Node(30, 0);
		Node n47 = new Node(47, 1);
		Node n10 = new Node(10, 0);
		Node n16 = new Node(16, 1);
		Node n19 = new Node(19, 1);
		Node n23 = new Node(23, 1);
		Node n28 = new Node(28, 1);
		Node n38 = new Node(38, 1);
		Node n7 = new Node(7, 1);
		Node n12 = new Node(12, 1);
		Node n15 = new Node(15, 0);
		Node n20 = new Node(20, 0);
		Node n35 = new Node(35, 0);
		Node n39 = new Node(39, 0);
		Node n3 = new Node(3, 0);
		
		tree.root.left = n17;
		tree.root.right = n41;
		n17.left = n14;
		n17.right = n21;
		n41.left = n30;
		n41.right = n47;
		n14.left = n10;
		n14.right = n16;
		n21.left = n19;
		n21.right = n23;
		n30.left = n28;
		n30.right = n38;
		n10.left = n7;
		n10.right = n12;
		n16.left = n15;
		n19.right = n20;
		n38.left = n35;
		n38.right = n39;
		n7.left = n3;

		complete(tree.root, tree.nil);
		tree.rbdelete(tree, n41);
		
		
		tree.printTreeKey(tree.root);
		tree.printTreeColor(tree.root);

		
		
		
		
		
	}

}
