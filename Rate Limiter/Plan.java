public enum Plan {

    FREE(20),
    PRO(60),
    PREMIUM(100);

    private int capacity;

    Plan(int capacity){
        this.capacity = capacity;
    }

    public int getCapacity(){
        return capacity;
    }
}