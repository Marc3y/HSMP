package de.marcey.hsmp.utils;

import de.marcey.hsmp.objects.DateTime;
import de.marcey.hsmp.objects.enums.TimeSymbol;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date getTime(int add, TimeSymbol symbol){
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        date.setSeconds(now.getSecond());
        date.setMinutes(now.getMinute());
        date.setHours(now.getHour());
        date.setDate(now.getDayOfMonth());
        date.setMonth(now.getMonthValue());
        date.setYear(now.getYear());

        if (symbol.equals(TimeSymbol.SECONDS)) {
            date.setSeconds(now.getSecond() + add);
        } else if (symbol.equals(TimeSymbol.MINUTES)) {
            date.setMinutes(now.getMinute() + add);
        } else if (symbol.equals(TimeSymbol.DAYS)) {
            date.setDate(now.getDayOfMonth() + add);
        } else if (symbol.equals(TimeSymbol.MONTHS)) {
            date.setMonth(now.getMonthValue() + add);
        } else if (symbol.equals(TimeSymbol.YEARS)) {
            date.setYear(now.getYear() + add);
        }

        return date;
    }

    public static String dateToString(Date date){
        return (date.getYear() + " " + date.getMonth() + " " + date.getDate() + " " + date.getHours() + " " + date.getMinutes() + " " + date.getSeconds());
    }

    public static Date stringToDate(String value) {
        String[] args = value.split("\\s+");
        try {
            int year = Integer.parseInt(args[0]);
            int month = Integer.parseInt(args[1]);
            int date = Integer.parseInt(args[2]);
            int hour = Integer.parseInt(args[3]);
            int minute = Integer.parseInt(args[4]);
            int second = Integer.parseInt(args[5]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, date, hour, minute, second);
            return calendar.getTime();
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static Date getCurrentDate(){
        return new Date();
    }

    public static DateTime getRemaining(Date date){
        return new DateTime(date);
    }

    /**
     * @param format %Y = Year, %M = Month, %D = Day, %H = Hour, %MIN = Minute, %S = SECOND
     */
    public static String formatDate(Date date, String format){
        String result = "";
        result = format.replaceAll("%Y", date.getYear() + "");
        result = result.replaceAll("%M", date.getMonth() + "");
        result = result.replaceAll("%D", date.getDate() + "");
        result = result.replaceAll("%H", date.getHours() + "");
        result = result.replaceAll("%MIN", date.getMinutes() + "");
        result = result.replaceAll("%S", date.getSeconds() + "");
        return result;
    }

    public static boolean isAfter(Date date){
        return date.after(getCurrentDate());
    }


}
