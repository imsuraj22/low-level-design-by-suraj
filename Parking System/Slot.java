public class Slot {
    private long slotId;
    private String localSlotNumber;
    private SlotType slotType;
    private boolean occupied;

    public Slot(SlotType slotType) {
        this.slotType = slotType;
        this.occupied = false;
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
