package restaurant;

import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Scanner;

import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.customers.Regular;
import restaurant.staff.Waiter;
import restaurant.staff.Chef;
import restaurant.staff.Cashier;
import restaurant.ui.GameUI;

public class RestaurantRushApp {
    private static final int MAX_TURNS = 18; // Can change to settings can be added next time
    private static final int[] ARRIVAL_TURNS = {1, 4, 7, 10, 13, 16};

    private static ArrayList<Customer> queue = new ArrayList<>();
    private static Customer[] tables = new Customer[2]; // Can change to settings can be added next time
    private static ArrayList<MenuItem> orders = new ArrayList<>();
    private static Queue<Integer> cashierQueue = new ArrayDeque<>();

    private static final MenuItem[] MENU_ITEMS = initMenuItems();

    private static GameUI ui = new GameUI(new Scanner(System.in));

    public static void main(String[] args) {
        int paidCustomers = 0;
        int totalPaidSatisfaction = 0;
        int unhappyDepartures = 0;
        int servedCustomers = 0;

        Waiter waiter = new Waiter(queue, tables, orders, ui);
        Chef chef = new Chef(orders, ui);
        Cashier cashier = new Cashier(cashierQueue, tables, orders, 0f, ui);

        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            // Arrivals
            for (int arrivalTurn : ARRIVAL_TURNS) {
                if (arrivalTurn == turn) {
                    Customer customer = new Regular(MENU_ITEMS);
                    customer.setStatus(CustomerStatus.WAITING);
                    queue.add(customer);
                    ui.displayCustomerArrived(customer);
                }
            }

            ui.displayTurnStart(turn, MAX_TURNS, cashier.getTotalRevenue(), getAverageSatisfaction(paidCustomers, totalPaidSatisfaction),
                    queue, tables, orders, waiter.getAvailableTasks(), chef.getAvailableTasks());

            // Player chooses what the waiter and chef do
            waiter.takeAction();
            chef.takeAction();

            // Collect money from the oldest customer
            cashier.takeAction();

            // Then customers who finished a full turn of eating become ready to pay (they pay next turn at the earliest)
            // and customers served this turn start eating
            for (int i = 0; i < tables.length; i++) {
                if (tables[i] == null) {
                    continue;
                }

                if (tables[i].getStatus() == CustomerStatus.EATING) {
                    tables[i].setStatus(CustomerStatus.READY_TO_PAY);
                    cashierQueue.add(i);
                } else if (tables[i].getStatus() == CustomerStatus.SERVED) {
                    tables[i].setStatus(CustomerStatus.EATING);
                    servedCustomers++;
                }
            }

            // Update the satisfaction of customers in the queue and at the tables
            for (Customer customer : tables) {
                if (customer != null) {
                    customer.satisfactionDecay();
                }
            }

            for (Customer customer : queue) {
                customer.satisfactionDecay();
            }

            int queueSizeBefore = queue.size();
            queue.removeIf(customer -> customer.getStatus() == CustomerStatus.LEAVING);
            unhappyDepartures += queueSizeBefore - queue.size();

            // Free tables of customers who have paid or given up
            for (int i = 0; i < tables.length; i++) {
                if (tables[i] != null && tables[i].getStatus() == CustomerStatus.LEAVING) {
                    if (hasPaid(i)) {
                        paidCustomers++;
                        totalPaidSatisfaction += tables[i].getSatisfaction();
                    } else {
                        unhappyDepartures++;
                    }
                    removeOrder(i);
                    tables[i] = null;
                }
            }
        }

        // Step 6: Result, the game always runs all turns
        float revenue = cashier.getTotalRevenue();
        Float averageSatisfaction = getAverageSatisfaction(paidCustomers, totalPaidSatisfaction);
        boolean victory = averageSatisfaction != null && isVictory(revenue, paidCustomers, averageSatisfaction);

        ui.displayGameOver(MAX_TURNS, MAX_TURNS, servedCustomers, paidCustomers, unhappyDepartures, revenue, averageSatisfaction, victory);
    }

    // Returns null when no customer has paid yet
    private static Float getAverageSatisfaction(int paidCustomers, int totalPaidSatisfaction) {
        if (paidCustomers == 0) {
            return null;
        }
        return (float) totalPaidSatisfaction / paidCustomers;
    }

    public static MenuItem[] initMenuItems() {
        MenuItem[] menuItems = new MenuItem[3];
        menuItems[0] = new MenuItem("Salad", 9.00f, 1);
        menuItems[1] = new MenuItem("Burger", 15.00f, 1);
        menuItems[2] = new MenuItem("Pasta", 18.00f, 2);
        return menuItems;
    }

    public static boolean isVictory(float revenue, int paidCustomers, float averageSatisfaction) {
        return revenue >= 45f && paidCustomers >= 4 && averageSatisfaction >= 60f;
    }

    private static boolean hasPaid(int tableNo) {
        for (MenuItem order : orders) {
            if (order.getTableNo().equals(tableNo) && order.getStatus() == MenuItemStatus.PAID) {
                return true;
            }
        }
        return false;
    }

    public static void removeOrder(int tableNo) {
        orders.removeIf(order -> order.getTableNo().equals(tableNo));
    }
}
