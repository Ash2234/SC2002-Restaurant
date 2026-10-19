package restaurant;

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

    public float getRevenue() { return revenue; }
    public int getServedCustomers() { return servedCustomers; }
    public int getPaidCustomers() { return paidCustomers; }
    public int getUnhappyDepartures() { return unhappyDepartures; }
}