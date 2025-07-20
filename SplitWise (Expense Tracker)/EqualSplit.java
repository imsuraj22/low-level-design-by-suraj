import java.util.Map;

public class EqualSplit implements Split {

    // 1. Private static final instance (eager initialization)
    private static final EqualSplit instance = new EqualSplit();

    // 2. Private constructor
    private EqualSplit() {
    }

    // 3. Public method to access the single instance
    public static EqualSplit getInstance() {
        return instance;
    }

    // 4. Implementation of interface method
    @Override
    public void perfromSplit(Map<User, Double> users, Map<User, Double> balances, Double amount) {
        // Equal split logic here
        int n = users.size();
        Double SplitAmount = amount / n;
        for (User user : users.keySet()) {
            balances.put(user, SplitAmount);
        }

    }
}
