package de.marcey.hsmp.commands;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PvpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(!p.hasPermission("HSMP.Admin")){
            return false;
        }
        if(args.length == 0){
            p.sendMessage(Strings.prefix + " §7Bitte nutze §c/pvp on/off");
            return false;
        } else if(args.length >= 1){
            FileConfiguration config = HSMP.getInstance().getConfig();
            if(args[0].equalsIgnoreCase("on")){
                if(config.contains("PVP") && config.getBoolean("PVP")){
                    p.sendMessage(Strings.prefix + " §cPVP ist bereits aktiviert.");
                    return false;
                }
                config.set("PVP", true);
                HSMP.getInstance().saveConfig();
                for(Player current : Bukkit.getOnlinePlayers()){
                    current.sendMessage(Strings.prefix + " §bPVP §7wurde §aaktiviert.");
                    current.sendMessage(Strings.prefix + " §cAchtung! §7Du kannst nun angegriffen werden.");
                }
            } else if(args[0].equalsIgnoreCase("off")){
                if(!config.contains("PVP") && !config.getBoolean("PVP")){
                    p.sendMessage(Strings.prefix + " §cPVP ist bereits deaktiviert.");
                    return false;
                }
                config.set("PVP", false);
                HSMP.getInstance().saveConfig();
                for(Player current : Bukkit.getOnlinePlayers()){
                    current.sendMessage(Strings.prefix + " §bPVP §7wurde §cdeaktiviert.");
                    current.sendMessage(Strings.prefix + " §7Dich kann nun kein Spieler mehr angreifen.");
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        List<String> list = new ArrayList<>();
        String input = "";
        if(args.length == 1){
            input = args[0];
            list.add("on");
            list.add("off");
        }
        return TabComplete.sort(input, list);
    }
}
