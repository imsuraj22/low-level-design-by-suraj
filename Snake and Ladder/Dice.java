
import java.util.Random;

public class Dice {
    private static Dice dice = null;
    private Random random;

    private Dice() {
        random = new Random();
    }

    public static Dice getInstance() {
        if (dice == null) {
            dice = new Dice();
        }
        return dice;
    }

    public int roll() {
        return random.nextInt(6) + 1;
    }

}
