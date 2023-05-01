package de.marcey.hsmp.utils;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.listener.RangListener;
import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeProvider {

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    private static String currentTimeStamp = "";

    public static String getTime(){
        return LocalTime.now().format(format) + " Uhr";
    }

    public static void timer(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(HSMP.getInstance(), new Runnable() {
            @Override
            public void run() {

                String time = getTime();
                if(!currentTimeStamp.equalsIgnoreCase(time)){
                    currentTimeStamp = time;
                    RangListener.setTablist();
                }

            }
        }, 0, 20);
    }

}
