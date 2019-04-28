public class Order{
	private int issuer_id;
	private String type;
	private int quantity;
	private int price;

	public Order(int id, String type, int q, int p){
		this.type = type;
		issuer_id = id;
		quantity = q;
		price = p;
	}

	public String getType(){
		return type;
	}

	public int getIssuer(){
		return issuer_id;
	}

	public int getQuantity(){
		return quantity;
	}

	public int getPrice(){
		return price;
	}

	public int hashCode(){
		return (type + (""+quantity+price+issuer_id)).hashCode();
	}

	public boolean equals(Object o){
		if(o instanceof Order){
			Order other = (Order) o;
			return (type.equals(other.type) && (quantity == other.quantity) && (price == other.price) && (issuer_id == other.issuer_id));
		}
		return false;
	}
}