package restaurant.customers;

import java.util.Random;

import restaurant.menu.MenuItem;

public class Regular extends Customer {
    // 6767676767676767
    static Random random = new Random(67L);

    public Regular(MenuItem[] menu) {
        super(menu);
        // Value for only Regular
        this.satisfactionLoss = 8;
    }

    /**
     * Custom order logic for just the Regular Customer class
     */
    @Override
    public MenuItem order() {
        int choice = random.nextInt(this.menu.length);
        return new MenuItem(this.menu[choice]);
    }
}