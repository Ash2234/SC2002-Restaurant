package restaurant;

/**
 * Maintains the values of:
 * revenue, customers served, customers who paid, unhappy departures, and the total satisfaction of paid customers
 *
 * A single instance is shared by the Cashier, the game loop and the UI, so they all read and write from the same object
 * The average satisfaction is calculated from paid customers only, and is null until the first customer pays.
 */
public class GameStats {
    private float revenue;
    private int servedCustomers;
    private int paidCustomers;
    private int unhappyDepartures;
    private int totalSatisfaction;

    public void recordServed() {
        servedCustomers++;
    }

    public void recordPayment(float bill, int satisfaction) {
        revenue += bill;
        paidCustomers++;
        totalSatisfaction += satisfaction;
    }

    public void recordUnhappyDeparture() {
        unhappyDepartures++;
    }

    public Float getAverageSatisfaction() {
        if (paidCustomers == 0) {
            return null;
        }
        return (float) totalSatisfaction / paidCustomers;
    }

    public float getRevenue() {
        return revenue;
    }

    public int getServedCustomers() {
        return servedCustomers;
    }

    public int getPaidCustomers() {
        return paidCustomers;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }
}