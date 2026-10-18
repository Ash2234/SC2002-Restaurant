package restaurant.customers;

import restaurant.MenuItem;

public abstract class Customer {
    private int satisfaction = 100;
    private int satisfactionLoss;
    private float bill;
    private CustomerStatus status;
    private MenuItem[] menu;

    public Customer(MenuItem[] menu) {
        this.menu = menu;
    }

    public abstract MenuItem order();

    public CustomerStatus getStatus() {
        return this.status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public boolean satisfactionDecay() {
        if (this.status == CustomerStatus.WAITING || this.status == CustomerStatus.SEATED) {
            this.satisfaction -= this.satisfactionLoss;
        }

        if (this.satisfaction <= 0) {
            this.satisfaction = 0;
            return true;
        }
        return false;
    }
}