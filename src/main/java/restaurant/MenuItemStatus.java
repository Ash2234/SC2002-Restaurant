package restaurant;
public enum MenuItemStatus {
    MENU,
    ORDERED,        // order taken, food not yet served
    READY,          // food is ready to be served
    SERVED,          // food has been served to the customer
    PAID            // customer has paid for the order
}