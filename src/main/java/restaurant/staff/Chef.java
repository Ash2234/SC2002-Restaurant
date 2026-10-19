package restaurant.staff;

import java.util.ArrayList;

import restaurant.MenuItem;
import restaurant.MenuItemStatus;
import restaurant.ui.GameUI;

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

    public boolean prepareOrder() {
        if (orders.isEmpty()) {
            this.ui.displayNoOrdersToPrepare();
            return false;
        }

        this.ui.displayOrdersToPrepare(orders);

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