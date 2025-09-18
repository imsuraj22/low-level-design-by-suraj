
import java.util.Random;

public class Dice {
    private Random random;
    int n = 0;

    public Dice(int n) {
        random = new Random();
        this.n = n;
    }

    public int roll() {
        return random.nextInt(n) + 1;
    }

}
