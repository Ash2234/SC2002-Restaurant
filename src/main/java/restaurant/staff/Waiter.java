package restaurant.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import restaurant.MenuItem;
import restaurant.MenuItemStatus;
import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;

public class Waiter {
    private Scanner scanner;
    private ArrayList<Customer> queue;
    private Customer[] tables;
    private ArrayList<MenuItem> orders;

    public Waiter(ArrayList<Customer> queue, Customer[] tables, ArrayList<MenuItem> orders, Scanner scanner) {
        this.queue = queue;
        this.tables = tables;
        this.orders = orders;
        this.scanner = scanner;
    }

    public void takeAction() {
        // Might move to main app instead for OOP rules but tbd

        while (true) {
            System.out.println("Choose an action for the waiter:");
            System.out.println("1. Seat a customer");
            System.out.println("2. Take an order");
            System.out.println("3. Serve an order");
            System.out.println("4. Exit");

            int choice = this.scanner.nextInt();

            switch (choice) {
                case 1:
                    if (!seatCustomer()) {
                        continue;
                    }
                    break;
                case 2:
                    if (!takeOrder()) {
                        continue;
                    }
                    break;
                case 3:
                    if (!serveOrder()) {
                        continue;
                    }
                    break;
                case 4:
                    return; // Exit the method
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public boolean seatCustomer() {
        // Should be seating the first customer in the queue to the first available table
        // Could let player choose which table or which customer to seat next time
        if (this.queue.isEmpty()) {
            System.out.println("No customers in the queue to seat.");
            return false;
        }

        for (int i = 0; i < this.tables.length; i++) {
            if (this.tables[i] == null) {
                this.tables[i] = this.queue.get(0);

                this.queue.remove(0);
                System.out.println("Customer seated at table " + (i + 1) + ".");
                return true;
            }
        }

        System.out.println("No available tables to seat the customer.");
        return false;
    }

    public boolean takeOrder() {
        // Could shift tablenumber logic to turn()

        if (Arrays.stream(tables).allMatch(Objects::isNull)) {
            System.out.println("No customers at the tables to take orders from.");
            return false;
        }

        while (true){
            System.out.println("Enter the table number to take order from (1-" + tables.length + "):");
            int choice = this.scanner.nextInt();

            if (choice < 1 || choice > tables.length) {
                System.out.println("Invalid table number. Please try again.");
                continue;
            }

            if (tables[choice - 1] == null || !tables[choice - 1].getStatus().equals(CustomerStatus.SEATED)) {
                System.out.println("No customer at table " + choice + " to take order from.");
                continue;
            }

            Customer customer = tables[choice - 1];
            MenuItem order = customer.order();
            order.setTableNo(choice);
            order.setStatus(MenuItemStatus.ORDERED);
            orders.add(order);
            System.out.println("Order taken from table " + choice + ": " + order.getName());
            return true;
        }
    }

    public boolean serveOrder() {
        if (orders.isEmpty()) {
            System.out.println("No orders to serve.");
            return false;
        }

        while (true) {
            System.out.println("Enter order number to serve (1-" + orders.size() + "):");
            int choice = this.scanner.nextInt();
            if (choice < 1 || choice > orders.size()) {
                System.out.println("Invalid order number. Please try again.");
                continue;
            }

            if (orders.get(choice - 1).getStatus() != MenuItemStatus.READY) {
                System.out.println("Order " + choice + " is not ready to be served.");
                continue;
            }

            MenuItem orderToServe = orders.get(choice - 1);
            int tableNo = orderToServe.getTableNo();
            this.tables[tableNo - 1].setStatus(CustomerStatus.EATING);
            orderToServe.setStatus(MenuItemStatus.SERVED);
            System.out.println("Order " + choice + " served to table " + tableNo + ".");
            return true;
        }
    }
}