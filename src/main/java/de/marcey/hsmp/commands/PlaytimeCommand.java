package de.marcey.hsmp.commands;

import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.Playtime;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(args.length == 0){
            p.sendMessage(Strings.prefix + " §7Deine Spielzeit beträgt §e" + Playtime.getFormattedPlaytime(p.getUniqueId()));
        } else if(args.length >= 1){
            String name = args[0];
            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
            if(op == null){
                p.sendMessage(Strings.prefix + " §cDer Spieler war noch nie online!");
                return false;
            }
            p.sendMessage(Strings.prefix + " §7Die Spielzeit von " + Rang.getRang(op.getUniqueId()).getFormattedDisplayname() + op.getName() + " §7beträgt §e" + Playtime.getFormattedPlaytime(op.getUniqueId()));
        }

        return false;
    }
}
