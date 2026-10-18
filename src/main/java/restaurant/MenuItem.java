package restaurant;

public class MenuItem {
    private String name;
    private float price;
    private int preparationUnits;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void cook() {
        if (this.preparationUnits > 0) {
            this.preparationUnits--;
            if (this.preparationUnits == 0) {
                this.status = "ready";
            }
        }
    }
}