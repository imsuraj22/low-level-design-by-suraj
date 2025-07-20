import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip {
    private String id;
    private String description;
    private List<User> members;
    private List<Expense> expenses;

    public Trip(String id) {
        this(id, null, null);
    }

    public Trip(String id, String description) {
        this(id, description, null);
    }

    public Trip(String id, String description, List<User> members) {
        // this(id, description);
        if (members == null)
            members = new ArrayList<>(members);
        this.id = id;
        this.description = description;

    }

    public void addUser(User user) {
        members.add(user);
    }

    public void deleteUser(User user) {

        members.remove(user);

    }

    public void addExpense(String expenseName, Double amount, User paidBy, Map<User, Double> users, SplitType splitType,
            String description, String tripId) {
        expenses.add(new Expense(expenseName, amount, paidBy, users, splitType, description, this.id));
    }

    public void addExpense(String expenseName, Double amount, User paidBy, Map<User, Double> users, SplitType splitType,
            String tripId) {
        expenses.add(new Expense(expenseName, amount, paidBy, users, splitType, null, this.id));
    }

    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    Map<User, Map<User, Double>> getExpenseSplit() {
        Map<User, Map<User, Double>> finalSplit = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            Map<User, Double> owes = expense.getBalances();
            User toReceiveUser = expense.getPaidBy();
            if (finalSplit.containsKey(toReceiveUser)) {
                Map<User, Double> temp = finalSplit.get(toReceiveUser);
                for (User user : owes.keySet()) {
                    temp.put(user, owes.get(user));
                }
                finalSplit.put(toReceiveUser, temp);
            } else {
                Map<User, Double> temp = new HashMap<>();
                for (User user : owes.keySet()) {
                    temp.put(user, owes.get(user));
                }
                finalSplit.put(toReceiveUser, temp);
            }
        }
        return finalSplit;
    }

}
