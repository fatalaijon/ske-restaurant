## Singleton for RestaurantManager

**Singleton** is a design pattern that lets you ensure an application
only has **one** instance of a particular class.  This is used in places
where there should only be one object and the object is accessible by
whatever objects need it.

Examples: a singleton Bank in a banking app, a singleton Store (that
records sales) in a retail app.

### How to write a Singleton

There are 3 parts to a Singleton:
```java
public class Singleton {
    // 1. static instance of the class
    private static Singleton instance;
    
    // 2. constructor is private or protected, to prevent other objects
    //    from using "new Singleton"
    protected Singleton() {
        // do initialization here
    }
    
    // 3. static accessor method creates the singleton
    public static Singleton getInstance() {
        if (instance != null) instance = new Singleton();
        return instance;
    }
}
```

For `RestaurantManager` we used:

```java
public class RestaurantManager {
    /** Single instance of the RestaurantManager class */
    private static RestaurantManager instance;
    // attributes of the RestaurantManger;
    private MenuItem[] menu;
    
    /** Protected Constructor: use getInstance() to get RestaurantManager. */
    protected RestaurantManager() {
        init(); // read the menu
    }
    
    /** Get an instance of RestaurantManager. */
    public static RestaurantManager getInstance() {
        if (instance != null) instance = new RestaurantManager();
        return instance;
    }
    // Other methods for behavior of RestaurantManager
}
```

Anywhere in the application that you need to access the RestaurantManager just write:

```java
    RestaurantManager rm = RestaurantManager.getInstance();
    MenuItem[] menu = rm.getMenuItems();
```

## Creating Specialized RestaurantManagers

Using Singleton (or a Factory Method) we can *hide* the details of what RestaurantManager is being created and used.  In this app we have a subclass named FileBackedRestaurantManager:

```java
package restaurant;
/**
 * RestaurantManger that uses files for menu data and orders log.
 */
public class FileBackedRestaurantManger extends RestaurantManger {
    ...
```

The `RestaurantManager.getInstance()` method can create and return a subclass object:
```java
public class RestaurantManager {
    private static RestaurantManager instance;
    
    /** Get an instance of RestaurantManager. */
    public static RestaurantManager getInstance() {
        if (instance != null) instance = new FileBackedRestaurantManager();
        return instance;
    }
```

We don't need to change any other part of the code to use a FileBackedRestaurantManager (because it behaves just like a basic RestaurantManager).  If we want to use a datbase-based restaurant manager, we only need to change the `getInstance()` method of the base class.
