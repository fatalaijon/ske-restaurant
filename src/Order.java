/**
 * Encapsulate information about a customer order.
 * 
 * @author Fatalai Jon
 */
public class Order {
	/** quantities of items in the order. */
	private int[] items;
	/** reference to the items and prices. */
	private String[] menuItems;
	private double[] prices;
	
	/**
	 * Initialize a new order.
	 */
	public Order(String[] menuItems, double[] prices ) {
		this.menuItems = menuItems;
		this.prices = prices;
		items = new int[menuItems.length];
		java.util.Arrays.fill(items, 0);
	}
	
	public boolean addItem(int id, int quantity) {
		if (quantity <= 0) return false; // invalid
		if (id < 0 || id >= items.length) {
			System.err.println("addItem: invalid item number "+id);
			return false;
		}
		items[id] += quantity;
		return true;
	}
	
	/**
	 * Remove all units of an item from the order.
	 */
	public void removeItem(int id) {
		if (id < 0 || id >= items.length) {
			System.err.println("removeItem: invalid item number "+id);
			return;
		}
		items[id] = 0;
	}
	
	/**
	 * Remove quantity of item from order
	 */
	public void removeItem(int id, int quantity) {
		if (id < 0 || id >= items.length) {
			System.err.println("removeItem: invalid item number "+id);
			return;
		}
		items[id] = Math.max(0, items[id] - quantity);
	}
	
	/**
	 * Get the quantity of a specific item
	 * @param id the id of item to get
	 * @return quantity of requested item in the order
	 */
	public int getQuantityOfItem(int id) {
		if (id < 0 || id >= items.length) {
			System.err.println("removeItem: invalid item number "+id);
			return 0;
		}
		return items[id];
	}
	
	public double getTotal() {
		double total = 0;
		for(int k=0; k<items.length; k++) total += items[k]*prices[k];
		//TODO apply any discounts
		
		return total;
	}
	
}
