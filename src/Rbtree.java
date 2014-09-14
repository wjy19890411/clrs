
public class Rbtree extends Tree{

	/**
	 * @param args
	 */
	//insert z as a red/0, fix later
	public void rbinsert(Rbtree T, Node z){
		Node y = new Node(0);
		Node x = new Node(0);
		y = T.nil;
		x = T.root;
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
			if(x == y.left){
				y.left = z;
			}
			else{
				y.right = z;
			}
		}
		z.left = T.nil;
		z.right = T.nil;
		z.color = 0;
		rbinsertfixup(T, z);
	}
	
	public void rbinsertfixup(Rbtree T, Node z){
		Node y = new Node(0);	//y is z's uncle
		while(z.parent.color == 0){
			if(z.parent == z.parent.parent.left){
				y = z.parent.parent.right;
				if(y.color == 0){
					z.parent.color = 1;
					y.color = 1;
					z.parent.parent.color = 0;
					z = y.parent;
				}
				else{
					if(z == z.parent.right){
						T.leftrotate(T, z.parent);
						z = z.left;
					}
					z.parent.color = 1;
					z.parent.parent.color = 0;
					T.rightrotate(T, z.parent.parent);
				}
				
			}
			else{
				y = z.parent.parent.left;
				if(y.color == 0){
					z.parent.color = 1;
					y.color = 1;
					z.parent.parent.color = 0;
					z = y.parent;
				}
				else{
					if(z == z.parent.left){
						T.rightrotate(T, z.parent);
					}
					z.parent.color = 1;
					z.parent.parent.color = 0;
					T.leftrotate(T, z.parent.parent);
				}
			}
			
		}
		
		
	}

	
	public void rbdelete(Rbtree T, Node z){
		//yoricol is col of critical position, x is new node in critical position,
		//the node to be double black
		Node y = new Node(0);
		Node x = new Node(0);
		int yoricol = 0;
		//z have no left, CP is z
		if(z.left == nil){
			y = z;
			//x might be nil
			x = z.right;
			yoricol = z.color;
			T.transplant(T, z, z.right);
		}
		//z have left, but no right, CP is z
		else if(z.right == nil){
			y = z;
			//x not nil
			x = z.left;
			yoricol = z.color;
			T.transplant(T, z, z.left);
		}
		//z have left and right, CP is min of z.right
		else{
			
			y = T.min(z.right);
			//x might nil if z.right is 1 node
			x = y.right; 
			yoricol = y.color;
			y.color = z.color;
			//z.right have more than 1 nodes
			if(y != z.right){
				T.transplant(T, y, y.right);
				y.right = z.right;
				z.right.parent = y;
			}
			else{
				x.parent = y;
			}
			T.transplant(T, z, y);
			z.left.parent = y;
			y.left = z.left;
			 
			
		}
		if (yoricol == 1)
			rbdeletefixup(T, x);
	}
	
	
	public void rbdeletefixup(Rbtree T, Node x){
		
		//now x is black, x is not root
		//x is left child
		Node w = new Node(0); //x's sibling
		while(x.color == 1 && x != T.root){
			if(x == x.parent.left){
				w = x.parent.right;
				//case 1
				if(w.color == 0){
					x.parent.color = 0;
					w.color = 1;
					T.leftrotate(T, x.parent);
					continue;
				}
				//case 2
				if(w.left.color == 1 && w.right.color == 1){
					w.color = 0;
					x = x.parent;
					continue;
				}
				//case 3
				if(w.right.color == 1){
					w.color = 0;
					w.left.color = 1;
					T.rightrotate(T, w);
					continue;
				}
				//case 4
				w.color = x.parent.color;
				x.parent.color = 1;
				w.right.color = 1;
				T.leftrotate(T, x.parent);
				return;
				
			}
			else{
				w = x.parent.left;
				//case 1
				if(w.color == 0){
					x.parent.color = 0;
					w.color = 1;
					T.rightrotate(T, x.parent);
					continue;
				}
				//case 2
				if(w.right.color == 1 && w.left.color == 1){
					w.color = 0;
					x = x.parent;
					continue;
				}
				//case 3
				if(w.left.color == 1){
					w.color = 0;
					w.right.color = 1;
					T.leftrotate(T, w);
					continue;
				}
				//case 4
				w.color = x.parent.color;
				x.parent.color = 1;
				w.left.color = 1;
				T.rightrotate(T, x.parent);
				return;
				
			}
			
		}
		x.color = 1;
	}
	
	
	
	
	
	
	
	
}
