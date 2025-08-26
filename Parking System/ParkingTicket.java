
import java.time.LocalDateTime;

public class ParkingTicket {
    private static long globalTicketIdCounter = 0;
    private long ticketId;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private Slot assignedSlot;
    private LocalDateTime arrivedAt;

    public ParkingTicket(String vehicleNumber, VehicleType vehicleType, Slot slot) {
        ticketId = ++globalTicketIdCounter;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.assignedSlot = slot;
        this.arrivedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", vehicleType=" + vehicleType +
                ", assignedSlot=" + (assignedSlot != null ? assignedSlot.getSlotId() : "N/A") +
                ", arrivedAt=" + arrivedAt +
                '}';
    }

    public long getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Slot getAssignedSlot() {
        return assignedSlot;
    }

    public void setAssignedSlot(Slot assignedSlot) {
        this.assignedSlot = assignedSlot;
    }

    public LocalDateTime getArrivedAt() {
        return arrivedAt;
    }

}
