package pl.pacinho.codeguessr.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
    public static String getTime(long seconds) {
        return timeFormat.format(getDate(seconds));
    }

    private static Date getDate(long seconds) {
        return new Date(seconds * 1000L);
    }
}
