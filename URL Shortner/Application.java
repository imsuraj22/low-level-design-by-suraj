import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Application {
    private Map<String, Url> shortKeyMap = new HashMap<>();
    private Map<Long, List<Url>> userUrlsMap = new HashMap<>();
    private List<User> registeredUsers = new ArrayList<>();
    private Set<String> customAliasSet = new HashSet<>();
    private URLShortnerService urlShortnerService;
    private UserService userService;
    private Scanner sc;
    // User loggedInUser = null;

    public Application() {
        sc = new Scanner(System.in);
        urlShortnerService = new URLShortnerService(shortKeyMap, userUrlsMap, customAliasSet, sc);
        userService = new UserService(shortKeyMap, userUrlsMap, registeredUsers, sc, urlShortnerService);
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.startMenu();
    }

    public void startMenu() {
        while (true) {
            System.out.println("\n=== URL Shortener Menu ===");
            System.out.println("Enter 1 to Register");
            System.out.println("Enter 2 to Login");
            System.out.println("Enter 3 to Create Short URL");
            System.out.println("Enter 4 to get URL");
            System.out.println("Enter 5 to Logout");
            System.out.println("Press Enter to Exit");

            String choice = sc.nextLine();
            choice = choice.trim();

            if (choice.isEmpty()) {
                System.out.println("Exiting... Goodbye!");
                break;
            }

            if (choice.equals("1")) {
                userService.createUser();

            } else if (choice.equals("2")) {
                System.out.println("Enter email");
                String email = sc.nextLine();
                System.out.println("Enter a password");
                String password = sc.nextLine();
                if (userService.logIn(email, password)) {
                    loggedInUserServices();

                } else {
                    System.out.println("Please Enter valid email or password");
                }

            } else if (choice.equals("3")) {
                User currentUser = userService.getLoggedInUser(); // ✅ get from UserService
                if (currentUser != null) {
                    urlShortnerService.addURL(currentUser.getId());
                } else {
                    urlShortnerService.addURL(-1); // guest
                }

            } else if (choice.equals("4")) {
                System.out.println("Enter short URL");
                String input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("No URL entered!");
                    continue;
                }

                String shortKey = input.substring(input.lastIndexOf("/") + 1);

                String longUrl = urlShortnerService.getLongURL(shortKey);
                System.out.println(longUrl);
            } else if (choice.equals("5")) {
                if (userService.getLoggedInUser() == null) {
                    System.out.println("No user is currently logged in!");
                } else {
                    User user = userService.getLoggedInUser();
                    System.out.println("User " + user.getName() + " logged out successfully.");
                    userService.logout();
                }
            } else {
                System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public void loggedInUserServices() {
        System.out.println("Enter 1 to see all URLs\nPress Enter to exit");
        String input = sc.nextLine();
        if (input.equals("1")) {
            userService.showAllUrls();
        } else {
            return;
        }
    }
}
