package restaurant.ui;

import java.util.List;
import java.util.Scanner;

import restaurant.MenuItem;
import restaurant.customers.Customer;

public class GameUI {
    private Scanner scanner;

    public GameUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt() {
        while (!this.scanner.hasNextInt()) {
            System.out.println("Please enter a number.");
            this.scanner.next();
        }
        return this.scanner.nextInt();
    }

    public int readIntInRange(int min, int max, String invalidMessage) {
        while (true) {
            int choice = readInt();
            if (choice >= min && choice <= max) {
                return choice;
            }
            System.out.println(invalidMessage);
        }
    }

    public int chooseWaiterAction() {
        System.out.println("Choose an action for the waiter:");
        System.out.println("1. Seat a customer");
        System.out.println("2. Take an order");
        System.out.println("3. Serve an order");
        System.out.println("4. Wait");
        return readIntInRange(1, 4, "Invalid choice. Please try again.");
    }

    public int chooseChefAction() {
        System.out.println("Choose an action for the chef:");
        System.out.println("1. Prepare an order");
        System.out.println("2. Wait");
        return readIntInRange(1, 2, "Invalid choice. Please try again.");
    }

    public int chooseTableToOrder(int numberOfTables) {
        System.out.println("Enter the table number to take order from (1-" + numberOfTables + "):");
        return readIntInRange(1, numberOfTables, "Invalid table number. Please try again.") - 1;
    }

    public int chooseOrderToServe(int numberOfOrders) {
        System.out.println("Enter order number to serve:");
        return readIntInRange(1, numberOfOrders, "Invalid order number. Please try again.") - 1;
    }

    public int chooseOrderToPrepare(int numberOfOrders) {
        System.out.print("Enter the number of the order to prepare: ");
        return readIntInRange(1, numberOfOrders, "Invalid order number. Please try again.") - 1;
    }

    public void displayTurnStart(int turn, int maxTurns, float revenue, Float averageSatisfaction,
                                 List<Customer> queue, Customer[] tables, List<MenuItem> orders,
                                 List<String> waiterTasks, List<String> chefTasks) {
        System.out.println();
        System.out.println("===== Turn " + turn + " / " + maxTurns + " =====");
        System.out.printf("Revenue: $%.2f | Average satisfaction of paid customers: %s%n",
                revenue, formatAverageSatisfaction(averageSatisfaction));

        System.out.println("Waiting queue:");
        if (queue.isEmpty()) {
            System.out.println("  (empty)");
        }
        for (Customer customer : queue) {
            System.out.println("  " + customer.getName() + " - satisfaction " + customer.getSatisfaction());
        }

        System.out.println("Tables:");
        for (int i = 0; i < tables.length; i++) {
            if (tables[i] == null) {
                System.out.println("  Table " + (i + 1) + ": empty");
            } else {
                System.out.println("  Table " + (i + 1) + ": " + tables[i].getName() + " - " + tables[i].getStatus()
                        + " - satisfaction " + tables[i].getSatisfaction());
            }
        }

        System.out.println("Orders:");
        if (orders.isEmpty()) {
            System.out.println("  (none)");
        }
        for (int i = 0; i < orders.size(); i++) {
            MenuItem order = orders.get(i);
            System.out.println("  " + (i + 1) + ". " + order.getName() + " for table " + (order.getTableNo() + 1) + " - "
                    + order.getStatus() + " (" + order.getPreparationUnits() + " preparation units left)");
        }

        System.out.println("Available tasks:");
        System.out.println("  Waiter: " + String.join(", ", waiterTasks));
        System.out.println("  Chef: " + String.join(", ", chefTasks));
        System.out.println();
    }

    public void displayCustomerArrived(Customer customer) {
        System.out.println(customer.getName() + " has joined the queue.");
    }

    public void displayNoCustomersInQueue() {
        System.out.println("No customers in the queue to seat.");
    }

    public void displayNoAvailableTables() {
        System.out.println("No available tables to seat the customer.");
    }

    public void displayCustomerSeated(int tableIndex) {
        System.out.println("Customer seated at table " + (tableIndex + 1) + ".");
    }

    public void displayNoCustomersToOrder() {
        System.out.println("No seated customers waiting to order.");
    }

    public void displayNoCustomerAtTableToOrder(int tableIndex) {
        System.out.println("No customer at table " + (tableIndex + 1) + " waiting to order.");
    }

    public void displayOrderTaken(int tableIndex, MenuItem order) {
        System.out.println("Order taken from table " + (tableIndex + 1) + ": " + order.getName());
    }

    public void displayNoOrdersReady() {
        System.out.println("No orders are ready to be served.");
    }

    public void displayReadyOrders(List<Integer> orderIndexes) {
        System.out.println("Ready to be served orders:");
        for (int orderIndex : orderIndexes) {
            System.out.println("Order " + (orderIndex + 1));
        }
    }

    public void displayOrderNotReady(int orderIndex) {
        System.out.println("Order " + (orderIndex + 1) + " is not ready to be served.");
    }

    public void displayOrderServed(int orderIndex, int tableIndex) {
        System.out.println("Order " + (orderIndex + 1) + " served to table " + (tableIndex + 1) + ".");
    }

    public void displayNoOrdersToPrepare() {
        System.out.println("No orders to prepare.");
    }

    public void displayOrdersToPrepare(List<MenuItem> orders) {
        System.out.println("Orders to prepare:");
        for (int i = 0; i < orders.size(); i++) {
            MenuItem order = orders.get(i);
            System.out.println((i + 1) + ". " + order.getName() + " - Status: " + order.getStatus());
        }
    }

    public void displayOrderPrepared(MenuItem order) {
        System.out.println("Order " + order.getName() + " is being prepared. Remaining preparation units: " + order.getPreparationUnits());
    }

    public void displayPayment(int tableIndex, MenuItem order) {
        System.out.printf("Customer at table %d has paid for their order: %s - $%.2f%n", tableIndex + 1, order.getName(), order.getPrice());
    }

    public void displayGameOver(int turnsUsed, int maxTurns, int servedCustomers, int paidCustomers,
                                int unhappyDepartures, float revenue, Float averageSatisfaction, boolean victory) {
        System.out.println();
        System.out.println("===== Game Over =====");
        System.out.println("Turns used: " + turnsUsed + " / " + maxTurns);
        System.out.println("Customers served: " + servedCustomers);
        System.out.println("Paid customers: " + paidCustomers);
        System.out.println("Unhappy departures: " + unhappyDepartures);
        System.out.printf("Revenue: $%.2f%n", revenue);
        System.out.println("Average satisfaction of paid customers: " + formatAverageSatisfaction(averageSatisfaction));
        System.out.println(victory ? "Victory!" : "Defeat!");
    }

    private String formatAverageSatisfaction(Float averageSatisfaction) {
        if (averageSatisfaction == null) {
            return "N/A";
        }
        return String.format("%.2f", averageSatisfaction);
    }
}
