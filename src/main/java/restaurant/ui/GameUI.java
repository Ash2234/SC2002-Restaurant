package restaurant.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import restaurant.GameStats;
import restaurant.customers.Customer;
import restaurant.menu.MenuItem;

/**
 * Console user interface for the game
 * It purely functions as an I/O interface for other objects in the game
 * It also serves as a filter to clean up inputs
 *
 * It contains no game rules
 * The staff classes and the game loop decide what is allowed and calls the display methods
 *  All dialogue is looked up by key from messages.properties through the Messages class.
 *
 * The queue, tables, orders and statistics are update automatically
 * Logic holding classes share the same instances of these objects so the methods don't get overloaded with arguments
 */
public class GameUI {
    // Print flags, combine with | (e.g. INDENT | PROMPT)
    private static final int INDENT = 1;  // two-space indent
    private static final int PROMPT = 2;  // no newline, trailing space

    private Scanner scanner;
    private Customer[] tables;
    private GameStats stats;
    private ArrayList<Customer> queue;
    private ArrayList<MenuItem> orders;

    public GameUI(Scanner scanner, Customer[] tables, ArrayList<Customer> queue, ArrayList<MenuItem> orders, GameStats stats) {
        this.scanner = scanner;
        this.tables = tables;
        this.stats = stats;
        this.queue = queue;
        this.orders = orders;
    }

    public int readInt() {
        while (!this.scanner.hasNextInt()) {
            print("input.notNumber");
            this.scanner.next();
        }
        return this.scanner.nextInt();
    }

    public int readIntInRange(int min, int max, String invalidKey) {
        while (true) {
            int choice = readInt();
            if (choice >= min && choice <= max) {
                return choice;
            }
            print(invalidKey);
        }
    }

    public int chooseWaiterAction() {
        print("waiter.menu.title");
        print("waiter.menu.seat");
        print("waiter.menu.takeOrder");
        print("waiter.menu.serve");
        print("waiter.menu.wait");
        return readIntInRange(1, 4, "input.invalidChoice");
    }

    public int chooseChefAction() {
        print("chef.menu.title");
        print("chef.menu.prepare");
        print("chef.menu.wait");
        return readIntInRange(1, 2, "input.invalidChoice");
    }

    public int chooseTableToOrder(int numberOfTables) {
        print("waiter.prompt.table", numberOfTables);
        return readIntInRange(1, numberOfTables, "input.invalidTable") - 1;
    }

    public int chooseOrderToServe(int numberOfOrders) {
        print("waiter.prompt.serve");
        return readIntInRange(1, numberOfOrders, "input.invalidOrder") - 1;
    }

    public int chooseOrderToPrepare(int numberOfOrders) {
        print(PROMPT, "chef.prompt.prepare");
        return readIntInRange(1, numberOfOrders, "input.invalidOrder") - 1;
    }

    public void displayTurnStart(int turn, int maxTurns,
                                List<String> waiterTasks, List<String> chefTasks) {
        System.out.println();
        print("turn.header", turn, maxTurns);
        print("turn.summary", this.stats.getRevenue(), formatAverageSatisfaction(this.stats.getAverageSatisfaction()));

        print("turn.queue.title");
        if (this.queue.isEmpty()) {
            print(INDENT, "list.empty");
        }
        for (Customer customer : this.queue) {
            print(INDENT, "turn.queue.entry", customer.getName(), customer.getSatisfaction());
        }

        print("turn.tables.title");
        for (int i = 0; i < this.tables.length; i++) {
            if (this.tables[i] == null) {
                print(INDENT, "turn.table.empty", i + 1);
            } else {
                print(INDENT, "turn.table.occupied", i + 1, this.tables[i].getName(),
                        this.tables[i].getStatus(), this.tables[i].getSatisfaction());
            }
        }

        print("turn.orders.title");
        if (this.orders.isEmpty()) {
            print(INDENT, "list.none");
        }
        for (int i = 0; i < this.orders.size(); i++) {
            MenuItem order = this.orders.get(i);
            print(INDENT, "turn.orders.entry", i + 1, order.getName(), order.getTableNo() + 1,
                    order.getStatus(), order.getPreparationUnits());
        }

        print("turn.tasks.title");
        print(INDENT, "turn.tasks.waiter", String.join(", ", waiterTasks));
        print(INDENT, "turn.tasks.chef", String.join(", ", chefTasks));
        System.out.println();
    }

    public void displayCustomerArrived(Customer customer) {
        print("arrival.joined", customer.getName());
    }

    public void displayNoCustomersInQueue() {
        print("seat.noQueue");
    }

    public void displayNoAvailableTables() {
        print("seat.noTables");
    }

    public void displayCustomerSeated(int tableIndex) {
        print("seat.done", tableIndex + 1);
    }

    public void displayNoCustomersToOrder() {
        print("order.noneWaiting");
    }

    public void displayNoCustomerAtTableToOrder(int tableIndex) {
        print("order.noCustomerAtTable", tableIndex + 1);
    }

    public void displayOrderTaken(int tableIndex, MenuItem order) {
        print("order.taken", tableIndex + 1, order.getName());
    }

    public void displayNoOrdersReady() {
        print("serve.noneReady");
    }

    public void displayReadyOrders(List<Integer> orderIndexes) {
        print("serve.readyTitle");
        for (int orderIndex : orderIndexes) {
            print("serve.readyEntry", orderIndex + 1);
        }
    }

    public void displayOrderNotReady(int orderIndex) {
        print("serve.notReady", orderIndex + 1);
    }

    public void displayOrderServed(int orderIndex, int tableIndex) {
        print("serve.done", orderIndex + 1, tableIndex + 1);
    }

    public void displayNoOrdersToPrepare() {
        print("cook.noOrders");
    }

    public void displayOrdersToPrepare() {
        print("cook.listTitle");
        for (int i = 0; i < this.orders.size(); i++) {
            MenuItem order = this.orders.get(i);
            print("cook.listEntry", i + 1, order.getName(), order.getStatus());
        }
    }

    public void displayOrderPrepared(MenuItem order) {
        print("cook.done", order.getName(), order.getPreparationUnits());
    }

    public void displayPayment(int tableIndex, MenuItem order) {
        print("pay.done", tableIndex + 1, order.getName(), order.getPrice());
    }

    public void displayGameOver(int turnsUsed, int maxTurns, boolean victory) {
        System.out.println();
        print("gameover.title");
        print("gameover.turns", turnsUsed, maxTurns);
        print("gameover.served", this.stats.getServedCustomers());
        print("gameover.paid", this.stats.getPaidCustomers());
        print("gameover.unhappy", this.stats.getUnhappyDepartures());
        print("gameover.revenue", this.stats.getRevenue());
        print("gameover.average", formatAverageSatisfaction(this.stats.getAverageSatisfaction()));
        print(victory ? "gameover.victory" : "gameover.defeat");
    }

    /**
     * Serves as an intermediate function to call the Messages object and has flags for indetation and no newline
     */
    private void print(int flags, String key, Object... args) {
        String text = Messages.get(key, args);
        if ((flags & INDENT) != 0) {
            text = "  " + text;
        }
        if ((flags & PROMPT) != 0) {
            System.out.print(text + " ");
        } else {
            System.out.println(text);
        }
    }

    private void print(String key, Object... args) {
        print(0, key, args);
    }

    /**
     * Just a formatter for the satisfaction and returns a string
     */
    private String formatAverageSatisfaction(Float averageSatisfaction) {
        if (averageSatisfaction == null) {
            return Messages.get("gameover.notAvailable");
        }
        return String.format("%.2f", averageSatisfaction);
    }
}