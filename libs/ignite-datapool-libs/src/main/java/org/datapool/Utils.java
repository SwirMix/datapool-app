package org.datapool;

import java.util.Random;

public class Utils {
    public static Long generateRandomRange(Long min, Long max) {
        return Math.abs((new java.util.Random().nextLong() % (max - min)) + min);
    }
}
