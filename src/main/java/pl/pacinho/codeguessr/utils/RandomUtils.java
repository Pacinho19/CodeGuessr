package pl.pacinho.codeguessr.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static int getInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
