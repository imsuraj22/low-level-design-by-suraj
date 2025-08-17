
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Floor {
    private int floorNumber;
    private List<Slot> slots;
    private static long globalSlotCounter = 0;
    private int localSlotCounter = 0;
    private int totalSlots;
    private PriorityQueue<Slot> bikeSlotsQueue;
    private PriorityQueue<Slot> carSlotsQueue;

    public Floor(int floorNumber, int bikeCount, int carCount) {
        slots = new ArrayList<>();
        this.floorNumber = floorNumber;
        this.totalSlots = bikeCount + carCount;
        addSlot(bikeCount, SlotType.BIKE_SLOT);
        addSlot(carCount, SlotType.CAR_SLOT);
        bikeSlotsQueue = new PriorityQueue<>(Comparator.comparing(Slot::getSlotId));
        carSlotsQueue = new PriorityQueue<>(Comparator.comparing(Slot::getSlotId));

    }

    private void addSlot(int count, SlotType slotType) {
        for (int i = 0; i < count; i++) {
            Slot newSlot = new Slot(slotType);
            newSlot.setSlotId(++globalSlotCounter);
            newSlot.setLocalSlotNumber("F" + floorNumber + "S" + (++localSlotCounter));
            slots.add(newSlot);
            if (slotType == SlotType.BIKE_SLOT) {
                bikeSlotsQueue.add(newSlot);
            } else {
                carSlotsQueue.add(newSlot);
            }
        }
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public Slot getNextAvailableSlot(SlotType type) {
        if (type == SlotType.BIKE_SLOT) {
            if (bikeSlotsQueue.isEmpty())
                return null;
            return bikeSlotsQueue.peek();
        } else {
            if (carSlotsQueue.isEmpty())
                return null;
            return carSlotsQueue.peek();
        }
    }

    public void occupySlot(Slot slot) {
        slot.setOccupied(true);
        if (slot.getSlotType() == SlotType.BIKE_SLOT)
            bikeSlotsQueue.remove(slot);
        else
            carSlotsQueue.remove(slot);
    }

    public void freeSlot(Slot slot) {
        slot.setOccupied(false);
        if (slot.getSlotType() == SlotType.BIKE_SLOT)
            bikeSlotsQueue.add(slot);
        else
            carSlotsQueue.add(slot);
    }

}