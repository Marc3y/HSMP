package de.marcey.hsmp.commands;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class OpenEndCommand implements CommandExecutor {
    private static int taskID;
    private static boolean isTaskRunning = false;
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(!p.hasPermission("HSMP.Admin")){
            p.sendMessage(Strings.prefix + " §cDazu hast du keine Rechte.");
            return false;
        }

        if(args.length == 0){
            p.sendMessage(Strings.prefix + " §7Bitte nutze §c/openend <Sekunden/cancel/close>");
        } else if(args.length == 1 && !args[0].equalsIgnoreCase("cancel") && !args[0].equalsIgnoreCase("close")){
            int i = -1;
            try {
                i = Integer.parseInt(args[0]);
            } catch (Exception e){
                p.sendMessage(Strings.prefix + " §7Bitte nutze §c/openend <Sekunden/cancel/close>");
                return false;
            }
            if(i < 0){
                p.sendMessage(Strings.prefix + " §7Bitte nutze §c/openend <Sekunden/cancel/close>");
                return false;
            }
            startEnd(p, i);
        } else if(args.length == 1 && args[0].equalsIgnoreCase("cancel")){
            if(isTaskRunning){
                Bukkit.getScheduler().cancelTask(taskID);
                p.sendMessage(Strings.prefix + " §7Der Countdown wurde §cabgebrochen§7.");
            } else p.sendMessage(Strings.prefix + " §cEs läuft derzeit kein Cooldown.");
        } else if(args.length == 1 && args[0].equalsIgnoreCase("close")){
            HSMP.getInstance().getConfig().set("OpenEnd", false);
            HSMP.getInstance().saveConfig();
            for(Player current : Bukkit.getOnlinePlayers()){
                current.sendMessage(Strings.prefix + " §7Das Ende wurde §cgeschlossen§7.");
                current.playSound(current.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 40, 1);
            }
            if(isTaskRunning){
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }

        return false;
    }

    private static void startEnd(Player host, int seconds){
        final int maxseconds = seconds;
        FileConfiguration config = HSMP.getInstance().getConfig();
        if(seconds <= 0){
            config.set("OpenEnd", true);
            HSMP.getInstance().saveConfig();
            for(Player current : Bukkit.getOnlinePlayers()){
                current.playSound(current.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100, 0.8f);
                current.sendTitle(Strings.openendtitle, Strings.openendsubtitle, 10, 200, 10);
                current.sendMessage(Strings.prefix + " §7Das Ende wurde offiziell §ageöffnet§7. Es ist nun möglich, das Ende zu betreten.");
            }
            isTaskRunning = false;
            return;
        }
        isTaskRunning = true;
        int ticks =  (maxseconds*20) >= 20 ? ((maxseconds*20)/4) : (maxseconds*20) > 10 ? (10*20) : (maxseconds*20);
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HSMP.getInstance(), new Runnable() {
            int s = seconds;
            @Override
            public void run() {

                if(s > 0) {
                    for (Player current : Bukkit.getOnlinePlayers()) {
                        current.playSound(current.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
                        current.sendMessage(Strings.prefix + " §7Das Ende wird in §e" + s + " §7Sekunden geöffnet.");
                    }
                }

                if(s <= 0){
                    config.set("OpenEnd", true);
                    HSMP.getInstance().saveConfig();
                    for(Player current : Bukkit.getOnlinePlayers()){
                        current.playSound(current.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100, 0.8f);
                        current.sendTitle(Strings.openendtitle, Strings.openendsubtitle, 10, 200, 10);
                        current.sendMessage(Strings.prefix + " §7Das Ende wurde offiziell §ageöffnet§7. Es ist nun möglich, das Ende zu betreten.");
                    }
                    isTaskRunning = false;
                    Bukkit.getScheduler().cancelTask(taskID);
                }

                s = s-(ticks/20);

            }
        }, 0, ticks);
    }
}
