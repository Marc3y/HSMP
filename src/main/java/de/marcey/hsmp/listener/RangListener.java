package de.marcey.hsmp.listener;

import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.objects.PlayerStat;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.PlayerStats;
import de.marcey.hsmp.scoreboard.ScoreboardManager;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.TimeProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RangListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Rang rang = Rang.getRang(p);
        Clan clan = ClanProvider.getClan(p);
        e.setQuitMessage("§c« §r" + (clan != null ? "§f[§r" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" : "") + rang.getFormattedDisplayname() + rang.getColor() + p.getName() + " §r§7hat das HSMP verlassen");
    }

    @EventHandler
    public void onChat(PlayerChatEvent e){
        Player p = e.getPlayer();
        Rang rang = Rang.getRang(p);
        String message = e.getMessage();
        Clan clan = ClanProvider.getClan(p);
        if(!message.toLowerCase().contains("@clan")){
            if(clan == null) {
                e.setFormat(rang.getFormattedDisplayname() + rang.getColor() + p.getName() + "§7: §r" + message);
            } else {
                e.setFormat("§r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" + rang.getFormattedDisplayname() + rang.getColor() + p.getName() + "§7: §r" + message);
            }
            PlayerStats stats = PlayerStats.getPlayerStats(p.getUniqueId());
            stats.addToStat(PlayerStat.CHAT_MESSAGES_SEND, 1);
        } else {
            if(clan == null) {
                p.sendMessage(Strings.prefix + " §cDu hast gerade versucht, eine Nachricht in den Clan-Chat zu schreiben obwohl du in keinem Clan bist. Die Nachricht wurde nicht gesendet.");
                e.setCancelled(true);
                return;
            }
            if(!clan.isClanChatEnabled()){
                p.sendMessage(Strings.prefix + " §cDer Clan-Chat ist deaktiviert. Clan-Operator können den Clan-Chat mit §e/clan settings §ceinschalten. Die Nachricht wurde nicht gesendet.");
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
            for(Player current : Bukkit.getOnlinePlayers()){
                if(clan.getMitglieder().contains(current.getUniqueId())){
                    message = message.replaceAll("(?i)@clan", "");
                    current.sendMessage("§7(Clan) §r§f[§r§7" + clan.getDisplayname().replace('&', '§') + "§r§f] §r" + rang.getFormattedDisplayname() + rang.getColor() + p.getName() + "§7: §r" + (message.startsWith(" ") ? message.replaceFirst(" ", "") : message));
                }
            }
            PlayerStats stats = PlayerStats.getPlayerStats(p.getUniqueId());
            stats.addToStat(PlayerStat.CHAT_MESSAGES_SEND, 1);
        }
    }

    public static void setTablist(){
        for(Player current : Bukkit.getOnlinePlayers()){
            boolean hasClan = ClanProvider.getClan(current) != null;
            current.setPlayerListHeader("§7\n§7   " + " §r§8--- §r§7" + Strings.smpname + " §r§8---    \n§7Spieler auf dem HSMP: §c" + Bukkit.getOnlinePlayers().size() + "\n§7\n" + (hasClan ? "§7Clan: §e" + ClanProvider.getClan(current).getDisplayname().replace('&', '§') + "§7\n§7" : "") +
                    "");
            current.setPlayerListFooter("§7\n§ctwitch.tv/Kenjih\n§6twitch.tv/Tjan\n§7\n§7" + TimeProvider.getTime() + "\n§7" + "\n§e/help §7um eine Hilfe zu bekommen \n§7");
        }
    }

}
