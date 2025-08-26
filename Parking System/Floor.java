
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Floor {
    private int floorNumber;
    private List<Slot> slots;
    private static long globalSlotCounter = 0;
    private int localSlotCounter = 0;
    private int totalSlots;
    private PriorityQueue<Slot> bikeSlotsQueue;
    private PriorityQueue<Slot> carSlotsQueue;
    private int bikecount;
    private int carCount;

    public Floor(int floorNumber, int bikeCount, int carCount) {
        slots = new ArrayList<>();
        this.floorNumber = floorNumber;
        this.totalSlots = bikeCount + carCount;
        bikeSlotsQueue = new PriorityQueue<>(Comparator.comparing(Slot::getSlotId));
        carSlotsQueue = new PriorityQueue<>(Comparator.comparing(Slot::getSlotId));
        addSlot(bikeCount, SlotType.BIKE_SLOT);
        addSlot(carCount, SlotType.CAR_SLOT);

        this.bikecount = bikeCount;
        this.carCount = carCount;

    }

    public int getFloorId() {
        return floorNumber;
    }

    private void addSlot(int count, SlotType slotType) {
        for (int i = 0; i < count; i++) {
            Slot newSlot = new Slot(slotType, floorNumber);
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
        Queue<Slot> queue = (type == SlotType.BIKE_SLOT) ? bikeSlotsQueue : carSlotsQueue;

        while (!queue.isEmpty()) {
            Slot slot = queue.peek();
            if (slot.tryOccupy()) {
                queue.poll();
                return slot;
            } else {
                queue.poll();
            }
        }
        return null;
    }

    // public void occupySlot(Slot slot) {
    // slot.setOccupied(true);
    // if (slot.getSlotType() == SlotType.BIKE_SLOT)
    // bikeSlotsQueue.remove(slot);
    // else
    // carSlotsQueue.remove(slot);
    // }

    public void freeSlot(Slot slot) {
        slot.release();
        if (slot.getSlotType() == SlotType.BIKE_SLOT)
            bikeSlotsQueue.add(slot);
        else
            carSlotsQueue.add(slot);
    }

    public boolean isBikeSpotAvailable() {
        return bikeSlotsQueue.isEmpty() ? true : false;
    }

    public boolean isCarSpotAvailable() {
        return carSlotsQueue.isEmpty() ? true : false;
    }

}