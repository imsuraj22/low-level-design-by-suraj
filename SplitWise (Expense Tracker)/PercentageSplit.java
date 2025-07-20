import java.util.Map;

public class PercentageSplit implements Split {

    // 1. Private static final instance
    private static final PercentageSplit instance = new PercentageSplit();

    // 2. Private constructor to prevent instantiation
    private PercentageSplit() {
    }

    // 3. Public method to get the single instance
    public static PercentageSplit getInstance() {
        return instance;
    }

    // 4. Implement the interface method
    @Override
    public void perfromSplit(Map<User, Double> users, Map<User, Double> balances, Double amount) {
        // Percentage split logic here
        for (User user : users.keySet()) {
            Double percentage = users.get(user);
            Double splitAmount = (amount * percentage) / 100.0;
            balances.put(user, splitAmount);
        }
    }
}
