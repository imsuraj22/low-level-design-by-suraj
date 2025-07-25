import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Split {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final String id;
    private String description;
    private List<User> members;
    private List<Expense> expenses;

    public Split() {
        this(null, new ArrayList<>());
    }

    public Split(String description) {
        this(description, new ArrayList<>());
    }

    public Split(String description, List<User> members) {
        this.id = "SPLIT-" + counter.incrementAndGet(); // Auto-generated ID
        this.description = description;
        this.members = members != null ? members : new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public void addUser(User user) {
        members.add(user);
    }

    public void deleteUser(User user) {
        members.remove(user);
    }

    public void addExpense(String expenseName, Double amount, User paidBy, Map<User, Double> users, SplitType splitType,
            String description) {
        expenses.add(new Expense(expenseName, amount, paidBy, users, splitType, description));
    }

    public void addExpense(String expenseName, Double amount, User paidBy, Map<User, Double> users,
            SplitType splitType) {
        expenses.add(new Expense(expenseName, amount, paidBy, users, splitType));
    }

    public void deleteExpenseById(int id) {
        expenses.removeIf(expense -> expense.getId() == id);
    }

    public Expense getExpenseById(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null; // or throw an exception if not found
    }

    public String getId() {
        return id;
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

    public Map<User, Map<User, Double>> getExpenseSplit() {
        Map<User, Map<User, Double>> finalSplit = new HashMap<>();
        for (Expense expense : expenses) {
            Map<User, Double> owes = expense.getBalances();
            User toReceiveUser = expense.getPaidBy();
            finalSplit.putIfAbsent(toReceiveUser, new HashMap<>());
            Map<User, Double> temp = finalSplit.get(toReceiveUser);
            for (Map.Entry<User, Double> entry : owes.entrySet()) {
                temp.put(entry.getKey(), entry.getValue());
            }
        }
        return finalSplit;
    }

    public void printSplitDetails() {
        System.out.println("===== Split ID: " + id + " =====");
        System.out.println("Description: " + (description != null ? description : "No description"));
        System.out.println("Members:");
        if (members != null) {
            for (User user : members) {
                System.out.println(" - " + user.getName());
            }
        }

        System.out.println("===============================================\n");
    }

    // void showExpense() {

    // System.out.println("\nExpenses:");
    // if (expenses != null && !expenses.isEmpty()) {
    // for (Expense expense : expenses) {
    // System.out.println("------------------------------------------------");
    // System.out.println("Expense Name : " + expense.getName());
    // System.out.println("Amount : ₹" + expense.getAmount());
    // System.out.println("Paid By : " + expense.getPaidBy().getName());
    // System.out.println("Split Type : " + expense.getSplitType());
    // System.out.println(
    // "Description : " + (expense.getDescription() != null ?
    // expense.getDescription() : "N/A"));
    // System.out.println("Balances:");
    // for (Map.Entry<User, Double> entry : expense.getBalances().entrySet()) {
    // System.out.println(" " + entry.getKey().getName() + " owes ₹" +
    // entry.getValue());
    // }
    // }
    // } else {
    // System.out.println("No expenses added yet.");
    // }
    // System.out.println("===============================================\n");
    // }
}
