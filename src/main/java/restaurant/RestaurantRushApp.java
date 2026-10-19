package restaurant;

import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Scanner;

import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.customers.Regular;
import restaurant.menu.MenuItem;
import restaurant.menu.MenuItemStatus;
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
    private static GameStats stats = new GameStats();

    private static final MenuItem[] MENU_ITEMS = initMenuItems();

    private static GameUI ui = new GameUI(new Scanner(System.in), tables, queue, orders, stats);

    public static void main(String[] args) {
        Waiter waiter = new Waiter(queue, tables, orders, ui);
        Chef chef = new Chef(orders, ui);
        Cashier cashier = new Cashier(cashierQueue, tables, orders, ui, stats);

        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            // Arrivals
            spawnArrivals(turn);

            ui.displayTurnStart(turn, MAX_TURNS, waiter.getAvailableTasks(), chef.getAvailableTasks());

            // Player chooses what the waiter and chef do
            waiter.takeAction();
            chef.takeAction();

            // Collect money from the oldest customer
            cashier.takeAction();

            advanceEating();
            applyWaitingLoss();
            removeAbandonedFromQueue();
            freeTables();
        }

        ui.displayGameOver(MAX_TURNS, MAX_TURNS, isVictory(stats));
    }

    private static void spawnArrivals(int turn) {
        for (int arrivalTurn : ARRIVAL_TURNS) {
            if (arrivalTurn == turn) {
                Customer customer = new Regular(MENU_ITEMS);
                customer.setStatus(CustomerStatus.WAITING);
                queue.add(customer);
                ui.displayCustomerArrived(customer);
            }
        }
    }

    private static void advanceEating() {
        for (int i = 0; i < tables.length; i++) {
            if (tables[i] == null) {
                continue;
            }

            if (tables[i].getStatus() == CustomerStatus.EATING) {
                tables[i].setStatus(CustomerStatus.READY_TO_PAY);
                cashierQueue.add(i);
            } else if (tables[i].getStatus() == CustomerStatus.SERVED) {
                tables[i].setStatus(CustomerStatus.EATING);
                stats.recordServed();
            }
        }
    }

    private static void applyWaitingLoss() {
        for (Customer customer : tables) {
            if (customer != null) {
                customer.satisfactionDecay();
            }
        }

        for (Customer customer : queue) {
            customer.satisfactionDecay();
        }
    }

    private static void removeAbandonedFromQueue() {
        for (int i = queue.size() - 1; i >= 0; i--) {
            if (queue.get(i).getStatus() == CustomerStatus.LEAVING) {
                queue.remove(i);
                stats.recordUnhappyDeparture();
            }
        }
    }

    private static void freeTables() {
        for (int i = 0; i < tables.length; i++) {
            if (tables[i] != null && tables[i].getStatus() == CustomerStatus.LEAVING) {
                if (!hasPaid(i)) {
                    stats.recordUnhappyDeparture();
                }
                removeOrder(i);
                tables[i] = null;
            }
        }
    }

    public static MenuItem[] initMenuItems() {
        MenuItem[] menuItems = new MenuItem[3];
        menuItems[0] = new MenuItem("Salad", 9.00f, 1);
        menuItems[1] = new MenuItem("Burger", 15.00f, 1);
        menuItems[2] = new MenuItem("Pasta", 18.00f, 2);
        return menuItems;
    }

    public static boolean isVictory(GameStats s) {
        Float avg = s.getAverageSatisfaction();
        return s.getRevenue() >= 45f && s.getPaidCustomers() >= 4 && avg != null && avg >= 60f;
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
