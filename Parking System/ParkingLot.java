
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ParkingLot {

    private static Map<Integer, Floor> floors;
    private static Map<Long, ParkingTicket> tickets;

    public ParkingLot() {
        floors = new HashMap<>();
        tickets = new HashMap<>();
    }

    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of floors in parking system:");
        int floorCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < floorCount; i++) {
            System.out.println("Floor " + (i + 1));
            System.out.println("Enter number of bike slots:");
            int bikeCount = Integer.parseInt(sc.nextLine());
            System.out.println("Enter number of car slots:");
            int carCount = Integer.parseInt(sc.nextLine());

            Floor floor = new Floor(i + 1, bikeCount, carCount);
            floors.put(i + 1, floor); // key = floorId, value = Floor object
        }

        while (true) {
            System.out.println("Enter 1 to park a vehicle.\nEnter 2 to exit from parking" +
                    "\nEnter 3 to change pricing (admin only)\nPress Enter to quit");

            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Exiting...");
                break;
            }

            switch (input) {
                case "1":
                    parkingLot.parkVehicle(sc);
                    break;
                case "2":
                    parkingLot.exitParking(sc);
                    break;
                case "3":
                    parkingLot.updatePricing(sc);
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    void updatePricing(Scanner sc) {
        System.out.println("Enter new Price/Hour for bike or press Enter to skip");
        String price = sc.nextLine();
        price = price.trim();
        if (!price.isEmpty()) {
            double newPrice = Double.parseDouble(price);
            Pricing.setRate(VehicleType.BIKE, newPrice);
        }
        System.out.println("Enter new Price/Hour for car or press Enter to skip");
        price = sc.nextLine();
        price = price.trim();
        if (!price.isEmpty()) {
            double newPrice = Double.parseDouble(price);
            Pricing.setRate(VehicleType.CAR, newPrice);
        }
        System.out.println("New Pricing");
        Pricing.print();

    }

    void parkVehicle(Scanner sc) {
        System.out.println("Enter 1 to park Bike\nEnter 2 to park Car");
        String input = sc.nextLine().trim();

        VehicleType vehicleType;
        SlotType slotType;

        if (input.equals("1")) {
            vehicleType = VehicleType.BIKE;
            slotType = SlotType.BIKE_SLOT;
        } else if (input.equals("2")) {
            vehicleType = VehicleType.CAR;
            slotType = SlotType.CAR_SLOT;
        } else {
            System.out.println("Wrong input");
            return;
        }

        // Iterate over all floors to find next available slot
        for (Floor floor : floors.values()) {
            Slot nextSlot = floor.getNextAvailableSlot(slotType);
            if (nextSlot != null) {
                System.out.println("Enter vehicle number:");
                String vehicleNumber = sc.nextLine().trim();

                // Occupy slot
                // nextSlot.setOccupied(true);
                // floor.occupySlot(nextSlot); // optional: update PQ if using priority queue

                // Create ticket
                ParkingTicket newTicket = new ParkingTicket(vehicleNumber, vehicleType, nextSlot);
                tickets.put(newTicket.getTicketId(), newTicket);

                System.out.println("Parked at Floor " + floor.getFloorId() +
                        ", Slot " + nextSlot.getLocalSlotNumber());
                System.out.println("This is your ticket number: " + newTicket.getTicketId());
                return;
            }
        }

        System.out.println("Sorry, no " + vehicleType + " parking available.");
    }

    public void exitParking(Scanner sc) {
        System.out.println("Enter ticket ID:");
        String input = sc.nextLine().trim(); // use nextLine to avoid Scanner issues
        long ticketId;

        try {
            ticketId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ticket number.");
            return;
        }

        if (!tickets.containsKey(ticketId)) {
            System.out.println("Ticket not found. Enter a valid ticket ID.");
            return;
        }

        ParkingTicket ticket = tickets.get(ticketId);

        // Calculate duration and fees
        LocalDateTime arrivedAt = ticket.getArrivedAt();
        LocalDateTime now = LocalDateTime.now();
        long minutesParked = Duration.between(arrivedAt, now).toMinutes();
        double fees = Pricing.calculateFee(ticket.getVehicleType(), minutesParked);

        // Print ticket info & fees
        ticket.toString();
        System.out.println("Your Fees: Rs" + fees);

        // Free the slot
        Slot slotToFree = ticket.getAssignedSlot();
        int floorId = slotToFree.getFloorId();
        Floor floor = floors.get(floorId);
        floor.freeSlot(slotToFree);

        // Remove ticket from active tickets map
        tickets.remove(ticketId);

        System.out.println("Slot freed. Thank you!");
    }

}
