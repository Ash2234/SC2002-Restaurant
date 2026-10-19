package restaurant.staff;

import java.util.ArrayList;

import restaurant.MenuItem;
import restaurant.MenuItemStatus;
import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.ui.GameUI;

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