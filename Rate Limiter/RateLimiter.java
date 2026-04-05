import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private ConcurrentHashMap<String, Bucket> buckets;
    private PlanService planService;

    public RateLimiter(PlanService planService) {
        this.buckets = new ConcurrentHashMap<>();
        this.planService = planService;
    }

    public boolean allowRequest(String clientId) {

        Plan plan = planService.getPlan(clientId);

        Bucket bucket = buckets.computeIfAbsent(
                clientId,
                id -> new Bucket(plan.getCapacity())
        );

        return bucket.tryConsume();
    }
}