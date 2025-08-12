import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private ConcurrentHashMap<String, Url> shortKeyMap;
    private Map<Long, List<Url>> userUrlsMap;
    private List<User> registeredUsers;
    private Scanner sc;
    private User loggedInUser = null;
    private URLShortnerService urlShortnerService;

    public UserService(ConcurrentHashMap<String, Url> shortKeyMap, Map<Long, List<Url>> userUrlsMap,
            List<User> registeredUsers,
            Scanner sc, URLShortnerService urlShortnerService) {
        this.shortKeyMap = shortKeyMap;
        this.userUrlsMap = userUrlsMap;
        this.registeredUsers = registeredUsers;
        this.sc = sc;
        this.urlShortnerService = urlShortnerService;

    }

    private boolean isValidEmail(String email) {

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {

        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    public void createUser() {
        while (true) {
            System.out.println("Enter the name to create user or press enter to stop");
            String name = sc.nextLine();
            if (name.isEmpty())
                return;

            System.out.println("Enter the email");
            String email = sc.nextLine();
            if (!isValidEmail(email)) {
                System.out.println("Invalid email! Please try again.");
                continue;
            }

            System.out.println("Enter the password");
            String password = sc.nextLine();
            if (!isValidPassword(password)) {
                System.out.println("Invalid password! Minimum 8 chars, 1 letter, 1 digit.");
                continue;
            }

            // Check if email already exists
            if (registeredUsers.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
                System.out.println("Email already registered! Try a different one.");
                continue;
            }

            User newUser = new User(name, email, password);
            registeredUsers.add(newUser);
            System.out.println("User Created Successfully");
        }

    }

    public boolean logIn(String email, String password) {
        for (int i = 0; i < registeredUsers.size(); i++) {
            User user = registeredUsers.get(i);
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;

    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println("User " + loggedInUser.getName() + " logged out.");
            loggedInUser = null;
        } else {
            System.out.println("No user logged in!");
        }
    }

    public void printURLS(long userId) {
        if (userUrlsMap.containsKey(userId)) {

            System.out.println("Your URLs:");
            for (Url url : userUrlsMap.get(userId)) {
                System.out.println("URL ID " + url.getUrlId() + "| Short: " + url.getShortURL() +
                        " | Long: " + url.getLongURL() +
                        " | Clicks: " + url.getClickCount());
            }
        } else {
            System.out.println("No URLs found");
        }
    }

    public void showAllUrls() {
        if (loggedInUser == null) {
            System.out.println("Please log in to see your URLs");
            return;
        }
        long userId = loggedInUser.getId();
        if (!userUrlsMap.containsKey(userId)) {
            System.out.println("There are no URLs please add a new URL");
            urlShortnerService.addURL(userId);
        }
        printURLS(userId);

        String input = "";
        while (true) {
            System.out
                    .println(
                            "Enter 1 to edit URL\nEnter 2 to delete URL\nEnter 3 to add the URL\nEnter 4 to show all URLs\nEnter 5 to get URL\nPress Enter to exit");
            input = sc.nextLine();
            if (input.equals("1")) {

                System.out.println("Enter URL id from below URLs");
                printURLS(userId);
                input = sc.nextLine();
                try {
                    long id = Long.parseLong(input);
                    urlShortnerService.editURL(userId, id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number.");
                    // break; // exit the loop
                }
            } else if (input.equals("2")) {

                System.out.println("Enter URL id from below URLs");
                printURLS(userId);
                input = sc.nextLine();
                try {
                    long id = Long.parseLong(input);
                    urlShortnerService.deleteURL(userId, id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number.");
                    // break; // exit the loop
                }
            } else if (input.equals("3")) {
                urlShortnerService.addURL(userId);
            } else if (input.equals("4")) {
                printURLS(userId);
            } else if (input.equals("5")) {
                System.out.println("Enter short URL");
                input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("No URL entered!");
                    return;
                }

                String shortKey = input.substring(input.lastIndexOf("/") + 1);

                String longUrl = urlShortnerService.getLongURL(shortKey);
                System.out.println(longUrl);
            } else {
                return;
            }
        }

    }

}
