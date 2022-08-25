package pl.pacinho.codeguessr.utils;

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class NumberFormatterUtils {

    public static final NumberFormatter intFormatter = initIntFormatter();
    private static NumberFormatter initIntFormatter() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(true);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }

}
