package restaurant.staff;

import java.util.ArrayList;

import restaurant.menu.MenuItem;
import restaurant.menu.MenuItemStatus;
import restaurant.ui.GameUI;

/**
 * The Chef can only do one task per turn:
 * Prepare an order (advances one order by one preparation unit)
 * Wait
 *
 * The main method is takeAction(), which prompts the user to choose the task for the Chef to do
 *
 * This class applies the rules for preparing orders and updates the shared order list
 * Order indexes used here are 0-based.
 */
public class Chef {
    private GameUI ui;
    private ArrayList<MenuItem> orders;

    public Chef(ArrayList<MenuItem> orders, GameUI ui) {
        this.orders = orders;
        this.ui = ui;
    }

    public void takeAction() {
        // Might move to main app instead for OOP rules but tbd

        while (true) {
            int choice = this.ui.chooseChefAction();

            switch (choice) {
                case 1:
                    if (prepareOrder()) {
                        return;
                    }
                    break;
                case 2:
                    return;
            }
        }
    }

    /**
     * Displays the orders and asks the user which one to prepare
     * Advances the chosen order by one preparation unit
     *
     * Displays success or failure message
     *
     * Returns boolean of whether or not an order was prepared
     */
    public boolean prepareOrder() {
        if (orders.isEmpty()) {
            this.ui.displayNoOrdersToPrepare();
            return false;
        }

        this.ui.displayOrdersToPrepare();

        int orderIndex = this.ui.chooseOrderToPrepare(orders.size());
        this.orders.get(orderIndex).cook();
        this.ui.displayOrderPrepared(this.orders.get(orderIndex));
        return true;
    }

    public ArrayList<String> getAvailableTasks() {
        boolean hasOrderToPrepare = false;
        for (MenuItem order : this.orders) {
            if (order.getStatus() == MenuItemStatus.ORDERED) {
                hasOrderToPrepare = true;
            }
        }

        ArrayList<String> chefTasks = new ArrayList<>();
        if (hasOrderToPrepare) {
            chefTasks.add("Prepare an order");
        }
        chefTasks.add("Wait");
        return chefTasks;
    }
}