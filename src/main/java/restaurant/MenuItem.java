package restaurant;

public class MenuItem {
    private String name;
    private float price;
    private int preparationUnits;
    
    private MenuItemStatus status = MenuItemStatus.MENU;
    private int tableNo;

    public MenuItem(String name, float price, int preparationUnits) {
        this.name = name;
        this.price = price;
        this.preparationUnits = preparationUnits;
    }

    public MenuItem(MenuItem menuItem) {
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
        this.preparationUnits = menuItem.getPreparationUnits();
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getPreparationUnits() {
        return preparationUnits;
    }

    public MenuItemStatus getStatus() {
        return status;
    }

    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public void cook() {
        if (this.preparationUnits > 0) {
            this.preparationUnits--;
            if (this.preparationUnits == 0) {
                this.status = MenuItemStatus.READY;
            }
        }
    }
}