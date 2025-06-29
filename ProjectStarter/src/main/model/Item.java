package model;

/**
 * Item is an object that includes all data needed for each item in the crafting
 * tree
 * Item includes a name, a category, a crafting method, and a value (in units)
 */
public class Item {
    String name;
    Category category;
    Method method;
    int amount;
    int cost;

    // EFFECTS: Initializes Item with a name, category, method, an amount, and cost
    public Item(String name, Category category, Method method, int amount, int cost) {
        this.name = name;
        this.category = category;
        this.method = method;
        this.amount = amount;
        this.cost = cost;
    }

    // Copy constructor
    public Item(Item item) {
        this.name = item.name;
        this.category = item.category;
        this.method = item.method;
        this.amount = item.amount;
        this.cost = item.cost;
    }

    public boolean equals(Item item) {
        Boolean sameName = getName().equals(item.getName());
        Boolean sameCategory = getCategory().equals(item.getCategory());
        Boolean sameMethod = getMethod().equals(item.getMethod());
        Boolean sameAmount = getAmount() == item.getAmount();
        Boolean sameCost = getCost() == item.getCost();

        if (sameName & sameCategory & sameMethod & sameAmount & sameCost) {
            return true;
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
        EventLog.getInstance().logEvent(new Event("Changed item name to" + name));
    }

    public void setCategory(Category category) {
        this.category = category;
        EventLog.getInstance().logEvent(new Event("Changed category to" + String.valueOf(category)));
    }

    public void setMethod(Method method) {
        this.method = method;
        EventLog.getInstance().logEvent(new Event("Changed method to" + String.valueOf(method)));
    }

    // REQUIRES: amount > 0
    public void setAmount(int amount) {
        this.amount = amount;
        EventLog.getInstance().logEvent(new Event("Changed amount to" + String.valueOf(amount)));
    }

    public void setCost(int cost) {
        this.cost = cost;
        EventLog.getInstance().logEvent(new Event("Changed cost to" + String.valueOf(cost)));
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Method getMethod() {
        return method;
    }

    public int getAmount() {
        return amount;
    }

    public int getCost() {
        return cost;
    }

    // REQUIRES: num >= 0
    // MODIFIES: this
    // EFFECTS: multiplies amount by num
    public void addAmount(int num) {
        amount += num;
        EventLog.getInstance()
                .logEvent(new Event("Added " + String.valueOf(num) + " to amount (total:" + amount + ")"));
    }

    // REQUIRES: num >= 0
    // MODIFIES: this
    // EFFECTS: multiplies amount by num
    public void substractAmount(int num) {
        amount -= num;
        EventLog.getInstance()
                .logEvent(new Event("Subtracted " + String.valueOf(num) + " from amount (total:" + amount + ")"));
    }

    // REQUIRES: num >= 0
    // MODIFIES: this
    // EFFECTS: multiplies amount by num
    public void multiplyAmount(int num) {
        amount *= num;
        EventLog.getInstance()
                .logEvent(new Event("Multiplied " + String.valueOf(num) + " to amount (total:" + amount + ")"));
    }

    // REQUIRES: num > 0,
    // MODIFIES: this
    // EFFECTS: divides amount by num
    public void divideAmount(int num) {
        amount /= num;
        EventLog.getInstance()
                .logEvent(new Event("Divided " + String.valueOf(num) + " from amount (total:" + amount + ")"));
    }

    // REQUIRES: num >= 0
    // MODIFIES: this
    // EFFECTS: multiplies amount by num
    public void addCost(int num) {
        cost += num;
        EventLog.getInstance().logEvent(new Event("Added " + String.valueOf(num) + "vto cost (total:" + cost + ")"));
    }

    // REQUIRES: num >= 0
    // MODIFIES: this
    // EFFECTS: multiplies amount by num
    public void substractCost(int num) {
        cost -= num;
        EventLog.getInstance()
                .logEvent(new Event("Subtracted " + String.valueOf(num) + " from cost (total:" + cost + ")"));
    }

    // REQUIRES: num >= 0
    // MODIFIES: this
    // EFFECTS: multiplies amount by num
    public void multiplyCost(int num) {
        cost *= num;
        EventLog.getInstance()
                .logEvent(new Event("Multiplied " + String.valueOf(num) + " to cost (total:" + cost + ")"));
    }

    // REQUIRES: num > 0,
    // MODIFIES: this
    // EFFECTS: divides amount by num
    public void divideCost(int num) {
        cost /= num;
        EventLog.getInstance()
                .logEvent(new Event("Divided " + String.valueOf(num) + " from cost (total:" + cost + ")"));
    }
}