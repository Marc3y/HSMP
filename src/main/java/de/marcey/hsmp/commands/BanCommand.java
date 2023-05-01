package de.marcey.hsmp.commands;

import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(!p.hasPermission("HSMP.Moderator") || Rang.getRang(p).getPriority() > 1){
            p.sendMessage(Strings.prefix + " §cDafür hast du keine Rechte.");
            return false;
        }

        if(args.length == 1){
            String name = args[0];
            Player target = Bukkit.getPlayer(name);
            if(target != null){

            }
        }

        return false;
    }
}
