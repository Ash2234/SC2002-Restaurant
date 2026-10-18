package restaurant.customers;

import java.util.Random;

import restaurant.MenuItem;

public class Regular extends Customer {
    static Random random = new Random(67L);

    public Regular() {
        super();
        this.satisfactionLoss = 8;
    }

    @Override
    public MenuItem order(MenuItem[] menuItems) {
        int choice = this.random.nextInt(menuItems.length);
        return new MenuItem(menuItems[choice]);
    }
}