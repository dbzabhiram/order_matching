public class Node{
	int price;
	int id;
	int height;
	Node left, right;

	Node(int price, int id){
		this.price = price;
		this.id = id;
		height = 1;
	}
}