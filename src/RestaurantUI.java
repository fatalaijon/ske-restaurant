
import java.util.Scanner;

/**
 * A console based interface for placing an order
 * at a restaurant.
 * 
 * @author Fatalai Jon
 *
 */
public class RestaurantUI {
	/** Names of items on the menu. */
	private String[] items = {};
	/** Prices of the items, in same order as item names. */
	private double[] prices = {};
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
	
	/** Customer's order is the quantity of each item.  Initially the quantities are 0. */
	
	/** Display the menu. */
	public void printMenu() {
		// format used for printf of menu
		String format = "%2d) %-24.24s %,6.2f%n";
		for(int k=0; k<items.length; k++) {
			System.out.printf(format, k+1, items[k], prices[k]);
		}
	}
	
	/** Display other commands. */
	public void printCommands() {
		// special items
		String choiceformat = "%2.2s) %s%n";
		System.out.printf(choiceformat, "m", "Show menu");
		System.out.printf(choiceformat, "s", "Show order");
		System.out.printf(choiceformat, "c", "Checkout and Submit Order");
		System.out.printf(choiceformat, "0", "Quit");
	}
	
	public String getChoice() {
		System.out.print("Enter choice (? for help): ");
		return console.next();
	}
	
	public void quit() {
		System.out.println("Goodbye");
		System.exit(0);
	}
	
	/** Accept customer order in a loop. */
	public void consoleUI( ) {
		// Get the menu and price data. Do it here to make sure 
		// we get up-to-date values.
		items = rm.getMenuItems();
		prices = rm.getPrices();
		// create an array for customer's order.
		int[] order = new int[items.length];
		// initial quantity of each item is zero
		java.util.Arrays.fill(order, 0);
		
		while(true) {
			String choice = getChoice();
			switch(choice) {
			case "?":
				printCommands();
				break;
			case "m":
				printMenu();
				break;
			case "s":
				displayOrder(order);
				break;
			case "c":
				displayOrder(order);
				// continue to quit...
			case "0":
				quit();
				break;
			default:
				// anything else should be an item number
				int itemNumber = 0;
				try {
					itemNumber = Integer.parseInt(choice);
					if (itemNumber >= 0 && itemNumber < items.length)
						order[itemNumber-1] += 1;
					else System.out.println("Invalid choice "+choice);
				} catch(NumberFormatException nfe) {
					System.out.println("Invalid choice "+choice);
				}
			}
		}
	}

	/** Show contents of customer's order. */
	private void displayOrder(int[] order) {
		double total = 0;
		boolean hasItems = false;
		System.out.printf("Item# %-24.24s %4s   %5s%n","Description", "Qnty", "Price");
		for(int k=0; k<order.length; k++) {
			if (order[k] != 0) {
				double itemPrice = order[k]*prices[k]; 
				hasItems = true;
				System.out.printf("%3d   %-24.24s  %3d  %,7.2f%n", k+1, items[k], order[k], itemPrice);
				total += itemPrice;
			}
		}
		if (hasItems) {
			System.out.printf("      %-24.24s       %,7.2f%n", "Total Price", total);
			System.out.println();
		}
		else
			System.out.println("No items in order");
	}

}
