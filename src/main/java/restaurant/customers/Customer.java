package restaurant.customers;

import restaurant.menu.MenuItem;

/**
 * Serves as the base class for all Customers in the game
 * It's id is basically the nth customer that spawned in
 * Needs to have a menu array passed in when initializing 
 * Defaults satisfaction value to 100
 */
public abstract class Customer {
    private static int nextId = 1;

    private final int id;
    private int satisfaction = 100;
    // To be determined by the child classes
    protected int satisfactionLoss;
    private CustomerStatus status;
    protected MenuItem[] menu;

    public Customer(MenuItem[] menu) {
        this.menu = menu;
        this.id = nextId++;
    }

    /**
     * Needs to be implemented by the child class and returns a MenuItem
     */
    public abstract MenuItem order();

    public String getName() { return "Customer " + this.id; }

    public int getSatisfaction() { return this.satisfaction; }

    public CustomerStatus getStatus() { return this.status; }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    /**
     * Decrease the satisfaction by the satisfactionLoss value and detects when Customer is unhappy
     */
    public void satisfactionDecay() {
        if (this.status == CustomerStatus.WAITING || this.status == CustomerStatus.SEATED) {
            this.satisfaction -= this.satisfactionLoss;
        }

        if (this.satisfaction <= 0) {
            this.satisfaction = 0;
            this.status = CustomerStatus.LEAVING;
        }
    }
}