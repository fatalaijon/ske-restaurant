package restaurant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

import order.Order;

/**
 * RestaurantManager that reads menu data from a file and
 * writes orders to a file.
 *
 * @author Fatalai Jon
 */

public class FileBackedRestaurantManager extends RestaurantManager {
	//TODO put the menu filename in a configuration file
	static final String MENU_FILE = "data/menu.txt";
	static final String ORDERS_LOG = "data/ske_orders.log";

	// Not static anymore! 
	private String[] menuItems;
	private double[] prices;
   
	/**
	 * Don't allow direct instantiation of this class.
	 * Constructor is protected to allow defining subclasses.
	 */
	protected FileBackedRestaurantManager() {
	    loadMenu( MENU_FILE );
	    nextOrderNumber = new AtomicLong(1L);
	}
	
			
	/** Load menu data from a file. */
	private void loadMenu(String filename) {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream in = loader.getResourceAsStream( filename );
		// If not found, try again as name of file in file system
		if (in == null) try {
			in = new FileInputStream( filename );
		} catch(FileNotFoundException nfe) {
			// handled below
		}
		// check that it worked
		if (in == null) {
			System.err.println("Could not find menu file "+filename);
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
				menuError(filename, linecount);
				continue;
			}
			if (args[0].isEmpty() || menuList.contains(args[0]) || price < 0.0) {
				menuError(filename, linecount);
			}
			else {
				menuList.add( args[0] );
				priceList.add( price );
			}
		}
		// close resource when done to free resources
		scanner.close();
		// Add a dummy item at the beginning of lists so that 
		// the indices of real menu items start at 1, not 0.
		menuList.add(0, "No item");
		priceList.add(0, 0.0);
		menuItems = new String[menuList.size()];
		prices = new double[priceList.size()];
		menuList.toArray(menuItems);
		// manual copy List<Double> to primitives
		for(int k=0; k<priceList.size(); k++) prices[k] = priceList.get(k);
	}

	private void menuError(String filename, int linenum) {
		System.err.printf("Invalid menu data in %s, line %d\n", filename, linenum);
	}
	
	/**
	 * @see RestaurantManager#getMenuItems()
	 */
	@Override
	public String[] getMenuItems() {
		return menuItems;
	}

	/**
	 * @see RestaurantManager#getPrices()
	 */
	@Override
	public double[] getPrices() {
		return prices;
	}


	/** For testing methods. Not for starting app. */
	public static void main(String[] args) {
		RestaurantManager rm = new FileBackedRestaurantManager();
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
		
		PrintStream log = getLog();
		log.printf("Order No. %d\nReceived %tT\n", order.getOrderNumber(), order.getTimeStamp() );
		log.println();
	}
	
	private PrintStream getLog() {
		final boolean append = true; // append to the output file
		final boolean autoFlush = true; // automatically flush output to file each time \n is seen
		try {
			// make sure the path to file exists
			File file = new File(ORDERS_LOG);
			if (! file.exists() ) {
				String path = file.getParent();
				if (path != null && ! path.isEmpty()) {
					File dirpath = new File(path);
					dirpath.mkdirs();
				}
			}
			FileOutputStream out = new FileOutputStream(ORDERS_LOG, append);
			PrintStream pout = new PrintStream(out, autoFlush);
			return pout;
		} catch (FileNotFoundException|SecurityException ex) {
			System.err.println("Exception opening log file "+ORDERS_LOG);
			System.err.println(ex.getMessage());
		}
		// Try using a temp file instead
		int k = ORDERS_LOG.lastIndexOf('.');
		String suffix = (k>0)? ORDERS_LOG.substring(k+1) : "log";
		String prefix = (k>0)? ORDERS_LOG.substring(0,k-1) : ORDERS_LOG;
		try {
			File templog = File.createTempFile(prefix, suffix);
			PrintStream pout = new PrintStream(templog);
			return pout;
		} catch (IOException ex) {
			System.err.println("Could not create temp file for orders log.");
			System.err.println(ex.getMessage());
		}
		// Log to console as last resort!
		return System.out;
	}
	
	@Override
	public void shutdown() {
		// Flush and Close files
	}

}
