package restaurant.customers;

import java.util.Random;

import restaurant.menu.MenuItem;

public class Regular extends Customer {
    static Random random = new Random(67L);

    public Regular(MenuItem[] menu) {
        super(menu);
        this.satisfactionLoss = 8;
    }

    @Override
    public MenuItem order() {
        int choice = random.nextInt(this.menu.length);
        return new MenuItem(this.menu[choice]);
    }
}