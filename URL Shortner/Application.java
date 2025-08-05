import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Application {
    private Map<String, Url> shortKeyMap = new HashMap<>();
    private Map<Long, List<Url>> userUrlsMap = new HashMap<>();
    private List<User> registeredUsers = new ArrayList<>();
    private Set<String> customAliasSet = new HashSet<>();

    public static void main(String[] args) {

    }
}
