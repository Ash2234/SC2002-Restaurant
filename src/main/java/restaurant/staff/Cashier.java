package restaurant.staff;

import java.util.ArrayList;
import java.util.Queue;

import restaurant.GameStats;
import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.menu.MenuItem;
import restaurant.menu.MenuItemStatus;
import restaurant.ui.GameUI;

/**
 * The Cashier collects payment automatically, with no choice from the user:
 * at most one customer per turn, the one who has been ready to pay the longest
 *
 * The main method is takeAction(), which takes the next table from the cashier queue and charges that table's order
 * The cashier queue stores the table numbers of Customers that are ready to pay
 *
 * This class applies the rules for payment and updates the shared tables, order list and game statistics
 * Table indexes used here are 0-based.
 */
public class Cashier {
    private Queue<Integer> cashierQueue;
    private Customer[] tables = new Customer[2];
    private ArrayList<MenuItem> orders = new ArrayList<>();
    private GameUI ui;
    private GameStats stats;


    public Cashier(Queue<Integer> cashierQueue, Customer[] tables, ArrayList<MenuItem> orders, GameUI ui, GameStats stats) {
        this.cashierQueue = cashierQueue;
        this.tables = tables;
        this.orders = orders;
        this.ui = ui;
        this.stats = stats;
    }

    public float getTotalRevenue() {
        return this.stats.getRevenue();
    }

    /**
     * The 'oldest' customer with READY_TO_PAY is determined by the cashier queue
     * The cashier queue uses the builtin Java queue to ensure that its enqueued and dequeued properly
     * 
     * It then updates the average satisfaction and total revenue accordingly in GameStats
     */
    public void takeAction() {
        Integer tableNo = this.cashierQueue.poll();
        if (tableNo == null) {
            return;
        }

        // Can add check for discounts and stuff here for later stages
        for (MenuItem order : this.orders) {
            if (order.getTableNo() == tableNo) {
                Customer customer = this.tables[tableNo];
                this.stats.recordPayment(order.getPrice(), customer.getSatisfaction());
                order.setStatus(MenuItemStatus.PAID);
                this.tables[tableNo].setStatus(CustomerStatus.LEAVING);
                this.ui.displayPayment(tableNo, order);
                return;            
            }
        }
    }
}