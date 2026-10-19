package restaurant.staff;

import java.util.ArrayList;
import java.util.Queue;

import restaurant.GameStats;
import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.menu.MenuItem;
import restaurant.menu.MenuItemStatus;
import restaurant.ui.GameUI;

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