import java.util.concurrent.atomic.AtomicBoolean;

public class Slot {
    private long slotId;
    private String localSlotNumber;
    private SlotType slotType;
    // private boolean occupied;
    private int floorId;
    private final AtomicBoolean occupied;

    public Slot(SlotType slotType, int floorId) {
        this.slotType = slotType;
        this.occupied = new AtomicBoolean(false);
        this.floorId = floorId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    // getters and setters
    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public String getLocalSlotNumber() {
        return localSlotNumber;
    }

    public void setLocalSlotNumber(String localSlotNumber) {
        this.localSlotNumber = localSlotNumber;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public boolean isOccupied() {
        return occupied.get();
    }

    // Try to occupy slot (thread-safe, only 1 succeeds)
    public boolean tryOccupy() {
        return occupied.compareAndSet(false, true);
    }

    // Free the slot again
    public void release() {
        occupied.set(false);
    }
}
