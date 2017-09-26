package homework1;

import java.util.Scanner;

/**
 * Homework 1: a console based interface for placing an order
 * at a restaurant.  Students don't know objects yet, so don't
 * use objects.
 * 
 * @author jim
 *
 */
public class Restaurant {
	/** Names of items on the menu. */
	static final String[] items = { "Pizza", "Chicken", "Salad", "Ice Tea", "Drinking Water" };
	/** Prices of the items, in same order as item names. */
	static final int[] prices = { 250, 90, 30, 25, 12 };
	/** Parse input from console. */
	static final Scanner console = new Scanner(System.in);
	
	/** Customer's order is the quantity of each item.  Initially the quantities are 0. */
	
	public static void printMenu() {
		// format used for printf of menu
		String format = "%2d) %-20.20s %,5d%n";
		for(int k=0; k<items.length; k++) {
			System.out.printf(format, k+1, items[k], prices[k]);
		}
		// special items
		String choiceformat = "%2.2s) %s%n";
		System.out.printf(choiceformat, "s", "Show order");
		System.out.printf(choiceformat, "c", "Checkout and quit");
		System.out.printf(choiceformat, "0", "Quit");
	}
	
	public static String getChoice() {
		System.out.print("Enter choice: ");
		return console.next();
	}
	
	public static void quit() {
		System.out.println("Goodbye");
		System.exit(0);
	}
	
	public static void main(String[] args) {
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
	private static void showOrder(int[] order) {
		int total = 0;
		boolean hasItems = false;
		System.out.printf("Item# %-20.20s %4s2  %5s%n","Description", "Qnty", "Price");
		for(int k=0; k<order.length; k++) {
			if (order[k] != 0) {
				int itemPrice = order[k]*prices[k]; 
				hasItems = true;
				System.out.printf("%3d   %-20.20s  %3d  %,5d%n", k+1, items[k], order[k], itemPrice);
				total += itemPrice;
			}
		}
		if (hasItems)
			System.out.printf("      %-20.20s    %,8d%n", "Total Price", total);
		else
			System.out.println("No items in order");
	}
}
