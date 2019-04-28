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

	static Node insert(Node root, int price, int order_id, int quantity){
		if(root == null)
			return new Node(price, order_id, quantity);

		if(price < root.price)
			root.left = insert(root.left, price, order_id, quantity);
		else if(price >= root.price)
			root.right = insert(root.right, price, order_id, quantity);
		
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

	static Node lowestSell(Node node){
		if(node == null)
			return null;

		Node current = node;  
  
        while (current.left != null)  
	        current = current.left;  
  
        return current;
	}

	static Node highestBuy(Node node){
		if(node == null)
			return null;

		Node current = node;
  
        while (current.right != null)  
	        current = current.right;
  
        return current;
	}

	static Node lowNode(Node node, int price){//, int issuer, HashMap<Integer, Order> hmOrder){
		if(node == null)
			return null;

		Node current = node;
  
        while(current.left != null){
			if(current.left.price < current.price){ //&& issuer != hmOrder.get(current.left.id).getIssuer()){
				current = current.left;
			}
		} 

		if(current.price > node.price)
			return null;
  
        return current;
	}

	static void executeSell(Node node, int nodeOrderID, HashMap<Integer, Order> hmOrder, HashMap<Integer, Issuer> hmIss){
		Node lowestPoss = node;

		Order execute_order = (Order) hmOrder.get(nodeOrderID);
		int exe_issuer = execute_order.getIssuer();
		int exe_price = execute_order.getPrice();
		int exe_quant = execute_order.getQuantity();

		//Buying coins at lowest possible value
		Node lowest = lowNode(node, exe_price);

		if(lowest == null)
			return;
		else{
			int lowQuan = lowestPoss.quantity;
			int lowPrice = lowestPoss.price;
			int lowOrderID = lowestPoss.id;

			int lowOrderIss = hmOrder.get(lowOrderID).getIssuer();
			
			Issuer lowIssuer = hmIss.get(lowOrderID);
			String lowIssName = lowIssuer.name;
			int lowIssBal = lowIssuer.balance;
			int lowIssQuan = lowIssuer.quantity;

			Issuer i = hmIss.get(exe_issuer);
			String name = i.name;
			int balance = i.balance;
			int quantity = i.quantity;

			hmIss.replace(exe_issuer, new Issuer(name, balance - lowPrice*lowQuan, quantity + lowQuan));
			hmIss.replace(lowOrderIss, new Issuer(lowIssName, lowIssBal + lowPrice*lowQuan, lowIssQuan - lowQuan));

			if(lowQuan > exe_quant){
				hmOrder.remove(nodeOrderID);
				hmOrder.replace(lowOrderID, new Order(lowOrderIss, "sell", lowQuan - exe_quant, lowPrice));
			} else if(lowQuan == exe_quant){
				hmOrder.remove(lowOrderID);
				hmOrder.remove(nodeOrderID);
			} else{
				hmOrder.remove(lowOrderID);
				hmOrder.replace(nodeOrderID, new Order(exe_issuer, "buy", exe_quant - lowQuan, exe_price));
				executeSell(node, nodeOrderID, hmOrder, hmIss);
			}
		}
	}

	static void preOrder(Node node){
		if(node != null){
			System.out.println(node.price + " ");
			preOrder(node.left);
			preOrder(node.right);
		}
	}
}
