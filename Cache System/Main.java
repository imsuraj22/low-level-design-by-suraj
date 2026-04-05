public class Main {
    public static void main(String[] args) {
        Cache<Integer, String> cache = new Cache<>(2);
        cache.set(1, "A", 5000);
        cache.set(2, "B", 5000);
        System.out.println(cache.get(1));
        cache.set(1, "A_updated", 5000);
        System.out.println(cache.get(1)); 
        cache.set(3, "C", 5000);
        System.out.println(cache.get(2)); 
    }
}
