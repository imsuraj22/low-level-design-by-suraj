
import java.time.LocalDateTime;

public class ParkingTicket {
    private static long globalTicketIdCounter = 0;
    private long ticketId;
    private String vehicleNumber;
    private SlotType slotType;
    private Slot assignedSlot;
    private LocalDateTime arrivedAt;

    public ParkingTicket(String vehicleNumber, SlotType slotType, Slot slot) {
        ticketId = ++globalTicketIdCounter;
        this.vehicleNumber = vehicleNumber;
        this.slotType = slotType;
        this.assignedSlot = slot;
        this.arrivedAt = LocalDateTime.now();
    }

    public long getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public SlotType getVehicleType() {
        return slotType;
    }

    public void setVehicleType(SlotType vehicleType) {
        this.slotType = vehicleType;
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
