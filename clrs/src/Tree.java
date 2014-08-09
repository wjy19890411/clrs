import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;



public class Tree {

	/**
	 * @param args
	 */
	
	public Node root = new Node(0, 1);
	public Node nil = new Node(0, 1);
	Tree(){
		nil.left = nil;
		nil.right = nil;
		nil.parent = nil;
		root.parent = nil;
		root.left = nil;
		root.right = nil;
	}
	
	//tree traversals
	
	public void preOrder(Node root){
		Deque<Node> parentstack = new ArrayDeque<Node>();
		Node node = new Node(0);
		parentstack.push(root);
		while(!parentstack.isEmpty()){
			node = parentstack.pop();
			//visit(node);
			System.out.print(node.key + "  ");
			if(node.right != nil){
				parentstack.push(node.right);
			}
			if(node.left != nil){
				parentstack.push(node.left);
			}
		}
	}
	
	public void inOrder(Node root){
		Deque<Node> parentstack = new ArrayDeque<Node>();
		Node node = new Node(0);
		node = root;
		while(!parentstack.isEmpty() || node != nil){
			if(node != nil){
				parentstack.push(node);
				node = node.left;
			}
			else{
				node = parentstack.pop();
				//visit(node);
				System.out.print(node.key + "  ");
				node = node.right;
			}
		}
	}
	
	public void postOrder(Node root){
		Deque<Node> parentstack = new ArrayDeque<Node>();
		Node node = new Node(0);
		node = root;
		Node lastnodevisited = new Node(0);
		lastnodevisited = nil;
		Node peeknode = new Node(0);
		while(!parentstack.isEmpty() || node != nil){
			if(node != nil){
				parentstack.push(node);
				node = node.left;
			}
			else{
				peeknode = parentstack.peek();
				if(peeknode.right != nil && lastnodevisited != peeknode.right){
					node = peeknode.right;
				}
				else{
					parentstack.pop();
					//visit(peeknode);
					System.out.print(peeknode.key + "  ");
					lastnodevisited = peeknode;
				}
			}
		}
	}
	
	//print tree properties
	
	public void printTreeKey(Node root){
		Queue<Node> tempqueue = new LinkedList<Node>();
		Queue<Node> pollingqueue = new LinkedList<Node>();
		Queue<Node> addingqueue = new LinkedList<Node>();
		Node node = new Node(0);
		node = root;
		pollingqueue.add(root);
		while(!pollingqueue.isEmpty() || !addingqueue.isEmpty()){
			if(!pollingqueue.isEmpty()){
				node = pollingqueue.poll();
				if(node.left != nil){
					System.out.print("/");
				}
				System.out.print(node.key);
				if(node.right != nil){
					System.out.print("\\");
				}
				System.out.print("  ");
				if(node.left != nil){
					addingqueue.add(node.left);
				}
				if(node.right != nil){
					addingqueue.add(node.right);
				}

			}
			else{
				tempqueue = pollingqueue;
				pollingqueue = addingqueue;
				addingqueue = tempqueue;
				System.out.println();
			}
		}
		System.out.println();
		System.out.println("printTreeKey completed!");
	}
	
	public void printTreeColor(Node root){
		Queue<Node> tempqueue = new LinkedList<Node>();
		Queue<Node> pollingqueue = new LinkedList<Node>();
		Queue<Node> addingqueue = new LinkedList<Node>();
		Node node = new Node(0);
		node = root;
		pollingqueue.add(root);
		while(!pollingqueue.isEmpty() || !addingqueue.isEmpty()){
			if(!pollingqueue.isEmpty()){
				node = pollingqueue.poll();
				if(node.left != nil){
					System.out.print("/");
				}
				System.out.print(node.color);
				if(node.right != nil){
					System.out.print("\\");
				}
				System.out.print("  ");
				if(node.left != nil){
					addingqueue.add(node.left);
				}
				if(node.right != nil){
					addingqueue.add(node.right);
				}

			}
			else{
				tempqueue = pollingqueue;
				pollingqueue = addingqueue;
				addingqueue = tempqueue;
				System.out.println();
			}
		}
		System.out.println();
		System.out.println("printTreeColor completed!");
	}
	
	
	

}
