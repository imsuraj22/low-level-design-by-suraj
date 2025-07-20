import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private int id;
    private String name;
    private String email;

    public User(String name, String email) {
        this.id = counter.incrementAndGet(); // Auto-incremented ID
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
