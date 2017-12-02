package restaurant;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import java.util.logging.Logger;

import order.Order;

/**
 * RestaurantManager provides behavior that a restaurant
 * would provide to staff, including what's on the menu,
 * handling of orders, recording sales.
 * 
 * This class is a singleton. Its the base class for the
 * actual RestaurantManager, which it creates the first
 * time any code calls getInstance().
 * 
 * Why do this? 
 * 1. so we only have one RestaurantManger (singleton)
 * 2. So we can have different implementations of Restaurant
 *    Manager without changing the other parts of the code
 *    (polymorphism).  
 *    
 * I'm implementing two RestaurantManagers:
 * a) FileBackedRestaurantManager - uses text files for data
 * b) RestaurantManagerORM - uses a database for data
 * 
 * the getIntance() method creates the real RestaurantManager.
 *
 * @author Fatalai Jon
 */

public class RestaurantManager {
	/** Name of the restaurant displayed in UI and on receipts. */
	static final String restaurantName = "SKE Object Cafe";
	
	/** Singleton instance of this class. */
	private static RestaurantManager instance = null;
	/** Next available order number. */
	protected AtomicLong nextOrderNumber;;
	/** Logger for messages such as exceptions and unusual conditions. */
	protected static Logger logger = null;
	
	/**
	 * Don't allow direct instantiation of this class.
	 * Constructor is protected to allow creating subclasses.
	 */
	protected RestaurantManager() {
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
				if (instance == null) instance = new FileBackedRestaurantManager();
			}
		}
		return instance;
	}	
	
	/** 
	 * Return the menu items as an array of menu item names.
	 * 
	 * @return the menu item names
	 */
	public String[] getMenuItems() {
		// subclass should override this and return actual menu
		return new String[0];
	}

	/** 
	 * Return the prices of the menu items
	 * 
	 * @return the menu item prices
	 */
	public double[] getPrices() {
		// subclass should override this and return actual prices
		return new double[0];
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
		
		// subclass is responsible for actually saving the order.
	}
	
	/**
	 * Return a unique order number.
	 * Uses AtomicLong to avoid possible inconsistencies
	 * if using threads.
	 * @return next available order number
	 */
	protected long getNextOrderNumber() {
		return nextOrderNumber.getAndIncrement();	
	}

	public String getRestaurantName() {
		return restaurantName;
	}
	
	protected static Logger getLogger() {
		if (logger == null) logger = Logger.getLogger("RestaurantManager");
		return logger;
	}
	
	/** Prepare for exit. Close files or database connection. */
	public void shutdown() {  }
}
