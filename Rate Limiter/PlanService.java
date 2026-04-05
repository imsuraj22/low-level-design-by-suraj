import java.util.concurrent.ConcurrentHashMap;

public class PlanService {

    private ConcurrentHashMap<String, Plan> clientPlans = new ConcurrentHashMap<>();

    public void registerClient(String clientId, Plan plan) {
        clientPlans.put(clientId, plan);
    }

    public Plan getPlan(String clientId) {
        return clientPlans.getOrDefault(clientId, Plan.FREE);
    }
}