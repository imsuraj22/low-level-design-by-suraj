import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class SplitService {

    Scanner sc;
    List<User> users;
    List<Split> splits;
    ExpenseService expenseService;
    UserService userService;

    public SplitService() {

    }

    public SplitService(Scanner sc, List<User> users, List<Split> splits, ExpenseService expenseService,
            UserService userService) {
        this.sc = sc;
        this.users = users;
        this.splits = splits;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    String addSplit() {
        List<User> usersToAdd = new ArrayList<>();

        System.out.println("Enter the split name/description:");
        String description = sc.nextLine().trim();

        // If no users yet in global list
        if (users.isEmpty()) {
            System.out.println("No users found. You need to add users to create the split.");
            System.out.println("You can keep adding users one by one. Press Enter when you're done.");

            while (true) {
                System.out.print("Press Enter to stop or type name to add a new user: ");
                String line = sc.nextLine().trim();
                if (line.isEmpty())
                    break;

                User newUser = new User(line);
                users.add(newUser); // add to global list
                usersToAdd.add(newUser); // add to current split
            }

        } else {
            // Existing users
            System.out.println("Choose users from the following list to add to the split:");
            for (User u : users) {
                System.out.println("ID: " + u.getId() + " | Name: " + u.getName() + " | Email: " + u.getEmail());
            }

            System.out.println("Enter user IDs one by one to add (press Enter to stop):");
            while (true) {
                String line = sc.nextLine().trim();
                if (line.isEmpty())
                    break;

                try {
                    int id = Integer.parseInt(line);
                    Optional<User> found = users.stream().filter(u -> u.getId() == id).findFirst();
                    if (found.isPresent()) {
                        usersToAdd.add(found.get());
                    } else {
                        System.out.println("❌ Invalid ID, try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input, enter a valid user ID or press Enter to stop.");
                }
            }

            // Allow adding new users too
            System.out.println("You can add new users as well.");

            while (true) {
                System.out.print("Press Enter to stop or type anything to add a new user: ");
                String line = sc.nextLine().trim();
                if (line.isEmpty())
                    break;

                User newUser = new User(line);
                users.add(newUser); // add to global list
                usersToAdd.add(newUser); // add to current split
            }
        }

        // Create and add the split
        Split newSplit = new Split(description, usersToAdd);
        splits.add(newSplit);
        System.out.println("✅ Split created with " + usersToAdd.size() + " user(s).");
        System.out.println("Enter 1 to add Expnese");
        String input = sc.nextLine();
        if (input.equals("1"))
            expenseService.addExpense(newSplit);
        return newSplit.getId();
    }

    void showSplits() {
        for (int i = 0; i < splits.size(); i++) {
            splits.get(i).printSplitDetails();

            // Ask if they want to see expenses
            System.out.println("Enter 1 to see all the expenses of this split");
            System.out.println("Enter 2 to delete this split");
            System.out.println("Enter 3 to edit this split");
            System.out.println("Enter 4 to contunue");
            System.out.println("Enter 5 to exit");

            String input = sc.nextLine(); // safer than charAt(0)
            if (input.equals("1")) {
                Split split = splits.get(i);
                List<Expense> expenses = split.getExpenses();
                for (int j = 0; j < expenses.size(); j++) {
                    expenses.get(j).showExpense();

                }
                System.out.println("Enter 1 to Edit an expense");
                System.out.println("Enter 2 to delete an expense");
                System.out.println("Enter 3 to add an expense");
                System.out.println("Press Enter to exit");
                input = sc.nextLine();
                // String expenseInput = sc.nextLine();
                if (input.equals("1")) {
                    // call method to edit the expense
                    System.out.println("Enter Edit ID of expense to edit");
                    input = sc.nextLine();
                    int v = Integer.parseInt(input);
                    expenseService.editExpense(split.getExpenseById(v), users);

                } else if (input.equals("2")) {
                    System.out.println("Enter Edit ID of expense to delete");
                    input = sc.nextLine();
                    int v = Integer.parseInt(input);
                    split.deleteExpenseById(v);
                } else if (input.equals("3")) {
                    expenseService.addExpense(split);
                    ;
                } else {
                    // pressed Enter or other key
                    // return;
                }
            } else if (input.equals("2")) {
                splits.remove(i);
            } else if (input.equals("3")) {
                editSplit(splits.get(i));
            } else if (input.equals("4")) {
                continue;
            } else {
                // System.out.println("Returning...");
                return;
            }

        }
    }

    public void editSplit(Split split) {
        ReentrantLock lock = split.getLock();
        lock.lock();
        try {
            System.out.println("Editing Split Details:");

            // Update description
            System.out.print("Current description: \"" + split.getDescription()
                    + "\"\nEnter new description (press Enter to skip): ");
            String newDesc = sc.nextLine().trim();
            if (!newDesc.isEmpty()) {
                split.setDescription(newDesc);
            }

            // Show existing members
            System.out.println("\nCurrent members in the split:");
            List<User> members = split.getMembers();
            for (int i = 0; i < members.size(); i++) {
                System.out.println((i + 1) + ". " + members.get(i).getName());
            }

            // Option to remove members
            System.out.print("Do you want to remove any members? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                List<User> toRemove = new ArrayList<>();
                for (User user : members) {
                    System.out.print("Remove " + user.getName() + "? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        toRemove.add(user);
                    }
                }
                members.removeAll(toRemove);
            }

            // Option to add members
            System.out.print("\nDo you want to add new members? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                while (true) {
                    System.out.println("Choose option:");
                    for (int i = 0; i < users.size(); i++) {
                        System.out.println((i + 1) + ". " + users.get(i).getName());
                    }
                    System.out.println((users.size() + 1) + ". Create New User");
                    System.out.println("0. Stop adding users");

                    int choice;
                    try {
                        choice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                        continue;
                    }

                    if (choice == 0)
                        break;
                    User selectedUser = null;

                    if (choice >= 1 && choice <= users.size()) {
                        selectedUser = users.get(choice - 1);
                    } else if (choice == users.size() + 1) {
                        selectedUser = userService.createNewUser();
                        users.add(selectedUser);
                    } else {
                        System.out.println("Invalid choice.");
                        continue;
                    }

                    if (members.contains(selectedUser)) {
                        System.out.println(selectedUser.getName() + " is already a member.");
                    } else {
                        members.add(selectedUser);
                        System.out.println("✅ " + selectedUser.getName() + " added to the split.");
                    }
                }
            }

            // Save updated members
            split.setMembers(members);

            System.out.println("\n✅ Split updated successfully.\n");
        } finally {
            lock.unlock();
        }
    }
}
