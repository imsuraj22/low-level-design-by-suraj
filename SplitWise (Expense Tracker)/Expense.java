import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Expense {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private int id; // auto-generated
    private String name;
    private double amount;
    private User paidBy;
    private String description;
    private SplitType splitType;
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
            String description) {
        this.id = counter.incrementAndGet(); // auto-incremented
        this.users = new HashMap<>(users);
        this.name = name;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.description = description;
        this.balances = new HashMap<>();

        calculateExpense(users, balances, amount, splitType);
    }

    // Overloaded constructor without description
    public Expense(String name, double amount, User paidBy, Map<User, Double> users, SplitType splitType) {
        this(name, amount, paidBy, users, splitType, null);
    }

    public void updateExpense(String name, double amount, User paidBy, Map<User, Double> users,
            SplitType splitType, String description) {
        this.name = name;
        this.amount = amount;
        this.paidBy = paidBy;
        this.users = new HashMap<>(users);
        this.splitType = splitType;
        this.description = description;
        this.balances = new HashMap<>();
        calculateExpense(this.users, this.balances, this.amount, this.splitType);
    }

    void calculateExpense(Map<User, Double> users, Map<User, Double> balances, Double amount, SplitType splitType) {
        balances.clear();
        if (splitType == SplitType.EQUAL) {
            EqualSplit equalSplit = EqualSplit.getInstance();
            equalSplit.performSplit(users, balances, amount);
        } else if (splitType == SplitType.EXACT) {
            ExactSplit exactSplit = ExactSplit.getInstance();
            exactSplit.performSplit(users, balances, amount);
            Double val = 0.0;
            for (User user : users.keySet()) {
                val += users.get(user);
            }
            System.out.println("val is: " + val);
            this.amount = val;
        } else {
            PercentageSplit percentageSplit = PercentageSplit.getInstance();
            percentageSplit.performSplit(users, balances, amount);
        }
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

    public Map<User, Double> getBalances() {
        return balances;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        calculateExpense(users, balances, amount, splitType);
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
        calculateExpense(users, balances, amount, splitType);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
        calculateExpense(users, balances, amount, splitType);
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

    public void showExpense() {
        System.out.println("-------- Expense Details --------");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Amount: " + amount);
        System.out.println("Paid By: " + paidBy.getName());
        System.out.println("Description: " + (description == null ? "No description" : description));
        System.out.println("Split Type: " + splitType);

        // System.out.println("\nUsers involved:");
        // for (Map.Entry<User, Double> entry : users.entrySet()) {
        // System.out.println("- " + entry.getKey().getName());
        // if (splitType == SplitType.EXACT || splitType == SplitType.PERCENT) {
        // System.out.println(" → Value: " + entry.getValue());
        // }
        // }

        System.out.println("\nBalances:");
        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            if (entry.getKey().getId() != paidBy.getId()) {
                String balanceStr = String.format("%.2f", entry.getValue());
                System.out
                        .println(
                                "- " + entry.getKey().getName() + " owes Rs " + balanceStr + " to " + paidBy.getName());
            }
        }
        System.out.println("----------------------------------\n");
    }
}
