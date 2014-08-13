
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

	
	
	
	
	
	
	
	
	
	
	
	
	
}
