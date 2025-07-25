import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private int id;
    private String name;
    private String email;

    public User(String name) {
        this(name, null);
    }

    public User(String name, String email) {
        this.id = counter.incrementAndGet(); // Auto-incremented ID
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }

}
