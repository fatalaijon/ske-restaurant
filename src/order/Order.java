package order;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Encapsulate information about a customer order.
 * 
 * @author Fatalai Jon
 */
public class Order {
	/** order number, should be unique. */
	private long orderNumber = 0L; // no order number yet
	/** date and time the order was completed and sent to Restaurant. */
	private LocalDateTime timestamp;
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
		// nothing in the order yet
		java.util.Arrays.fill(items, 0);
	}
	
	/**
	 * Add a quantity of some menu item to this order.
	 * @param id the id number of menu item to add
	 * @param quantity number of units to add
	 * @return true if added, false otherwise.
	 */
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
	 * @param id the id number of item to remove
	 */
	public void removeItem(int id) {
		if (id < 0 || id >= items.length) {
			System.err.println("removeItem: invalid item number "+id);
			return;
		}
		items[id] = 0;
	}
	
	/**
	 * Remove quantity of item from order.
	 * @param id the id number of menu item to remove
	 * @param quantity how many units to remove.  If there are not
	 * that many units in the order, then all units in order are removed.
	 * Never remove more than the actual quantity in order.
	 * 
	 */
	public void removeItem(int id, int quantity) {
		if (id < 0 || id >= items.length) {
			System.err.println("removeItem: invalid item number "+id);
			return;
		}
		items[id] = Math.max(0, items[id] - quantity);
	}
	
	/**
	 * Get the quantity of a specific item in the order.
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
	
	/**
	 * Compute and return the total price of this order.
	 * @return the total price of order
	 */
	public double getTotal() {
		double total = 0;
		for(int k=0; k<items.length; k++) total += items[k]*prices[k];
		//TODO apply any discounts
		
		return total;
	}
	
	/** Test if the order is empty. */
	public boolean isEmpty() {
		for(int qnty: items) if (qnty > 0) return false;
		return true;
	}
	
	/** Get the ITEM IDs of menu items in this order. */
	public int[] getItems() {
		int[] itemIds = new int[items.length]; // worse case -- every item on menu is in order
		int count = 0;
		for(int k=0; k<items.length; k++) if (items[k] > 0) itemIds[count++] = k;
		// trim array to correct size by copying it
		return Arrays.copyOf(itemIds, count); 
	}

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public void setTimeStamp( LocalDateTime time ) {
		this.timestamp = time;
	}
	
	public LocalDateTime getTimeStamp() {
		return timestamp;
	}
}
