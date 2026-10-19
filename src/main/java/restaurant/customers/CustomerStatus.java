package restaurant.customers;

public enum CustomerStatus {
    WAITING,        // in the queue
    SEATED,         // at a table, order not yet taken
    SERVED,         // served this turn, starts eating next turn
    EATING,         // served, eating
    READY_TO_PAY,
    LEAVING,         // finished eating, leaving
}