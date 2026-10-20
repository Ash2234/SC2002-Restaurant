package restaurant.menu;

/**
 * The MenuItem serves as both an order and a menu item
 * The lifecycle of a MenuItem:
 * 1. Is initialized in the main game loop and stored in the menu array
 * 2. Customer orders it off the menu by copying it and giving it to the Waiter
 * 3. The Waiter then stores it in the orders array
 * 4. The Chefs cooks it and the Waiter serves it
 * 5. The Customer finishes eating and pays, the Cashier then sets it status to PAID
 * 
 * It holds the table number that ordered it, status, name, price and preparation units.
 * To be initialized with preparationUnits, name and price
 */
public class MenuItem {
    private String name;
    private float price;
    private int preparationUnits;
    
    private MenuItemStatus status = MenuItemStatus.MENU;
    private Integer tableNo;

    public MenuItem(String name, float price, int preparationUnits) {
        this.name = name;
        this.price = price;
        this.preparationUnits = preparationUnits;
    }

    /**
     * Clone method
     */
    public MenuItem(MenuItem menuItem) {
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
        this.preparationUnits = menuItem.getPreparationUnits();
    }

    public String getName() { return name; }

    public float getPrice() { return price; }

    public int getPreparationUnits() { return preparationUnits; }

    public MenuItemStatus getStatus() { return status; }

    public Integer getTableNo() { return tableNo; }

    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    /**
     * Manages its own preparationUnit through the public cook() method
     * Automatically swtiches its own status from ORDERED to READY
     */
    public void cook() {
        if (this.preparationUnits > 0) {
            this.preparationUnits--;
            if (this.preparationUnits == 0) {
                this.status = MenuItemStatus.READY;
            }
        }
    }
}