package de.marcey.hsmp.commands;

import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.scoreboard.ScoreboardManager;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RangCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(s instanceof Player){
            Player p = (Player) s;
            if(p.hasPermission("HSMP.Admin")){
                if(args.length == 0){
                    p.sendMessage(Strings.prefix + " §7Dein Rang ist " + Rang.getRang(p).getColor() + Rang.getRang(p).getDisplayname());
                    p.sendMessage(Strings.prefix + " §cBitte nutze §e/rang <Name> <Rang>");
                }
                if(args.length == 2){
                    String name = args[0];
                    String rang = args[1];
                    Rang rangToSet = null;
                    for(Rang r : Rang.getRangs()){
                        if(r.getDisplayname().equalsIgnoreCase(rang) || r.getName().equalsIgnoreCase(rang)){
                            rangToSet = r;
                        }
                    }
                    Player target = Bukkit.getPlayer(name);
                    if(target == null){
                        p.sendMessage(Strings.prefix + " §cDer Spieler ist offline.");
                        return false;
                    }
                    if(rangToSet == null){
                        p.sendMessage(Strings.prefix + " §cDer Rang existiert nicht.");
                        return false;
                    }

                    Rang.setRang(target, rangToSet);
                    ScoreboardManager.updateScoreboard();
                    p.sendMessage(Strings.prefix + " §7Du hast §6" + target.getName() + " §7den " + rangToSet.getColor() + rangToSet.getDisplayname() + "§7-Rang gegeben.");
                    target.sendMessage(Strings.prefix + " §7Du hast den " + rangToSet.getColor() + rangToSet.getDisplayname() + "§7-Rang bekommen.");
                }
            } else {
                p.sendMessage(Strings.prefix + " §7Du besitzt den Rang " + Rang.getRang(p).getColor() + Rang.getRang(p).getDisplayname());
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        String input = "";
        if(s instanceof Player){
            Player p = (Player) s;
            if(!p.hasPermission("HSMP.Admin")){
                return list;
            }
            if(args.length == 1){
                input = args[0];
                for(Player current : Bukkit.getOnlinePlayers()){
                    list.add(current.getName());
                }
            }
            if(args.length == 2){
                input = args[1];
                for(Rang r : Rang.getRangs()){
                    list.add(r.getDisplayname());
                }
            }
        }

        return TabComplete.sort(input, list);
    }

}
