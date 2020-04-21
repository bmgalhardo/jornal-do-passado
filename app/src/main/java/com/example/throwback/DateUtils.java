package com.example.throwback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    public static String getToday(boolean verbose) {

        Date date = new Date();

        if (verbose) {

            Map<Integer, String> monthMap = new HashMap<Integer, String>();

            monthMap.put(0, "Janeiro");
            monthMap.put(1, "Fevereiro");
            monthMap.put(2, "Março");
            monthMap.put(3, "Abril");
            monthMap.put(4, "Maio");
            monthMap.put(5, "Junho");
            monthMap.put(6, "Julho");
            monthMap.put(7, "Agosto");
            monthMap.put(8, "Setembro");
            monthMap.put(9, "Outubro");
            monthMap.put(10, "Novembro");
            monthMap.put(11, "Dezembro");

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int monthID = cal.get(Calendar.MONTH);
            int dayID = cal.get(Calendar.DAY_OF_MONTH);
            int yearID = cal.get(Calendar.YEAR);
            String month = monthMap.get(monthID);
            return String.format("%d de %s de %d", dayID, month, yearID);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(date);
        }
    }

}
