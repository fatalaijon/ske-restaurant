
import java.util.Scanner;

import order.Order;
import restaurant.RestaurantManager;

/**
 * A console based interface for placing an order
 * at a restaurant.
 * 
 * @author Fatalai Jon
 *
 */
public class RestaurantUI {
	/** Names of items on the menu. */
	private String[] items = null;
	/** Prices of the items, in same order as item names. */
	private double[] prices = null;
	/** Parse input from console. */
	static final Scanner console = new Scanner(System.in);
	/** The RestaurantManager, for getting menu info and submitting order. */
	private final RestaurantManager rm;

	// **Dependency Injection**
	// RestaurantUI depends on RestaurantManager.
	// Instead of creating a new RestaurantManager here, 
	// we let some other object "give" us a reference to
	// the RestaurantManager.
	// This is called "Dependency Injection" - you inject (set)
	// a reference to a dependency.
	
	/**
	 * Initialize (but don't start) the UI.
	 * @param rm a reference to the RestaurantManager.
	 */
	public RestaurantUI(RestaurantManager rm) {
		this.rm = rm;
		// items and prices initialized in consoleUI method.
	}
	
	/**
	 * Accept orders at console, in a loop.
	 */
	public void consoleUI() {
		while(true) {
			Order order = acceptOrder();	
			if (order == null || order.isEmpty()) continue;
			// pay and submit
			acceptPayment(order);
			submitOrder(order);
			printReceipt(order);
		}
	}
	
	/** Display the menu. */
	public void printMenu() {
		if (items == null) initMenu();
		// format used for printf of menu
		String format = "[%2d] %-24.24s %,6.2f%n";
		// Item 0 is not used so that item numbers start at 1.
		for(int k=1; k<items.length; k++) {
			System.out.printf(format, k, items[k], prices[k]);
		}
		// add a blank line for readability
		System.out.println();
	}
	
	/** Display other commands. */
	public void printCommands() {
		int itemcount = items.length - 1; // don't use element [0]
		String choiceformat = "%-6.6s %s%n";
		System.out.printf(choiceformat, "1-"+itemcount, "Add item# to order");
		System.out.printf(choiceformat, "m", "Display menu");
		System.out.printf(choiceformat, "p", "Print contents of order");
		System.out.printf(choiceformat, "s", "checkout and Submit Order");
		System.out.printf(choiceformat, "x", "Cancel order");
		System.out.printf(choiceformat, "Q", "Quit (capital 'Q')");
	}
	
	
	
	/** 
	 * Accept customer order in a loop. 
	 * @return a completed order or null if order is cancelled.
	 */
	protected Order acceptOrder( ) {
		// make sure we have the menu data
		if (items == null) initMenu();
		// create an array for customer's order.
		Order order = new Order(items, prices);
		
		while(true) {
			String choice = getReply("Enter item# to order or command (? for help): ");
			switch(choice) {
			case "?":
				printCommands();
				break;
			case "m":
			case "M":
				printMenu();
				break;
			case "p":
			case "P":
				displayOrder(order);
				break;
			case "x":
			case "X":
				if ( cancelOrder(order) ) {
					System.out.println("Order cancelled.");
					return null;
				}
				break;
			case "s":
			case "S":
				// return the order.  The caller will submit it.
				return order;
			case "Q":
				quit();
				return null;
			default:
				// anything else should be an item number
				int itemNumber = 0;
				try {
					itemNumber = Integer.parseInt(choice);
					if (itemNumber >= 0 && itemNumber < items.length) {
						
						if (order.addItem(itemNumber, 1)) {
							System.out.printf("Added %s.  Total quantity: %d\n", items[itemNumber], order.getQuantityOfItem(itemNumber));
						}
						else {
							System.out.printf("Failed to add item %d (%s)\n", itemNumber, items[itemNumber]);
						}
					}
					else System.out.println("Invalid choice "+choice);
				} catch(NumberFormatException nfe) {
					System.out.println("Invalid choice "+choice);
				}
			}
		}
	}

	/** Show contents of customer's order. */
	private void displayOrder(Order order) {
		boolean hasItems = false;
		System.out.printf("Item# %-24.24s %4s   %5s%n","Description", "Qnty", "Price");
		for(int k=0; k<items.length; k++) {
			int qnty = order.getQuantityOfItem(k);
			if (qnty != 0) {
				double itemPrice = qnty*prices[k]; 
				hasItems = true;
				System.out.printf("%3d   %-24.24s  %3d  %,7.2f%n", k, items[k], qnty, itemPrice);
			}
		}
		if (hasItems) {
			double total = order.getTotal();
			System.out.printf("      %-24.24s       %,7.2f%n", "Total Price", total);
			System.out.println();
		}
		else
			System.out.println("No items in order");
	}
	
	/**
	 * Accept payment for an order.
	 * @param order a customer order to get payment for
	 * @precondition the order is not null
	 */
	protected void acceptPayment(Order order) {
		System.out.printf("Total amount %,.2f\n", order.getTotal());
	}
	
	protected void submitOrder(Order order) {
		rm.recordOrder( order );
		System.out.println("Order Submitted.  Your order number is "+order.getOrderNumber());
		System.out.println("Thank you for your order.\n");
		
	}
	/**
	 * Confirm action to cancel an order,
	 * if order is not empty.
	 * @param order the order to cancel
	 * @return true if OK to cancel this order.
	 */
	private boolean cancelOrder(Order order) {
		if (order == null || order.isEmpty()) return true;
		int itemCount = 0;
		for(int id : order.getItems()) itemCount += order.getQuantityOfItem(id);
		String confirm = getReply(String.format("Order contains %d items.  Really cancel (y/n)? ", itemCount) );
		if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) return true;
		return false;
	}
	
	/** Initialize the menu data, using RestaurantManager. */
	private void initMenu() {
		items = rm.getMenuItems();
		prices = rm.getPrices();
	}
	
	private String getReply(String prompt) {
		System.out.print(prompt);
		return console.nextLine().trim();
	}
	
	public void printReceipt(Order order) {
		System.out.println(rm.getRestaurantName());
		System.out.printf("Order No:   %d\n", order.getOrderNumber());
		System.out.printf("Date/Time:  %tT\n", order.getTimeStamp());
		System.out.println();
		displayOrder(order);
	}
	
	public void quit() {
		if (rm != null) rm.shutdown();
		System.exit(0);
	}
}
