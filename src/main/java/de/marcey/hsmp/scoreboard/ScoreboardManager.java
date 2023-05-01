package de.marcey.hsmp.scoreboard;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.listener.RangListener;
import de.marcey.hsmp.objects.Rang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {

    private static Scoreboard sb;

    public static void setScoreboard(Player p){
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = sb.registerNewObjective("n", "dummy");


        for(Player current : Bukkit.getOnlinePlayers()){
            String team = current.getUniqueId().toString();
            team = "00" + Rang.getRang(current).getPriority() + current.getUniqueId();
            if(sb.getTeam(team) != null){
                sb.getTeam(team).unregister();
            }
            sb.registerNewTeam(team);
            Rang rang = Rang.getRang(current);
            if(ClanProvider.getClan(current) != null){
                Clan clan = ClanProvider.getClan(current);
                if(!rang.equals(Rang.SPIELER)) {
                    sb.getTeam(team).setPrefix("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r§7" + rang.getFormattedDisplayname());
                    sb.getTeam(team).setColor(rang.getColor());
                } else {
                    sb.getTeam(team).setPrefix("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" + rang.getColor());
                    sb.getTeam(team).setColor(rang.getColor());
                }
            } else {
                if(!rang.equals(Rang.SPIELER)) {
                    sb.getTeam(team).setPrefix(rang.getFormattedDisplayname());
                    sb.getTeam(team).setColor(rang.getColor());
                } else {
                    sb.getTeam(team).setPrefix(rang.getColor() + "");
                    sb.getTeam(team).setColor(rang.getColor());
                }
            }
        }


        for (Player current : Bukkit.getOnlinePlayers()) {
            setTeams(current);
        }

    }

    public static int taskID;
    public static void updater(){
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(!Bukkit.getOnlinePlayers().isEmpty()) {
                    for (Player current : Bukkit.getOnlinePlayers()) {
                        setTeams(current);
                    }
                }
            }
        }, 0, 200);
    }

    public static void updateScoreboard(){
        for(Player current : Bukkit.getOnlinePlayers()){
            setTeams(current);
        }
        RangListener.setTablist();
    }

    private static void setTeams(Player p){
        try {
            Rang rang = Rang.getRang(p);
            String team = "00" + rang.getPriority() + p.getUniqueId();
            Team t = sb.getTeam(team);
            if (t == null) {
                sb.registerNewTeam(team);
                Clan clan = ClanProvider.getClan(p);
                if(clan != null) {
                    if(!rang.equals(Rang.SPIELER)) {
                        sb.getTeam(team).setPrefix("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r§7" + rang.getFormattedDisplayname());
                        sb.getTeam(team).setColor(rang.getColor());
                    } else {
                        sb.getTeam(team).setPrefix("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" + rang.getColor());
                        sb.getTeam(team).setColor(rang.getColor());
                    }
                } else {
                    if(!rang.equals(Rang.SPIELER)) {
                        sb.getTeam(team).setPrefix(rang.getFormattedDisplayname());
                        sb.getTeam(team).setColor(rang.getColor());
                    } else {
                        sb.getTeam(team).setPrefix(rang.getColor() + "");
                        sb.getTeam(team).setColor(rang.getColor());
                    }
                }
            }
            Clan clan = ClanProvider.getClan(p);
            if(clan != null) {
                if (!sb.getTeam(team).getPrefix().contains(clan.getDisplayname())) {
                    if(!rang.equals(Rang.SPIELER)) {
                        sb.getTeam(team).setPrefix("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r§7" + rang.getFormattedDisplayname());
                        sb.getTeam(team).setColor(rang.getColor());
                    } else {
                        sb.getTeam(team).setPrefix("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" + rang.getColor());
                        sb.getTeam(team).setColor(rang.getColor());
                    }
                }
            } else {
                if (sb.getTeam(team).getPrefix().contains("§f[")) {
                   if(!rang.equals(Rang.SPIELER)) {
                       sb.getTeam(team).setPrefix(rang.getFormattedDisplayname());
                       sb.getTeam(team).setColor(rang.getColor());
                   } else {
                       sb.getTeam(team).setPrefix(rang.getColor() + "");
                       sb.getTeam(team).setColor(rang.getColor());
                   }
                }
            }
            sb.getTeam(team).addPlayer(p);
            p.setScoreboard(sb);
        } catch (Exception e){

        }
    }

}
