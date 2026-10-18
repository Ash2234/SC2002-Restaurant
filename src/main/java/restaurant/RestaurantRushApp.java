package restaurant;

import restaurant.customers.Customer;

public class RestaurantRushApp {
    public static void main(String[] args) {
        float revenue = 0f;
        int paidCustomers = 0;
        float averageSatisfaction = 0;
        int unhappyDepartures = 0;

        Customer[] queue = new Customer[100]; // Can find maximum number of customers in the queue next time
        Customer[] tables = new Customer[2]; // Can change to settings can be added next time
        MenuItem[] menuItems = initMenuItems();
        MenuItem[] orders = new MenuItem[100]; // Can find maximum number of orders next time

        int turn;
        for (turn = 1; turn <= 16; turn++) { // Number of turns can be changed to settings can be added next time
            System.out.println("Turn " + turn + ":");

            

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
