package order;
/**
 * This class represents one item on a customer's order.
 * It can be a food item or something else (like a discount).
 * 
 * For efficient coding, the attributes can be accessed
 * using either get/set methods (except id, which is final)
 * or directly as fields.  For example:
 * 
 * item.setQuantity( item.getQuantity() + 1 );
 * or
 * item.quantity += 1;
 * 
 * @author Fatalai Jon
 *
 */
public class OrderItem {
	/** Item id cannot be changed. */
	protected final long id;
	protected String name;
	/** price is the unit price for 1 of this item. */
	protected double price;
	protected int quantity;
	
	/**
	 * Initialize a new Order Item.
	 */
	public OrderItem(long id, String name, double price) {
		this(id, name, price, 0);
	}
	
	public OrderItem(long id, String name, double price, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	

}
