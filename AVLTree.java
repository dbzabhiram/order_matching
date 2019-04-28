import java.util.*;

public class AVLTree{
	static Node root;

	AVLTree(){
		root = null;
	}

	static int height(Node N){
		if(N == null)
			return 0;

		return N.height;
	}

	static int getBalance(Node N){
		if(N == null)
			return 0;

		return height(N.left) - height(N.right);
	}

	static Node leftRotate(Node x){
		Node y = x.right;
		Node T2 = y.left;

		y.left = x;
		x.right = T2;

		x.height = 1 + Math.max(height(x.left), height(x.right));
		y.height = 1 + Math.max(height(y.left), height(y.right));

		return y;
	}

	static Node rightRotate(Node y){
		Node x = y.left;
		Node T2 = x.right;

		x.right = y;
		y.left = T2;

		y.height = 1 + Math.max(height(y.left), height(y.right));
		x.height = 1 + Math.max(height(x.left), height(x.right));

		return x;
	}

	static Node insert(Node root, int price, int order_id){
		if(root == null)
			return new Node(price, order_id);

		if(price < root.price)
			root.left = insert(root.left, price, order_id);
		else if(price >= root.price)
			root.right = insert(root.right, price, order_id);
		
		root.height = 1 + Math.max(height(root.left), height(root.right));

		int balance = getBalance(root);

		//Left-left rotation
		if(balance > 1 && price < root.left.price)
			return leftRotate(root);
		
		//Right-right rotation
		if(balance < -1 && price > root.right.price)
			return rightRotate(root);

		//Left-right rotation
		if(balance < -1 && price > root.left.price){
			root.left = leftRotate(root.left);
			return rightRotate(root);
		}

		if(balance > 1 && price < root.right.price){
			root.right = rightRotate(root.right);
			return leftRotate(root);
		}

		return root;
	}

	static Node minValueNode(Node node){
		Node current = node;  
  
        while (current.left != null)  
	        current = current.left;  
  
        return current;
	}

	static Node remove(Node root, int price, int order_id){
		if(root == null)
			return root;

		if(price < root.price)
			root.left = remove(root.left, price, order_id);
		else if(price > root.price)
			root.right = remove(root.right, price, order_id);
		else{
			if(root.id != order_id) root.right = remove(root.right, price, order_id);
			else{
				if((root.left == null) || (root.right == null)){
					Node temp = null;

					if(root.left == null)
						temp = root.right;
					else
						temp = root.left;

					if(temp == null){
						temp = root;
						root = null;
					} else root = temp;
				} else{
					Node nextNode = minValueNode(root.right);

					root.price = nextNode.price;
					root.id = nextNode.id;

					root.right = remove(root.right, root.price, root.id);
				}
			}
		}

		if(root == null)
			return root;

		root.height = 1 + Math.max(height(root.right), height(root.left));

		int balance = getBalance(root);

		if(balance > 1 && getBalance(root.left) >=0)
			return rightRotate(root);

		if (balance > 1 && getBalance(root.left) < 0){  
            root.left = leftRotate(root.left);  
            return rightRotate(root);  
        }  
  
        if (balance < -1 && getBalance(root.right) <= 0)  
            return leftRotate(root);  
  
        if (balance < -1 && getBalance(root.right) > 0){  
            root.right = rightRotate(root.right);  
            return leftRotate(root);  
        }  
  
        return root;
	}

	static int lowestSell(Node node){
		if(node == null)
			return 0;

		Node current = node;  
  
        while (current.left != null)  
	        current = current.left;  
  
        return current.price;
	}

	static int highestBuy(Node node){
		if(node == null)
			return 0;

		Node current = node;
  
        while (current.right != null)  
	        current = current.right;
  
        return current.price;
	}

	static void preOrder(Node node){
		if(node != null){
			System.out.println(node.price + " ");
			preOrder(node.left);
			preOrder(node.right);
		}
	}
}