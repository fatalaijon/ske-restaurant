import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RestaurantManager provides behavior that a restaurant
 * would provide to staff, including what's on the menu,
 * handling of orders, recording sales.
 * This class is a singleton.
 *
 * @author Fatalai Jon
 */

public class RestaurantManager {
	/** Name of the restaurant displayed in UI and on receipts. */
	static final String restaurantName = "SKE Objects Restaurant";
    //TODO put this in a configuration file
	static final String MENU_FILE = "data/menu.txt";
    /** Singleton instance of this class. */
    private static RestaurantManager instance = null;
    // Order numbers
    protected AtomicLong nextOrderNumber;
	// Not static anymore! 
	private String[] menuItems;
	private double[] prices;
   
    /**
     * Don't allow direct instantiation of this class.
     * Constructor is protected to allow defining subclasses.
     */
    protected RestaurantManager() {
        loadMenu();
        nextOrderNumber = new AtomicLong(1L);
    }

    /**
	 * Get an instance of RestaurantManager.
     * Returned object may be a singleton or subclass object.
	 *
     * @return instance of RestaurantManager
	 */
	public static RestaurantManager getInstance() {
		if (instance == null) {
			// If it doesn't exist, then create it now (only once).
			// This synchronized block and redundant test prevent double object creation.
			synchronized(RestaurantManager.class) {
				if (instance == null) instance = new RestaurantManager();
			}
		}
		return instance;
	}	
			
	/** Load menu data from a file. */
	private void loadMenu() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream in = loader.getResourceAsStream( MENU_FILE );
		// check that it worked
		if (in == null) {
			System.err.println("Could not find menu file "+MENU_FILE);
			return;
		}
		// Temporary collections for menu data
		List<String> menuList = new ArrayList<>();
		List<Double> priceList = new ArrayList<>();

		Scanner scanner = new Scanner(in);
		int linecount = 0;
		while( scanner.hasNextLine() ) {
			String line = scanner.nextLine().trim();
			linecount++;
			if (line.isEmpty() || line.startsWith("#")) continue;
			String[] args = line.split("\\s*;\\s*");
			if (args.length != 2) {
				menuError(MENU_FILE, linecount);
				continue;
			}
			double price = 0.0;
			try {
				price = Double.parseDouble(args[1]);
			} catch (NumberFormatException nfe) {
				menuError(MENU_FILE, linecount);
				continue;
			}
			if (args[0].isEmpty() || menuList.contains(args[0]) || price <= 0.0) {
				menuError(MENU_FILE, linecount);
			}
			else {
				menuList.add( args[0] );
				priceList.add( price );
			}
		}
		// close resource when done to free resources
		scanner.close();
		// Add a dummy items to start of lists so that the indices of real menu items start at 1, not 0.
		menuList.add(0, "No item");
		priceList.add(0, 0.0);
		menuItems = new String[menuList.size()];
		prices = new double[priceList.size()];
		menuList.toArray(menuItems);
		// manual copy Double to primitives
		for(int k=0; k<priceList.size(); k++) prices[k] = priceList.get(k);
	}

	private void menuError(String filename, int linenum) {
		System.err.printf("Invalid menu data in %s, line %d\n", filename, linenum);
	}
	
	
	public String[] getMenuItems() {
		return menuItems;
	}



	public double[] getPrices() {
		return prices;
	}


    /** For testing methods. Not for starting app. */
	public static void main(String[] args) {
		RestaurantManager rm = getInstance();
		String[] menu = rm.getMenuItems();
		double[] prices = rm.getPrices();

		for(int k=0; k<menu.length; k++) {
			System.out.printf("%-24.24s  %,7.2f\n", menu[k], prices[k]);
		}
	}
	
	/**
	 * Record an order.  Set the order number and timestamp.
	 * @param order
	 */
	public void recordOrder(Order order) {
		// assign order number
		order.setOrderNumber( getNextOrderNumber() );
		order.setTimeStamp( LocalDateTime.now() );
	}
	
	/**
	 * Return a unique order number.
	 * Uses AtomicLong to avoid possible inconsistencies
	 * if using threads.
	 * @return next available order number
	 */
	private long getNextOrderNumber() {
		return nextOrderNumber.getAndIncrement();
		
	}

	public static String getRestaurantName() {
		return restaurantName;
	}
}
