package restaurant;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * An item on the menu, with product id, description, and price.
 * This is used as basis for persistence.
 * 
 * There are two sets of annotations available: one specific to ORMlist
 * and the standard JPA annotations. To use JPA annotations you need the
 * javax.persistence.api JAR file.
 * 
 * JPA       ORMlite
 * @Entity   @DatabaseTable
 * @Id       @DatabaseField(generatedId=true) or (id=true)
 * @Column   @DatabaseField
 * 
 * @author Fatalai Jon
 */
@DatabaseTable(tableName="menu_items")
public class MenuItem {
	// These constants are names of fields in the menu_items table.
	// They are provided so that we can create queries with QueryBuilder
	// that are uncoupled from the Java attribute names.
	public static final String NAME_FIELD = "name";
	public static final String PRICE_FIELD = "price";
	// A unique ID for this item.  Persistence frameworks usually
	// encourage use of object types (Long) instead of primitive,
	// but you can use primitive if you want.
	// If using object type, then null means the object definitely
	// hasn't been persisted yet.
	
	// automatically assign an id on save, if doesn't have an id
	@DatabaseField(generatedId=true, allowGeneratedIdInsert=true)
	private Long id;
	@DatabaseField(canBeNull=false, columnName=NAME_FIELD)
	private String name;
	@DatabaseField(canBeNull=false, columnName=PRICE_FIELD)
	private double price;
	
	/**
	 * Default constructor for use by ORM framework, with minimal package scope.
	 * Use the other constructor with parameters for creating actual MenuItems.
	 * 
	 */
	MenuItem() {
		// persistence framework requires a default (no-arg) constructor
		this("", 0.0);
	}
	
	/**
	 * A new MenuItem with given description (name) and unit price.
	 * @param name the name of this item
	 * @param price the unit price
	 */
	public MenuItem(String name, double price) {
		this.name = name;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return String.format("[%d] %s @ %,.2f", id, name, price);
	}

}
