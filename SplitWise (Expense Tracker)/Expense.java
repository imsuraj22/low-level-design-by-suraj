import java.util.Map;

public class Expense {
    private String id;
    private double amount;
    private User paidBy;
    private String description;
    private SplitType SplitType;
    private Trip trip;
    private Map<User, Double> balances;

    public Expense(String id, double amount, User paidBy, SplitType splitType, String description, Trip trip) {
        this.id = id;
        this.amount = amount;
        this.paidBy = paidBy;
        this.SplitType = splitType;
        this.description = description;
        this.trip = trip;
    }

    public Expense(String id, double amount, User paidBy, SplitType splitType, Trip trip) {
        this(id, amount, paidBy, splitType, null, trip);
    }

    public void setBalances(Map<User, Double> balances) {
        this.balances = balances;
    }

    public Map<User, Double> getBalances() {
        return balances;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SplitType getSplitType() {
        return SplitType;
    }

    public void setSplitType(SplitType splitType) {
        SplitType = splitType;
    }
}
