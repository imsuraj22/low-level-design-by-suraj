import java.util.Map;

public interface SplitStrategy {
    public void performSplit(Map<User, Double> users, Map<User, Double> balances, Double amount);
}
