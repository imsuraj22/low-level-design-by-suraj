import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    static Scanner sc;
    static List<User> users;
    static List<Split> splits;

    public static void main(String args[]) {
        sc = new Scanner(System.in);
        users = new ArrayList<>();
        splits = new ArrayList<>();
        String acceptInput = "";
        while (true) {
            System.out.println(
                    "Enter 1 to create a new split\nEnter 2 to see all the splits\nPress Enter without typing anything to exit");
            acceptInput = sc.nextLine().trim(); // Read the full line

            if (acceptInput.isEmpty())
                break; // Exit if just Enter is pressed

            char choice = acceptInput.charAt(0); // Get the first character only
            if (choice == '1') {
                addSplit();
            } else if (choice == '2') {
                showSplits();
            }
            // else if (choice == '3') {
            // addExpense(null);
            // }
            else {
                break; // Any other character -> exit
            }

        }
    }

    static String addSplit() {
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
            addExpense(newSplit);
        return newSplit.getId();
    }

    static void showSplits() {
        for (int i = 0; i < splits.size(); i++) {
            splits.get(i).printSplitDetails();

            // Ask if they want to see expenses
            System.out.println("Enter 1 to see all the expenses of this split");
            System.out.println("Enter 2 to delete this split");
            System.out.println("Enter 3 to edit this split");
            System.out.println("Press Enter to exit");

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
                    editExpense(split.getExpenseById(v), users);

                } else if (input.equals("2")) {
                    System.out.println("Enter Edit ID of expense to delete");
                    input = sc.nextLine();
                    int v = Integer.parseInt(input);
                    split.deleteExpenseById(v);
                } else if (input.equals("3")) {
                    addExpense(split);
                    ;
                } else {
                    // pressed Enter or other key
                    return;
                }
            } else if (input.equals("2")) {
                splits.remove(i);
            } else if (input.equals("3")) {
                editSplit(splits.get(i));
            } else {
                // System.out.println("Returning...");
                return;
            }

        }
    }

    public static void editSplit(Split split) {
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
                    selectedUser = createNewUser();
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
    }

    public static void addExpense(Split split) {
        if (split == null) {
            System.out.println("⚠️ No split provided. Creating a new split first...");
            String id = addSplit();
            for (Split s : splits) {
                if (s.getId().equals(id)) {
                    split = s;
                    break;
                }
            }
        }

        System.out.println("Enter expense name:");
        String name = sc.nextLine().trim();

        List<User> members = split.getMembers();

        // Select Split Type
        SplitType splitType = null;
        while (splitType == null) {
            System.out.println("Select split type: 1. EQUAL  2. EXACT  3. PERCENT");
            try {
                int typeChoice = Integer.parseInt(sc.nextLine().trim());
                switch (typeChoice) {
                    case 1:
                        splitType = SplitType.EQUAL;
                        break;
                    case 2:
                        splitType = SplitType.EXACT;
                        break;
                    case 3:
                        splitType = SplitType.PERCENT;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        double amount = 0;
        if (splitType != SplitType.EXACT) {
            while (true) {
                System.out.print("Enter total amount: ");
                try {
                    amount = Double.parseDouble(sc.nextLine().trim());
                    if (amount <= 0) {
                        System.out.println("Amount must be greater than 0.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        }

        Map<User, Double> usersMap = new HashMap<>();
        double percentSum = 0;
        List<User> includedUsers = new ArrayList<>();

        for (User user : members) {
            System.out.println("Include " + user.getName() + " in split? (y/n):");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                double value = 0;

                if (splitType == SplitType.EXACT) {
                    while (true) {
                        System.out.print("Enter exact amount: ");
                        try {
                            value = Double.parseDouble(sc.nextLine().trim());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Try again.");
                        }
                    }
                } else if (splitType == SplitType.PERCENT) {
                    while (true) {
                        System.out.print("Enter percent: ");
                        try {
                            double percent = Double.parseDouble(sc.nextLine().trim());
                            percentSum += percent;
                            value = (percent / 100.0) * amount;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid percentage. Try again.");
                        }
                    }
                }

                usersMap.put(user, value);
                includedUsers.add(user);
            }
        }

        System.out.print("Add a new user to the split? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            User newUser = createNewUser();
            members.add(newUser);

            double val = 0;
            if (splitType == SplitType.EXACT) {
                while (true) {
                    System.out.print("Enter exact amount: ");
                    try {
                        val = Double.parseDouble(sc.nextLine().trim());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            } else if (splitType == SplitType.PERCENT) {
                while (true) {
                    System.out.print("Enter percent: ");
                    try {
                        double percent = Double.parseDouble(sc.nextLine().trim());
                        percentSum += percent;
                        val = (percent / 100.0) * amount;
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            }

            usersMap.put(newUser, val);
            includedUsers.add(newUser);
        }

        if (splitType == SplitType.PERCENT && Math.abs(percentSum - 100.0) > 0.001) {
            System.out.println("❌ Total percentage is " + percentSum + "%. It must be exactly 100%.");
            return;
        }

        System.out.println("Select PaidBy user from included ones:");
        for (int i = 0; i < includedUsers.size(); i++) {
            System.out.println((i + 1) + ". " + includedUsers.get(i).getName());
        }

        User paidBy = null;
        while (paidBy == null) {
            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                if (choice < 1 || choice > includedUsers.size()) {
                    System.out.println("Invalid selection. Try again.");
                } else {
                    paidBy = includedUsers.get(choice - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }

        System.out.print("Enter description (press Enter to skip): ");
        String desc = sc.nextLine().trim();

        if (desc.isEmpty()) {
            split.addExpense(name, amount, paidBy, usersMap, splitType);
        } else {
            split.addExpense(name, amount, paidBy, usersMap, splitType, desc);
        }

        System.out.println("✅ Expense added successfully.\n");
    }

    public static void editExpense(Expense expense, List<User> globalUsers) {
        System.out.println("Editing expense: " + expense.getName());

        System.out.print("New name (Enter to skip): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty())
            name = expense.getName();

        System.out.print("New description (Enter to skip): ");
        String desc = sc.nextLine().trim();
        if (desc.isEmpty())
            desc = expense.getDescription();

        SplitType type = null;
        while (type == null) {
            System.out.println("Choose Split Type: 1. EQUAL  2. EXACT  3. PERCENT");
            try {
                int opt = Integer.parseInt(sc.nextLine().trim());
                switch (opt) {
                    case 1:
                        type = SplitType.EQUAL;
                        break;
                    case 2:
                        type = SplitType.EXACT;
                        break;
                    case 3:
                        type = SplitType.PERCENT;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }

        double amount = expense.getAmount();
        if (type != SplitType.EXACT) {
            System.out.print("Enter new amount (Enter to skip): ");
            String amtStr = sc.nextLine().trim();
            if (!amtStr.isEmpty()) {
                try {
                    amount = Double.parseDouble(amtStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Keeping old amount.");
                }
            }
        }

        Map<User, Double> usersMap = new HashMap<>();
        List<User> includedUsers = new ArrayList<>();
        double percentSum = 0;

        for (User user : globalUsers) {
            System.out.println("Include " + user.getName() + " in split? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                double val = 0;
                if (type == SplitType.EXACT) {
                    while (true) {
                        System.out.print("Enter exact amount: ");
                        try {
                            val = Double.parseDouble(sc.nextLine().trim());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number.");
                        }
                    }
                } else if (type == SplitType.PERCENT) {
                    while (true) {
                        System.out.print("Enter percentage: ");
                        try {
                            double percent = Double.parseDouble(sc.nextLine().trim());
                            percentSum += percent;
                            val = (percent / 100.0) * amount;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid percentage.");
                        }
                    }
                }

                usersMap.put(user, val);
                includedUsers.add(user);
            }
        }

        System.out.print("Add new user to split? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            User newUser = createNewUser();
            globalUsers.add(newUser);

            double val = 0;
            if (type == SplitType.EXACT) {
                while (true) {
                    System.out.print("Enter exact amount: ");
                    try {
                        val = Double.parseDouble(sc.nextLine().trim());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            } else if (type == SplitType.PERCENT) {
                while (true) {
                    System.out.print("Enter percent: ");
                    try {
                        double percent = Double.parseDouble(sc.nextLine().trim());
                        percentSum += percent;
                        val = (percent / 100.0) * amount;
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            }

            usersMap.put(newUser, val);
            includedUsers.add(newUser);
        }

        if (type == SplitType.PERCENT && Math.abs(percentSum - 100.0) > 0.001) {
            System.out.println("❌ Total percent is " + percentSum + "%. It must be 100%.");
            return;
        }

        System.out.println("Select PaidBy from included users:");
        for (int i = 0; i < includedUsers.size(); i++) {
            System.out.println((i + 1) + ". " + includedUsers.get(i).getName());
        }

        User paidBy = null;
        while (paidBy == null) {
            try {
                int idx = Integer.parseInt(sc.nextLine().trim());
                if (idx < 1 || idx > includedUsers.size()) {
                    System.out.println("Invalid index.");
                } else {
                    paidBy = includedUsers.get(idx - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }

        expense.updateExpense(name, amount, paidBy, usersMap, type, desc);
        System.out.println("✅ Expense updated.\n");
    }

    public static User createNewUser() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        return new User(name);
    }

}