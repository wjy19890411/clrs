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
	
	public Node search(Node x, int key){
		if(x == nil || x.key == key){
			return x;
		}
		if(key < x.key){
			return search(x.left, key);
		}
		else{
			return search(x.right, key);
		}
	}
	
	public Node min(Node x){
		while(x.left != nil){
			x = x.left;
		}
		return x;
	}
	
	public Node max(Node x){
		while(x.right != nil){
			x = x.right;
		}
		return x;
	}
	
	public Node successor(Node x){
		if(x.right != nil){
			return min(x.right);
		}
		else{
			Node y = new Node(0);
			y = x.parent;
			while(y != nil && x == y.right){
				x = y;
				y = x.parent;
			}
			return y;
		}
	}
	
	public Node predecessor(Node x){
		if(x.left != nil){
			return max(x.left);
		}
		else{
			Node y = new Node(0);
			y = x.parent;
			while(y != nil && x == y.left){
				x = y;
				y = x.parent;
			}
			return y;
		}
	}
	
	public void insert(Tree T, Node z){
		z.parent = nil;
		z.left = nil;
		z.right = nil;
		Node y = new Node(0);
		Node x = new Node(0);
		x = T.root;
		y = nil;
		while(x != nil){
			y = x;
			if(z.key < x.key){
				x = x.left;
			}
			else{
				x = x.right;
			}
		}
		z.parent = y;
		if(y == nil){
			T.root = z;
		}
		else{
			if(z.key < y.key){
				y.left = z;
			}
			else{
				y.right = z;
			}
		}
	}
	
	//replace subtree u with v, u can't be nil, v can be nil
	public void transplant(Tree T, Node u, Node v){
		if(u.parent == nil){
			T.root = v;
		}
		else{
			if(u == u.parent.left){
				u.parent.left = v;
			}
			else{
				u.parent.right = v;
			}
		}
		v.parent = u.parent;
		return;
	}
	
	public void delete(Tree T, Node z){
		if(z.left == nil){
			transplant(T, z, z.right);
			return;
		}
		else if(z.right == nil){
			transplant(T, z, z.left);
			return;
		}
		else{
			Node y = new Node(0);
			y = min(z.right);
			if(y != z.right){
				transplant(T, y, y.right);
				y.right = z.right;
				z.right.parent = y;
			}
			transplant(T, z, y);
			y.left = z.left;
			y.left.parent = y;
		}
		
	}
	
	//x.right != nil
	public void leftrotate(Tree T, Node x){
		Node y = new Node(0);
		y = x.right;
		if(x.parent == nil){
			T.root = y;
		}
		else{
			if(x == x.parent.left){
				x.parent.left = y;
			}
			else{
				x.parent.right = y;
			}
		}
		y.parent = x.parent;
		if(y.left != nil){
			y.left.parent = x;
		}
		x.right = y.left;
		x.parent = y;
		y.left = x;
	}
	
	public void rightrotate(Tree T, Node x){
		Node y = new Node(0);
		y = x.left;
		y.parent = x.parent;
		if(x.parent == nil){
			T.root = y;
		}
		else{
			if(x == x.parent.left){
				x.parent.left = y;
			}
			else{
				x.parent.right = y;
			}
		}
		x.left = y.right;
		if(y.right != nil){
			y.right.parent = x;
		}
		y.parent = x.parent;
		y.right = x;
		x.parent = y;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
