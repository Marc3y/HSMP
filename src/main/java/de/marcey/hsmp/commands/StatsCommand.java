package de.marcey.hsmp.commands;

import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.gui.PlayerStatsGUI;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(args.length == 0){
            PlayerStatsGUI.openStatsInv(p, p.getUniqueId(), Rang.getRang(p).getPriority() <= 2);
        } else {
            String name = args[0];
            if(Bukkit.getOfflinePlayer(name) == null){
                p.sendMessage(Strings.prefix + " §cDieser Spieler war noch nie auf dem Server.");
                return false;
            }
            PlayerStatsGUI.openStatsInv(p, Bukkit.getOfflinePlayer(name).getUniqueId(), Rang.getRang(p).getPriority() <= 2);
        }

        return false;
    }
}
