## Programming Project Proposal

1. Title: **Menu Ordering System with Graphical UI**

2. Team: Fatalai Jon

3. A menu ordering system for a restaurant, including a graphical UI and order tracking.

4. The application lets a restaurant customer (or anyone) choose items to order from a graphical menu of choices, divided into categories, and place an order.  Categories may be, for example, main dishes, side order, beverages, and dessert.  It is intended to show only a limited selection of food items.   
    When the order is complete, it is sent to a document-oriented database in the cloud.  From there, another app can view the order.   
     Main features and course knowledge used are:
    * Object-oriented design. Uses objects for instances of everything.
    * Uses collections (List and Map) for menu and orders.
    * JavaFX for graphical UI.
    * Cloud document database for orders (maybe).

5. Vision: a simple ordering system that is easy to use.  The UI should make it clear to customer what he can do at each step, and be familiar (look similar to other apps most people use).   This work serves as a prototype for me to learn how to design and implement a real menu ordering app for a small restaurant.

## Technology and Implementation

[Singleton Restaurant Manager](Singleton.md) - the application ensures that
there is only **one** RestaurantManager object, but we can change the actual
RestaurantManager (using subclasses) without changing any other part of the code.



