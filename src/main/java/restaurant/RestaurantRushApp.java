package restaurant;

import java.util.ArrayList;
import java.util.Scanner;

import restaurant.customers.Customer;
import restaurant.staff.Waiter;

public class RestaurantRushApp {
    private static ArrayList<Customer> queue = new ArrayList<>();
    private static Customer[] tables = new Customer[2]; // Can change to settings can be added next time
    private static ArrayList<MenuItem> orders = new ArrayList<>();

    private static final MenuItem[] MENU_ITEMS = initMenuItems();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        float revenue = 0f;
        int paidCustomers = 0;
        float averageSatisfaction = 0;
        int unhappyDepartures = 0;

        Waiter waiter = new Waiter(queue, tables, orders, scanner);

        int turn;
        for (turn = 1; turn <= 16; turn++) { // Number of turns can be changed to settings can be added next time
            System.out.println("Turn " + turn + ":");

            // Player chooses what the waiter does
            waiter.takeAction();

            // Player choose what the chef does

            // Update the satisfaction of customers in the queue and at the tables

            if (isVictory(revenue, paidCustomers, averageSatisfaction)) {
                break;
            }
        }
    }

    public static MenuItem[] initMenuItems() {
        MenuItem[] menuItems = new MenuItem[5];
        menuItems[0] = new MenuItem("Salad", 9.00f, 1);
        menuItems[1] = new MenuItem("Burger", 15.00f, 1);
        menuItems[2] = new MenuItem("Pasta", 18.00f, 2);
        return menuItems;
    }

    public static boolean isVictory(float revenue, int paidCustomers, float averageSatisfaction) {
        if (revenue >= 45f && paidCustomers >= 4 && averageSatisfaction >= 60f) {
            return true;
        }
        return false;
    }
}
