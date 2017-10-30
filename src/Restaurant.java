
import java.util.Scanner;

/**
 * Homework 1: a console based interface for placing an order
 * at a restaurant.  Students don't know objects yet, so don't
 * use objects.
 * 
 * @author Fatalai Jon
 *
 */
public class Restaurant {
	/** Names of items on the menu. */
	String[] items;
	/** Prices of the items, in same order as item names. */
	double[] prices;
	/** Parse input from console. */
	static final Scanner console = new Scanner(System.in);

	public Restaurant() {
		RestaurantManager rm = RestaurantManager.getInstance();
		items = rm.getMenuItems();
		prices = rm.getPrices();
	}
	
	/** Customer's order is the quantity of each item.  Initially the quantities are 0. */
	
	public void printMenu() {
		// format used for printf of menu
		String format = "%2d) %-20.20s %,6.2f%n";
		for(int k=0; k<items.length; k++) {
			System.out.printf(format, k+1, items[k], prices[k]);
		}
		// special items
		String choiceformat = "%2.2s) %s%n";
		System.out.printf(choiceformat, "s", "Show order");
		System.out.printf(choiceformat, "c", "Checkout and quit");
		System.out.printf(choiceformat, "0", "Quit");
	}
	
	public String getChoice() {
		System.out.print("Enter choice: ");
		return console.next();
	}
	
	public void quit() {
		System.out.println("Goodbye");
		System.exit(0);
	}
	
	/** Accept customer order in a loop. */
	private void consoleUI( ) {
		// create an array for customer's order.
		int[] order = new int[items.length];
		// initial quantity of each item is zero
		java.util.Arrays.fill(order, 0);
		
		while(true) {
			printMenu();
			String choice = getChoice();
			switch(choice) {
			case "s":
				showOrder(order);
				break;
			case "c":
				showOrder(order);
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
	private void showOrder(int[] order) {
		double total = 0;
		boolean hasItems = false;
		System.out.printf("Item# %-20.20s %4s   %5s%n","Description", "Qnty", "Price");
		for(int k=0; k<order.length; k++) {
			if (order[k] != 0) {
				double itemPrice = order[k]*prices[k]; 
				hasItems = true;
				System.out.printf("%3d   %-20.20s  %3d  %,7.2f%n", k+1, items[k], order[k], itemPrice);
				total += itemPrice;
			}
		}
		if (hasItems) {
			System.out.printf("      %-20.20s       %,7.2f%n", "Total Price", total);
			System.out.println();
		}
		else
			System.out.println("No items in order");
	}

	public static void main(String[] args) {
		Restaurant restaurant = new Restaurant();
		restaurant.consoleUI();
	}
}
