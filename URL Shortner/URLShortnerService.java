import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class URLShortnerService {
    private ConcurrentHashMap<String, Url> shortKeyMap;
    private Map<Long, List<Url>> userUrlsMap;
    private Set<String> customAliasSet;
    private Map<String, Boolean> aliasRegistry; // alias -> true

    private Scanner sc;
    private String input = "";
    private final String URL_REGEX = "^(https?://)" + "([\\w.-]+)" + "(\\.[a-zA-Z]{2,6})" + "([\\w\\-./?%&=]*)?$";

    public URLShortnerService(ConcurrentHashMap<String, Url> shortKeyMap, Map<Long, List<Url>> userUrlsMap,
            Set<String> customAliasSet, Scanner sc, Map<String, Boolean> aliasRegistry) {
        this.shortKeyMap = shortKeyMap;
        this.userUrlsMap = userUrlsMap;
        this.customAliasSet = customAliasSet;
        this.sc = sc;
        this.aliasRegistry = aliasRegistry;
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
            // Keep asking for a unique custom alias until user gives one or skips
            while (aliasRegistry.containsKey(customAlias)) {
                System.out.println("This CustomAlias already exists. Enter a different one or press Enter to skip:");
                customAlias = sc.nextLine().trim();
                if (customAlias.isEmpty()) {
                    customAlias = null; // force fallback to auto-generate
                    break;
                }
            }

            if (customAlias != null && !customAlias.isEmpty()) {
                aliasRegistry.put(customAlias, Boolean.TRUE);
                shortKey = customAlias;
            } else {
                // Auto-generate since custom alias skipped
                do {
                    shortKey = ShortUrlGenerator.generateShortKey();
                } while (shortKeyMap.containsKey(shortKey) || aliasRegistry.containsKey(shortKey));
                aliasRegistry.put(shortKey, Boolean.TRUE);
            }
        } else {
            // Generate a unique short key if no custom alias provided
            do {
                shortKey = ShortUrlGenerator.generateShortKey();
                // Check if key is free without inserting the final Url object yet
            } while (shortKeyMap.containsKey(shortKey));
        }

        // 3. Create Url object
        Url url = (userId != -1)
                ? new Url(userId, longURL, shortKey, customAlias, expiryDate)
                : new Url(longURL, shortKey, customAlias, expiryDate);

        // Put in the map only if key is still free (atomic insert)
        Url existing = shortKeyMap.putIfAbsent(shortKey, url);
        if (existing != null) {
            // Rare collision after Url object creation, retry generation
            // You could loop here again or recursively call the method
            return shortenURL(userId, longURL, customAlias, expiryDate);
        }

        if (userId != -1) {
            userUrlsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(url);
        }

        return "Created URL https://myUrlService.ly/" + shortKey;
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
        List<Url> urls = userUrlsMap.get(userId);
        if (urls == null || urls.isEmpty()) {
            System.out.println("No URLs found for this user");
            return;
        }
        for (int i = 0; i < urls.size(); i++) {
            if (urls.get(i).getUrlId() == urlID) {
                Url urlToRemove = urls.get(i); // store before removal
                urls.remove(i);

                if (urlToRemove.getCustomAlias() != null) {
                    aliasRegistry.remove(urlToRemove.getCustomAlias());
                }

                System.out.println("URL removed successfully");
                return;
            }
        }
        System.out.println("Enter valid URLID");

    }

    public void editURL(long userId, long urlId) {
        List<Url> urls = userUrlsMap.get(userId);
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
        String longUrl = null;
        String customAlias = null;
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
            input = input.trim();

            // Keep asking until alias is unique or user skips
            while (!input.isEmpty() && aliasRegistry.containsKey(input)) {
                System.out.println(
                        "This CustomAlias already exists. Enter a different one or press Enter to skip this");
                input = sc.nextLine();
                input = input.trim();
            }

            if (!input.isEmpty()) {
                // Remove old alias if exists
                if (url.getCustomAlias() != null) {
                    aliasRegistry.remove(url.getCustomAlias());
                }
                customAlias = input;
                aliasRegistry.put(customAlias, Boolean.TRUE); // reserve the new alias
                // url.setCustomAlias(customAlias);
            }

        } else if (input.equals("2")) {
            if (url.getCustomAlias() != null) {
                aliasRegistry.remove(url.getCustomAlias()); // free up the alias
                url.setCustomAlias(null);
                customAlias = null;
            }
            do {
                customAlias = ShortUrlGenerator.generateShortKey();
                // Check if key is free without inserting the final Url object yet
            } while (shortKeyMap.containsKey(customAlias));

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

                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusYears(30);

                // 3. Check if date is greater than today
                if (!eDate.isAfter(today)) {
                    System.out.println("Date must be greater than today's date.");
                    continue;
                }

                // 4. Check if within next 30 years
                if (eDate.isAfter(maxDate)) {
                    System.out.println(
                            "Date cannot be more than 30 years from now. Please re-enter or press Enter to skip.");
                    continue;
                }

                // 5. If valid, exit loop
                break;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format!");
            }
        }

        if (longUrl != null && !longUrl.isEmpty())
            url.setLongURL(longUrl);
        if (customAlias != null && !customAlias.isEmpty()) {

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
