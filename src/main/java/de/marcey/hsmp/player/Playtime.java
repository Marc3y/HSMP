package de.marcey.hsmp.player;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.listener.StatsListener;
import de.marcey.hsmp.objects.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Playtime {

    public static void updater(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(HSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(Player current : Bukkit.getOnlinePlayers()){
                    if(!is5MinutesOnline(current)){
                        continue;
                    }
                    PlayerStats.getPlayerStats(current.getUniqueId()).addToStat(PlayerStat.PLAYTIME, 300);
                }
            }
        }, 0, 300*20);
    }

    private static boolean is5MinutesOnline(Player p){
        long current = System.currentTimeMillis();
        long has = StatsListener.joined.get(p);
        return ((current-has) >= 300000);
    }

    public static String getFormattedPlaytime(UUID uuid) {
        int seconds = PlayerStats.getPlayerStats(uuid).getStat(PlayerStat.PLAYTIME);
        int days = seconds / (24 * 3600);
        seconds %= (24 * 3600);
        int hours = seconds / 3600;
        seconds %= 3600;
        int minutes = seconds / 60 ;
        seconds %= 60;
        if(days > 0) {
            return days + "d " + hours + "h " + minutes + "min";
        } else if(hours > 0){
            return hours + "h " + minutes + "min";
        } else {
            return minutes + "min";
        }
    }

}
