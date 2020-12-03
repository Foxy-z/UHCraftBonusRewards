package fr.uhcraft.bonusrewards.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {

    public static Double round(Double value, int places) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String format(double i) {
        return i % 1.0D == 0.0D ? format((long) i) : (new DecimalFormat("#,###.00")).format(i);
    }

    private static String format(long i) {
        return (new DecimalFormat("#,###")).format(i);
    }
}
