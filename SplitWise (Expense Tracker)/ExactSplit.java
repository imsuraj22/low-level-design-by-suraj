import java.util.Map;

public class ExactSplit implements SplitStrategy {

    // 1. Private static instance
    private static final ExactSplit instance = new ExactSplit();

    // 2. Private constructor to prevent external instantiation
    private ExactSplit() {
    }

    // 3. Public accessor
    public static ExactSplit getInstance() {
        return instance;
    }

    // 4. Method from interface
    @Override
    public void perfromSplit(Map<User, Double> users, Map<User, Double> balances, Double amount) {
        // splitting logic here
        for (User user : users.keySet()) {
            balances.put(user, users.get(user));
        }
    }
}
