package restaurant.customers;

public enum CustomerStatus {
    WAITING,        // in the queue
    SEATED,         // at a table, order not yet taken
    EATING,         // served, eating
    READY_TO_PAY
}