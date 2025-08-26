import java.util.EnumMap;
import java.util.Map;

public class Pricing {
    private static Map<VehicleType, Double> pricePerHour = new EnumMap<>(VehicleType.class);

    static {

        pricePerHour.put(VehicleType.BIKE, 50.0);
        pricePerHour.put(VehicleType.CAR, 100.0);
    }

    // Get rate
    public static double getRate(VehicleType vehicleType) {
        return pricePerHour.getOrDefault(vehicleType, 0.0);
    }

    public static void print() {
        for (VehicleType vc : pricePerHour.keySet()) {
            System.out.println(vc + " : " + pricePerHour.get(vc));
        }
    }

    // Set rate dynamically if needed
    public static void setRate(VehicleType vehicleType, double rate) {
        pricePerHour.put(vehicleType, rate);
    }

    // Calculate fee in minutes
    public static double calculateFee(VehicleType vehicleType, long minutesParked) {
        double rate = getRate(vehicleType);
        return (rate / 60.0) * minutesParked;
    }
}
