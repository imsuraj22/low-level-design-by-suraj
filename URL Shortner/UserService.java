import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserService {
    private Map<String, Url> shortKeyMap;
    private Map<Long, List<Url>> userUrlsMap;
    private List<User> registeredUsers;
    private Scanner sc;
    private User loggedInUser = null;
    private URLShortnerService urlShortnerService;

    public UserService(Map<String, Url> shortKeyMap, Map<Long, List<Url>> userUrlsMap, List<User> registeredUsers,
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
            System.out.println("Enter the name or press enter to stop");
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
        }

    }

    public void logIn(String email, String password) {
        for (int i = 0; i < registeredUsers.size(); i++) {
            User user = registeredUsers.get(i);
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                loggedInUser = user;
            }
        }
        System.out.println("Please enter valid email id or password");

    }

    public void showAllUrls() {
        if (loggedInUser == null) {
            System.out.println("Please log in to see your URLs");
            return;
        }
        long userId = loggedInUser.getId();
        if (!userUrlsMap.containsKey(userId)) {
            System.out.println("No URLs found");
            return;
        }
        System.out.println("Your URLs:");
        for (Url url : userUrlsMap.get(userId)) {
            System.out.println("Short: " + url.getShortURL() +
                    " | Long: " + url.getLongURL() +
                    " | Clicks: " + url.getClickCount());
        }
        String input = "";
        while (true) {
            System.out
                    .println("Enter 1 to edit URL/nEnter 2 to delete URL/nEnter 3 to add the URL/nPress Enter to exit");
            input = sc.nextLine();
            if (input.equals("1")) {
                System.out.println("Enter URL id");
                input = sc.nextLine();
                urlShortnerService.editURL(userId, Long.parseLong(input));
            } else if (input.equals("2")) {
                System.out.println("Enter URL id");
                input = sc.nextLine();
                urlShortnerService.deleteURL(userId, Long.parseLong(input));
            } else if (input.equals("3")) {
                urlShortnerService.addURL(userId);
            } else {
                return;
            }
        }

    }

}
