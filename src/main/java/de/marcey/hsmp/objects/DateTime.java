package de.marcey.hsmp.objects;

import de.marcey.hsmp.utils.DateUtil;

import java.util.Date;

public class DateTime {
    private int formattedSeconds;
    private int formattedMinutes;
    private int formattedHours;
    private int formattedDays;
    private long remaining;

    public DateTime(Date date){
        this.remaining = date.getTime()-DateUtil.getCurrentDate().getTime();
        this.formattedDays = (int) (remaining / (1000 * 60 * 60 * 24));
        remaining %= (1000 * 60 * 60 * 24);
        this.formattedHours = (int) (remaining / (1000 * 60 * 60));
        remaining %= (1000 * 60 * 60);
        this.formattedMinutes = (int) (remaining / (1000 * 60));
        remaining %= (1000 * 60);
        this.formattedSeconds = (int) (remaining / 1000);
    }

    public int getFormattedDays() {
        return formattedDays;
    }

    public int getFormattedHours() {
        return formattedHours;
    }

    public int getFormattedMinutes() {
        return formattedMinutes;
    }

    public int getFormattedSeconds() {
        return formattedSeconds;
    }

}
