package de.marcey.hsmp.commands;

import de.marcey.hsmp.utils.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

public class SpawnElytraCommand implements TabExecutor {

    //Diese Class ist copied von einem älteren Plugin

    @Override
    public boolean onCommand(CommandSender s, Command command, String l, String[] args) {

        if(s instanceof Player){
            Player p = (Player) s;
            if(p.hasPermission("SpawnElytra.Admin")){
                Config cfg = new Config("Locations");
                FileConfiguration config = cfg.getConfig();
                if(args.length == 0){
                    p.sendMessage("§7Bitte benutze §c/spawnelytra <setspawn/setboost/setradius>");
                }
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("setspawn")){
                        config.set("SpawnLocation", p.getLocation());
                        config.set("Worldname", p.getLocation().getWorld().getName());
                        cfg.save();
                        p.sendMessage("§aSpawn-Location wurde erfolgreich gesetzt!");
                    }
                }
                if(args.length == 2){
                    if(args[0].equalsIgnoreCase("setboost")){
                        if(isNumeric(args[1])){
                            int boost = Integer.parseInt(args[1]);
                            config.set("Boost", boost);
                            cfg.save();
                            p.sendMessage("§aDer Boost wurde auf §c" + boost + " §agesetzt!");
                        } else p.sendMessage("§7Bitte benutze §c/spawnelytra <setspawn/setboost/setradius>");
                    }
                    if(args[0].equalsIgnoreCase("setradius")){
                        if(isNumeric(args[1])){
                            int radius = Integer.parseInt(args[1]);
                            config.set("Radius", radius);
                            cfg.save();
                            p.sendMessage("§aDer Radius wurde auf §c" + radius + " §agesetzt!");
                        } else p.sendMessage("§7Bitte benutze §c/spawnelytra <setspawn/setboost/setradius>");
                    }
                    if(args[0].equalsIgnoreCase("setboostenabled")){
                        if(args[1].equalsIgnoreCase("true")){
                            config.set("BoostEnabled", true);
                            cfg.save();
                            p.sendMessage("§aBoost ist nun aktiviert!");
                        }
                        if(args[1].equalsIgnoreCase("false")){
                            config.set("BoostEnabled", false);
                            cfg.save();
                            p.sendMessage("§cBoost ist nun deaktiviert!");
                        }
                    }

                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command command, String l, String[] args) {

        List<String> list = new ArrayList<>();
        if(s instanceof Player){
            Player p = (Player) s;
            if(p.hasPermission("SpawnElytra.Admin")){
                if(args.length == 1){
                    list.add("setboost");
                    list.add("setspawn");
                    list.add("setradius");
                    list.add("setboostenabled");
                }
                if(args.length == 2){
                    if(args[0].equalsIgnoreCase("setradius")){
                        list.add("<Radius>");
                    }
                    if(args[0].equalsIgnoreCase("setboost")){
                        list.add("<Boost>");
                    }
                    if(args[0].equalsIgnoreCase("setboostenabled")){
                        list.add("<true/false>");
                    }
                }
            }
        }

        return list;
    }

    public static boolean isNumeric(String str) {
        ParsePosition pos = new ParsePosition(0);
        NumberFormat.getInstance().parse(str, pos);
        return str.length() == pos.getIndex();
    }

}
