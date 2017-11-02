/**
 * The main class for running the SKE Restaurant application.
 * This call just initializes other objects and invokes a
 * method to start UI.
 * 
 * @author Fatalai Jon
 *
 */
public class RestaurantApp {

	public static void main(String[] args) {
		RestaurantManager rm = RestaurantManager.getInstance();
		RestaurantUI restaurant = new RestaurantUI( rm );
		restaurant.consoleUI();
	}
}
