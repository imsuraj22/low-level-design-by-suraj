import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Expense {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private int id; // auto-generated
    private String name; // was previously String id
    private double amount;
    private User paidBy;
    private String description;
    private SplitType splitType;
    private String tripName;
    private Map<User, Double> balances;
    private Map<User, Double> users;

    public Map<User, Double> getUsers() {
        return users;
    }

    public void setUsers(Map<User, Double> users) {
        this.users = users;
    }

    // Constructor with optional description
    public Expense(String name, double amount, User paidBy, Map<User, Double> users, SplitType splitType,
            String description,
            String tripName) {
        this.id = counter.incrementAndGet(); // auto-incremented
        this.users = new HashMap<>(users);
        this.name = name;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.description = description;
        this.tripName = tripName;
        this.balances = new HashMap<>();

        calculateExpense(users, balances, amount, splitType);

    }

    void calculateExpense(Map<User, Double> users, Map<User, Double> balances, Double amount, SplitType splitType) {
        balances.clear();
        if (splitType == SplitType.EQUAL) {

            EqualSplit equalSplit = EqualSplit.getInstance();
            equalSplit.perfromSplit(users, balances, amount);
        } else if (splitType == SplitType.EXACT) {
            ExactSplit exactSplit = ExactSplit.getInstance();
            exactSplit.perfromSplit(users, balances, amount);
        } else {
            PercentageSplit percentageSplit = PercentageSplit.getInstance();
            percentageSplit.perfromSplit(users, balances, amount);
        }
    }

    // Overloaded constructor without description
    public Expense(String name, double amount, User paidBy, Map<User, Double> users, SplitType splitType,
            String tripName) {
        this(name, amount, paidBy, users, splitType, null, tripName);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public String getDescription() {
        return description;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public String getTripName() {
        return tripName;
    }

    public Map<User, Double> getBalances() {
        return balances;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setBalances(Map<User, Double> balances) {
        this.balances = balances;
    }

    void addUser(User user, Double amDouble) {
        users.put(user, amDouble);
        calculateExpense(users, balances, amDouble, this.splitType);
    }

    void deleteUser(User user) {
        users.remove(user);
        calculateExpense(users, balances, this.amount, this.splitType);
    }

}
