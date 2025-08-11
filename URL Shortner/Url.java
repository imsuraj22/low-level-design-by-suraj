import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Url {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private long urlId;
    private long userId; 
    private String longURL;
    private String shortURL; 
    private String customAlias; 
    private LocalDate createAt;
    private LocalDate expireDate;
    private long clickCount;

    // Constructor for guest users (no userId)
    public Url(String longURL, String shortURL, String customAlias, LocalDate expireDate) {
        this(-1, longURL, shortURL, customAlias, expireDate);
    }

    // Constructor for logged-in users
    public Url(long userId, String longURL, String shortURL, String customAlias, LocalDate expireDate) {
        this.urlId = counter.incrementAndGet();
        this.userId = userId;
        this.longURL = longURL;
        this.shortURL = shortURL; // Either generated Base62 or custom alias
        this.customAlias = customAlias; // Save if user provided, else null
        this.createAt = LocalDate.now();
        this.expireDate = expireDate;
        this.clickCount = 0;
    }

    // Getters and Setters
    public long getUrlId() {
        return urlId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLongURL() {
        return longURL;
    }

    public void setLongURL(String longURL) {
        this.longURL = longURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }
}
