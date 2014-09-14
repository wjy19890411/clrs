import java.util.ArrayDeque;
import java.util.Deque;


public class EgOn317 extends Rbtree{

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
		
		Node n11 = new Node(11, 1);
		Node n2 = new Node(2, 0);
		Node n14 = new Node(14, 1);
		Node n1 = new Node(1, 1);
		Node n7 = new Node(7, 1);
		Node n15 = new Node(15, 0);
		Node n5 = new Node(5, 0);
		Node n8 = new Node(8, 0);
		Node n4 = new Node(4, 0);
		
		EgOn317 tree = new EgOn317();
		tree.root.key = 11;
		
		tree.root.left = n2;
		tree.root.right = n14;
		n2.left = n1;
		n2.right = n7;
		n14.right = n15;
		n7.left = n5;
		n7.right = n8;
		
		complete(tree.root, tree.nil);
		tree.printTreeKey(tree.root);
		tree.printTreeColor(tree.root);
		tree.rbinsert(tree, n4);
		tree.printTreeKey(tree.root);
		tree.printTreeColor(tree.root);
		
		
		
	}

}
