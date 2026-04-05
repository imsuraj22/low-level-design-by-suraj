
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        PlanService planService = new PlanService();
        RateLimiter rateLimiter = new RateLimiter(planService);

        while (true) {

            System.out.println("\nChoose Option:");
            System.out.println("1. Register Client");
            System.out.println("2. Send Request");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Enter Client ID: ");
                    String clientId = scanner.nextLine();

                    System.out.println("Select Plan: FREE / PRO / PREMIUM");
                    String planInput = scanner.nextLine().toUpperCase();

                    Plan plan = Plan.valueOf(planInput);

                    planService.registerClient(clientId, plan);

                    System.out.println("Client registered successfully.");
                    break;

                case 2:
                    System.out.print("Enter Client ID: ");
                    String requestClient = scanner.nextLine();

                    boolean allowed = rateLimiter.allowRequest(requestClient);

                    if (allowed) {
                        System.out.println("Request Allowed");
                    } else {
                        System.out.println("Rate Limit Exceeded");
                    }

                    break;

                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid Option");
            }
        }
    }
}

