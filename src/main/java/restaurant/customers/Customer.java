package restaurant.customers;

import restaurant.menu.MenuItem;

public abstract class Customer {
    private static int nextId = 1;

    private final int id;
    private int satisfaction = 100;
    protected int satisfactionLoss;
    private CustomerStatus status;
    protected MenuItem[] menu;

    public Customer(MenuItem[] menu) {
        this.menu = menu;
        this.id = nextId++;
    }

    public abstract MenuItem order();

    public String getName() { return "Customer " + this.id; }

    public int getSatisfaction() { return this.satisfaction; }

    public CustomerStatus getStatus() { return this.status; }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

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