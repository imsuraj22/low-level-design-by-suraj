import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class ExpenseService {
    Scanner sc;
    List<User> users;
    List<Split> splits;
    SplitService splitService;
    UserService userService;

    public ExpenseService() {

    }

    public ExpenseService(Scanner sc, List<User> users, List<Split> splits, SplitService splitService,
            UserService userService) {
        this.sc = sc;
        this.users = users;
        this.splits = splits;
        this.splitService = splitService;
        this.userService = userService;
    }

    public void editExpense(Expense expense, List<User> globalUsers) {
        ReentrantLock lock = expense.getLock();
        lock.lock();
        try {
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

            while (true) {
                System.out.print("Add new user to split? (y/n): ");
                if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                    User newUser = userService.createNewUser();
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
                } else {
                    break;
                }
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
        } finally {
            lock.unlock();
        }
    }

    public void addExpense(Split split) {
        if (split == null) {
            System.out.println("⚠️ No split provided. Creating a new split first...");
            String id = splitService.addSplit();
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

        while (true) {
            System.out.print("Add a new user to the split? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                User newUser = userService.createNewUser();
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
            } else {
                break;
            }
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
}
