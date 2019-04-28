public class Issuer{
	String name;
	int balance;
	int quantity;

	Issuer(String name, int bal, int q){
		this.name = name;
		balance = bal;
		quantity = q;
	}

	public int hashCode(){
		return (name + (""+quantity+balance)).hashCode();
	}

	public boolean equals(Object o){
		if(o instanceof Issuer){
			Issuer other = (Issuer) o;
			return (name.equals(other.name) && (quantity == other.quantity) && (balance == other.balance));
		}
		return false;
	}
}