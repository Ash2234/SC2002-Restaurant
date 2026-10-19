package restaurant.staff;

import java.util.ArrayList;
import java.util.Queue;

import restaurant.MenuItem;
import restaurant.MenuItemStatus;
import restaurant.customers.Customer;
import restaurant.customers.CustomerStatus;
import restaurant.ui.GameUI;

public class Cashier {
    private Queue<Integer> cashierQueue;
    private Customer[] tables = new Customer[2];
    private ArrayList<MenuItem> orders = new ArrayList<>();
    private float totalRevenue;
    private GameUI ui;


    public Cashier(Queue<Integer> cashierQueue, Customer[] tables, ArrayList<MenuItem> orders, float totalRevenue, GameUI ui) {
        this.cashierQueue = cashierQueue;
        this.tables = tables;
        this.orders = orders;
        this.totalRevenue = totalRevenue;
        this.ui = ui;
    }

    public float getTotalRevenue() {
        return this.totalRevenue;
    }

    public void takeAction() {
        Integer tableNo = this.cashierQueue.poll();
        if (tableNo == null) {
            return;
        }

        // Can add check for discounts and stuff here for later stages
        for (MenuItem order : this.orders) {
            if (order.getTableNo() == tableNo) {
                this.totalRevenue += order.getPrice();
                order.setStatus(MenuItemStatus.PAID);
                this.tables[tableNo].setStatus(CustomerStatus.LEAVING);
                this.ui.displayPayment(tableNo, order);
                return;            
            }
        }
    }
}