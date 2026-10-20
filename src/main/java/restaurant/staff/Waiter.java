package restaurant.staff;

import java.util.ArrayList;

import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.menu.MenuItem;
import restaurant.menu.MenuItemStatus;
import restaurant.ui.GameUI;

/**
 * The Waiter can only do one task per turn: 
 * Seat a customer from the queue
 * Take a seated customer's order
 * Serve a ready order
 * 
 * The main method is takeAction(), which prompts the user to choose the task for the Waiter to do
 *
 * This class applies the rules for each task and updates the shared queue, tables and order list
 * Table and order indexes used here are 0-based.
 */
public class Waiter {
    private GameUI ui;
    private ArrayList<Customer> queue;
    private Customer[] tables;
    private ArrayList<MenuItem> orders;

    public Waiter(ArrayList<Customer> queue, Customer[] tables, ArrayList<MenuItem> orders, GameUI ui) {
        this.queue = queue;
        this.tables = tables;
        this.orders = orders;
        this.ui = ui;
    }

    public void takeAction() {
        while (true) {
            int choice = this.ui.chooseWaiterAction();

            switch (choice) {
                case 1:
                    if (seatCustomer()) {
                        return;
                    }
                    break;

                case 2:
                    if (takeOrder()) {
                        return;
                    }
                    break;

                case 3:
                    if (serveOrder()) {
                        return;
                    }
                    break;

                case 4:
                    return;
            }
        }
    }

    /**
     * Seats the first customer in the queue into the first available table
     * 
     * It displays the success or failure messages to user
     * 
     * Returns boolean of whether or not the customer was successfully seated
     */
    public boolean seatCustomer() {
        // Should be seating the first customer in the queue to the first available table
        // Could let player choose which table or which customer to seat next time
        if (this.queue.isEmpty()) {
            this.ui.displayNoCustomersInQueue();
            return false;
        }

        for (int i = 0; i < this.tables.length; i++) {
            if (this.tables[i] == null) {
                this.tables[i] = this.queue.get(0);
                this.tables[i].setStatus(CustomerStatus.SEATED);

                this.queue.remove(0);
                this.ui.displayCustomerSeated(i);
                return true;
            }
        }

        this.ui.displayNoAvailableTables();
        return false;
    }

    /**
     * Asks the user for the table to take the order from
     * Takes the order and places it into the shared instance of orders
     * 
     * Displays success or failure message
     * 
     * Returns boolean of whether or not the order was able to be taken
     */
    public boolean takeOrder() {
        // Could shift tablenumber logic to turn()

        boolean hasCustomerToOrder = false;
        for (int i = 0; i < tables.length; i++) {
            if (canTakeOrder(i)) {
                hasCustomerToOrder = true;
            }
        }

        if (!hasCustomerToOrder) {
            this.ui.displayNoCustomersToOrder();
            return false;
        }

        int tableIndex;
        while (true){
            tableIndex = this.ui.chooseTableToOrder(tables.length);

            if (!canTakeOrder(tableIndex)) {
                this.ui.displayNoCustomerAtTableToOrder(tableIndex);
                continue;
            }

            Customer customer = tables[tableIndex];
            MenuItem order = customer.order();
            order.setTableNo(tableIndex);
            order.setStatus(MenuItemStatus.ORDERED);
            orders.add(order);
            this.ui.displayOrderTaken(tableIndex, order);
            return true;
        }
    }

    /**
     * Gathers the list of orders that are ready to be served then displays them
     * 
     * After serving the order, it changes the order and Customer status to served
     * 
     * Returns boolean on whether the order was successfully served
     */
    public boolean serveOrder() {
        ArrayList<Integer> readyOrderIndexes = new ArrayList<>();
        for (int i = 0; i < this.orders.size(); i++) {
            if (this.orders.get(i).getStatus() == MenuItemStatus.READY) {
                readyOrderIndexes.add(i);
            }
        }

        if (readyOrderIndexes.isEmpty()) {
            this.ui.displayNoOrdersReady();
            return false;
        }

        this.ui.displayReadyOrders(readyOrderIndexes);

        while (true) {
            int orderIndex = this.ui.chooseOrderToServe(orders.size());

            if (orders.get(orderIndex).getStatus() != MenuItemStatus.READY) {
                this.ui.displayOrderNotReady(orderIndex);
                continue;
            }

            MenuItem orderToServe = orders.get(orderIndex);
            int tableNo = orderToServe.getTableNo();
            this.tables[tableNo].setStatus(CustomerStatus.SERVED);
            orderToServe.setStatus(MenuItemStatus.SERVED);
            this.ui.displayOrderServed(orderIndex, tableNo);
            return true;
        }
    }

    // A seated customer can only order once, so skip tables that already have an order
    public boolean canTakeOrder(int tableIndex) {
        if (this.tables[tableIndex] == null || this.tables[tableIndex].getStatus() != CustomerStatus.SEATED) {
            return false;
        }

        for (MenuItem order : this.orders) {
            if (order.getTableNo() == tableIndex) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getAvailableTasks() {
        boolean hasFreeTable = false;
        boolean hasSeatedCustomer = false;
        for (int i = 0; i < this.tables.length; i++) {
            if (this.tables[i] == null) {
                hasFreeTable = true;
            } else if (canTakeOrder(i)) {
                hasSeatedCustomer = true;
            }
        }

        boolean hasReadyOrder = false;
        for (MenuItem order : this.orders) {
            if (order.getStatus() == MenuItemStatus.READY) {
                hasReadyOrder = true;
            }
        }

        ArrayList<String> waiterTasks = new ArrayList<>();
        if (!this.queue.isEmpty() && hasFreeTable) {
            waiterTasks.add("Seat a customer");
        }
        if (hasSeatedCustomer) {
            waiterTasks.add("Take an order");
        }
        if (hasReadyOrder) {
            waiterTasks.add("Serve an order");
        }
        waiterTasks.add("Wait");
        return waiterTasks;
    }
}