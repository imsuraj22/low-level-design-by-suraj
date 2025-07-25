import java.util.Scanner;

public class UserService {
    Scanner sc;

    public UserService(Scanner sc) {
        this.sc = sc;
    }

    public User createNewUser() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        return new User(name);
    }
}
