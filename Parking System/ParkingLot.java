
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParkingLot {

    private static List<Floor> floors;

    public ParkingLot() {
        floors = new ArrayList<>();
    }

    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot();
        Scanner sc = new Scanner(System.in);
        String input = "";
        System.out.println("Enter the floors in pakring system");
        input = sc.nextLine();
        int floorCount = Integer.parseInt(input);
        for (int i = 0; i < floorCount; i++) {
            System.out.println("Enter No of bike slots and car slots for floor " + i + 1 + " \n No of bike Slots ");
            int bikeCount = sc.nextInt();
            System.out.println("\n No of car Slots");
            int carCount = sc.nextInt();
            floors.add(new Floor(i + 1, bikeCount, carCount));

        }

    }
}
