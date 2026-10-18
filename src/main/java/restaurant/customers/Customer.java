package restaurant.customers;

import restaurant.MenuItem;

public abstract class Customer {
    private int satisfaction = 100;
    private int satisfactionLoss;
    private float bill;
    private String status;

    public abstract MenuItem order(MenuItem[] menuItems);

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean satisfactionDecay() {
        if (this.status == "waiting" || this.status == "seated") {
            this.satisfaction -= this.satisfactionLoss;
        }

        if (this.satisfaction <= 0) {
            this.satisfaction = 0;
            return true;
        }
        return false;
    }
}