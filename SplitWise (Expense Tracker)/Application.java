import java.util.ArrayList;

import java.util.List;

import java.util.Scanner;

public class Application {
    static Scanner sc;
    static List<User> users;
    static List<Split> splits;
    static ExpenseService expenseService;
    static SplitService splitService;
    static UserService userService;

    public static void main(String args[]) {
        sc = new Scanner(System.in);
        users = new ArrayList<>();
        splits = new ArrayList<>();
        userService = new UserService(sc);

        // Step 2: Create empty services temporarily
        expenseService = new ExpenseService(); // temporary
        splitService = new SplitService(sc, users, splits, expenseService, userService);

        // Step 3: Now inject the proper splitService into expenseService
        expenseService = new ExpenseService(sc, users, splits, splitService, userService);

        // Step 4: Replace splitService to update it with the new expenseService
        splitService = new SplitService(sc, users, splits, expenseService, userService);
        String acceptInput = "";
        while (true) {
            System.out.println(
                    "Enter 1 to create a new split\nEnter 2 to see all the splits\nPress Enter without typing anything to exit");
            acceptInput = sc.nextLine().trim(); // Read the full line

            if (acceptInput.isEmpty())
                break; // Exit if just Enter is pressed

            char choice = acceptInput.charAt(0); // Get the first character only
            if (choice == '1') {
                splitService.addSplit();
            } else if (choice == '2') {
                splitService.showSplits();
            }
            // else if (choice == '3') {
            // addExpense(null);
            // }
            else {
                break; // Any other character -> exit
            }

        }
    }

}