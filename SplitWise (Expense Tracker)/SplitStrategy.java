import java.util.Map;

public interface SplitStrategy {
    public void perfromSplit(Map<User, Double> users, Map<User, Double> balances, Double amount);
}
