import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class URLShortnerService {
    private Map<String, Url> shortKeyMap;
    private Map<Long, List<Url>> userUrlsMap;
    private Set<String> customAliasSet;
    private Scanner sc;
    private String input = "";
    private final String URL_REGEX = "^(https?://)" + "([\\w.-]+)" + "(\\.[a-zA-Z]{2,6})" + "([\\w\\-./?%&=]*)?$";

    public URLShortnerService(Map<String, Url> shortKeyMap, Map<Long, List<Url>> userUrlsMap,
            Set<String> customAliasSet, Scanner sc) {
        this.shortKeyMap = shortKeyMap;
        this.userUrlsMap = userUrlsMap;
        this.customAliasSet = customAliasSet;
        this.sc = sc;
    }

    public void addURL(long userId) {
        Scanner sc = new Scanner(System.in);

        // 1. Accept and validate long URL
        String longURL;
        while (true) {
            System.out.println("Enter the long URL:");
            longURL = sc.nextLine().trim();

            if (longURL.isEmpty()) {
                System.out.println("Long URL cannot be empty!");
                continue;
            }
            if (!isValidURL(longURL)) {
                System.out.println("Invalid URL format! Please enter a valid URL (http or https).");
                continue;
            }
            break;
        }

        // 2. Accept optional custom alias
        String customAlias = "";
        while (true) {
            System.out.println("Enter custom alias (or press Enter to skip):");
            customAlias = sc.nextLine().trim();

            if (customAlias.isEmpty()) {
                // User wants random short key
                customAlias = null;
                break;
            }

            if (customAliasSet.contains(customAlias)) {
                System.out
                        .println("Custom alias already exists! ");
                continue; // ask again
            }

            // Alias is unique
            break;
        }

        // 3. Accept optional expiry date
        LocalDate expiryDate = null;
        while (true) {
            System.out.println("Enter new Expire date (yyyy-MM-dd) or press Enter to skip:");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                expiryDate = LocalDate.now().plusYears(10); // Default 10 years
                break;
            }

            try {
                expiryDate = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);

                // Reject dates beyond 30 years
                LocalDate maxDate = LocalDate.now().plusYears(30);
                if (expiryDate.isAfter(maxDate)) {
                    System.out.println("Date cannot be more than 30 years from now. Re-enter or press Enter to skip.");
                    continue;
                }

                // Reject past dates
                if (expiryDate.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in the past. Re-enter or press Enter to skip.");
                    continue;
                }

                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Use yyyy-MM-dd or press Enter to skip.");
            }
        }

        // 4. Shorten URL
        String shortURL = shortenURL(userId, longURL, customAlias, expiryDate);
        System.out.println("Short URL created: " + shortURL);
    }

    private Url alreadyExists(long userId, String longUrl) {
        if (userUrlsMap.containsKey(userId)) {
            List<Url> urls = userUrlsMap.get(userId);
            for (int i = 0; i < urls.size(); i++) {
                if (urls.get(i).getLongURL().equals(longUrl))
                    return urls.get(i);
            }
        }
        return null;
    }

    public String shortenURL(long userId, String longURL, String customAlias, LocalDate expiryDate) {
        String shortKey;
        if (userId != -1) {
            Url exist = alreadyExists(userId, longURL);
            if (exist != null) {
                System.out.println("URL already shortened. Returning existing short URL.");
                return "https://myUrlService.ly/" + exist.getShortURL();
            }
        }

        // 1. Custom alias case
        if (customAlias != null && !customAlias.isEmpty()) {
            if (customAliasSet.contains(customAlias)) {
                throw new RuntimeException("Custom alias already exists!");
            }
            shortKey = customAlias;
            customAliasSet.add(customAlias);
        } else {
            // 2. Auto-generate unique short key
            do {
                shortKey = ShortUrlGenerator.generateShortKey();
            } while (shortKeyMap.containsKey(shortKey));
        }

        // 3. Create Url object
        Url url = (userId != -1)
                ? new Url(userId, longURL, shortKey, customAlias, expiryDate)
                : new Url(longURL, shortKey, customAlias, expiryDate);

        // 4. Store in maps
        shortKeyMap.put(shortKey, url);
        if (userId != -1) {
            userUrlsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(url);
        }

        return "Created URL " + "https://myUrlService.ly/" + shortKey;
    }

    public String getLongURL(String shortKey) {
        Url url = shortKeyMap.get(shortKey);
        if (url == null)
            return "Not Found";

        // 1. Check expiry
        if (url.getExpireDate() != null && LocalDate.now().isAfter(url.getExpireDate())) {
            shortKeyMap.remove(shortKey);
            if (url.getCustomAlias() != null) {
                customAliasSet.remove(url.getCustomAlias());
            }
            if (userUrlsMap.containsKey(url.getUserId())) {
                userUrlsMap.get(url.getUserId()).remove(url);
            }
            return "Link Expired! This URL is deleted.";
        }

        // 2. Increase click count
        url.setClickCount(url.getClickCount() + 1);
        return url.getLongURL();
    }

    public void deleteURL(long userId, long urlID) {
        List<Url> urls = userUrlsMap.get(urlID);
        for (int i = 0; i < urls.size(); i++) {
            if (urls.get(i).getUrlId() == urlID) {
                urls.remove(i);
                System.out.println("URL removed successfully");
                return;
            }
        }
        System.out.println("Enter valid URLID");
        return;

    }

    public void editURL(long userId, long urlId) {
        List<Url> urls = userUrlsMap.get(urlId);
        Url url = null;
        for (int i = 0; i < urls.size(); i++) {
            if (urls.get(i).getUrlId() == urlId) {
                url = urls.get(i);
            }
        }
        if (url == null) {
            System.out.println("Incorrect URL ID");
            return;
        }
        // longurl,customallias,expire date
        String longUrl = "";
        String customAlias = "";
        LocalDate eDate = null;
        System.out.println("Enter new LongURL or press enter to skip");
        longUrl = sc.nextLine();
        longUrl = longUrl.trim();
        if (!longUrl.isEmpty()) {
            while (isValidURL(longUrl) == false) {
                System.out.println("Enter a valid URL");
                longUrl = sc.nextLine();
            }
        }

        System.out.println("Enter 1 to update customAlias\nEnter 2 to remove customAlias\nPress Enter to skip");
        input = sc.nextLine();
        if (input.equals("1")) {
            System.out.println("Enter new CustomAlias");
            input = sc.nextLine();
            while (customAliasSet.contains(input)) {
                System.out.println(
                        "This CustomAlias already exits Enter a different one or press enter to skip this");
                input = sc.nextLine();
                input = input.trim();
                if (input.isEmpty())
                    break;
            }
            customAlias = input;

        } else if (input.equals("2")) {
            url.setCustomAlias(null);
        }

        while (true) {
            System.out.println("Enter new Expire date in this format (yyyy-MM-dd) or press Enter to skip:");
            String input = sc.nextLine().trim();

            // 1. Skip if empty
            if (input.isEmpty()) {
                eDate = null;
                break;
            }

            try {
                // 2. Parse date
                eDate = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);

                // 3. Check if within next 30 years
                LocalDate maxDate = LocalDate.now().plusYears(30);
                if (eDate.isAfter(maxDate)) {
                    System.out.println(
                            "Date cannot be more than 30 years from now. Please re-enter or press Enter to skip.");
                    continue;
                }

                // 4. If valid, exit loop
                break;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format!");
            }
        }

        if (longUrl != null)
            url.setLongURL(longUrl);
        if (customAlias != null) {
            url.setCustomAlias(customAlias);
            url.setShortURL(customAlias);
        }
        if (eDate != null)
            url.setExpireDate(eDate);
        System.out.println("URL updated successfully");
    }

    public boolean isValidURL(String url) {

        return url != null && url.matches(URL_REGEX);
    }

}
