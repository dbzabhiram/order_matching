import java.io.*;
import java.util.*;

public class OrderMatching{
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		//Account details
		HashMap<Integer, Issuer> account_book = new HashMap<>();

		System.out.println("Enter number of issuers:");
		int numOfIssuer = Integer.parseInt(br.readLine());

		System.out.println("Name, quantity of coins and balance of each issuer respectively:");

		for(int i = 0; i < numOfIssuer; i++){
			String[] issuerArgs = br.readLine().split(" ");
			String issuer_name = issuerArgs[0];
			int issuer_coin = Integer.parseInt(issuerArgs[1]);
			int issuer_bal = Integer.parseInt(issuerArgs[2]);

			account_book.put((i+1), new Issuer(issuer_name, issuer_coin, issuer_bal));
		}

		//Order Details
		HashMap<Integer, Order> orderbook = new HashMap<>();
		int order_id = 0;

		//Setup AVL Trees according to price and order_id's
		AVLTree buyTree = new AVLTree();
		AVLTree sellTree = new AVLTree();

		System.out.println("Welcome to the exchange, what would you like to do:");
		System.out.println("1. Add order\n2. Cancel all order\n3. Cancel order\n"+
				 			"4. Execute order\n5. Get Lowest Sell\n6. Get Highest Buy\n"+
				 			"7. Get Issuer ID\n8. Final Balance");

		for(int i_loop=0; i_loop<5; i_loop++){
			System.out.println("Select option:");
			int option = Integer.parseInt(br.readLine());

			switch(option){
				case 1: System.out.println("What is the type of order (buy/sell)?:");
						String order_type = br.readLine();
						System.out.println("Enter issuer id, quantity and price:");
						String[] args1 = br.readLine().split(" ");
						int issuer_id = Integer.parseInt(args1[0]);
						int quantity = Integer.parseInt(args1[1]);
						int price = Integer.parseInt(args1[2]);

						if(order_type.toLowerCase().equals("b") || order_type.toLowerCase().equals("buy")){
							order_id++;
							orderbook.put(order_id, new Order(issuer_id, "buy", quantity, price));
							//Setup the tree
							buyTree.root = buyTree.insert(buyTree.root, price, order_id);
						} else if(order_type.toLowerCase().equals("s") || order_type.toLowerCase().equals("sell")){
							order_id++;
							orderbook.put(order_id, new Order(issuer_id, "sell", quantity, price));
							//Setup the tree
							sellTree.root = sellTree.insert(sellTree.root, price, order_id);
						} else {
							System.out.println("No such order type present.");
						}

						System.out.println("Order ID: " + order_id);
						break;

				case 2: orderbook.clear();
						//Setup clear function
						buyTree.root = null;
						sellTree.root = null;
						break;

				case 3: System.out.println("Id of cancelling order: ");
						int cancel_id = Integer.parseInt(br.readLine());
						Order cancel_order = (Order) orderbook.get(cancel_id);
						if(cancel_order.getType().equals("buy")) buyTree.root = buyTree.remove(buyTree.root, cancel_order.getPrice(), cancel_id);
						else sellTree.root = sellTree.remove(sellTree.root, cancel_order.getPrice(), cancel_id);

						orderbook.remove(cancel_id);
						break;

				//Write execute function atlast
				case 4: System.out.println("Id of executing order: ");
						int execute_id = Integer.parseInt(br.readLine());
						//Write here, need to update all orderbook, account_book, buy tree and sell tree.
						break;

				case 5: System.out.println("Lowest sell available: " + sellTree.lowestSell(sellTree.root));
						break;

				case 6: System.out.println("Highest buy available: " + buyTree.highestBuy(buyTree.root));
						break;

				case 7: printIds(account_book);
						break;

				//Update each balance account after execution of orders
				case 8: break;

				default: System.out.println("Wrong option selected!");
			}
		}
	}

	static void printIds(HashMap<Integer, Issuer> hm){
		for(Map.Entry<Integer, Issuer> entry: hm.entrySet()){
			Issuer i = entry.getValue();
			System.out.println(entry.getKey()+" "+i.name);
		}
	}
}