import java.util.concurrent.locks.ReentrantLock;

public class Bucket{

    int capacity;
    double tokens;
    double refillRate;
    double lastRefillTime;

    private final ReentrantLock lock = new ReentrantLock();

    public Bucket(int cap){
        this.capacity=cap;
        this.tokens=cap;
        this.refillRate = ((double) capacity) / 60;
        this.lastRefillTime=System.currentTimeMillis();;
    }

    public void refillTokens(){
         long now = System.currentTimeMillis();
         double elapsedSeconds = (now - lastRefillTime) / 1000.0; // convert ms → seconds
         tokens = Math.min(capacity, tokens + elapsedSeconds * refillRate);
         lastRefillTime = now; // update lastRefillTime!
    }

    public boolean tryConsume(){
        lock.lock();
        try {
            refillTokens();
            if(tokens>=1){
                tokens-=1;
                return true;
            }
            return false;
        } finally {
        lock.unlock();
    }

    }
    
}