package de.marcey.hsmp.commands;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.gui.KriegeGUI;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import de.marcey.hsmp.clansystem.kriegsystem.KriegProvider;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.TabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KriegCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        Clan clan = ClanProvider.getClan(p);
        if(clan == null){
            p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return false;
        }
        if(args.length == 0){
            KriegeGUI.openInv(p, ClanProvider.getClan(p));
        } else if(args.length >= 2){
            if(args[0].equalsIgnoreCase("invite")){
                String clanname = args[1];
                KriegProvider.inviteToKrieg(p, ClanProvider.getClanByClanname(clanname));
            } else if(args[0].equalsIgnoreCase("accept")){
                String clanname = args[1];
                KriegProvider.acceptKriegRequest(p, ClanProvider.getClanByClanname(clanname));
            } else if(args[0].equalsIgnoreCase("deny")){
                String clanname = args[1];
                KriegProvider.denyKriegRequest(p, ClanProvider.getClanByClanname(clanname));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(!(s instanceof Player)){
            return list;
        }
        Player p = (Player) s;
        String input = "";
        if(args.length == 1){
            input = args[0];
            list.add("invite");
            list.add("accept");
            list.add("deny");
        } else if(args.length == 2) {
            input = args[1];
            if(args[0].equalsIgnoreCase("invite")){
                Clan clan = ClanProvider.getClan(p);
                if(clan == null){
                    return list;
                }
                for(Clan c : HSMP.getClans().getAllClans()){
                    if (c.getUniqueId().equalsIgnoreCase(clan.getUniqueId())) continue;
                    if(KriegProvider.getKrieg(clan, c) != null) continue;
                    list.add(c.getClanname());
                }
            } else if(args[0].equalsIgnoreCase("accept")){
                Clan clan = ClanProvider.getClan(p);
                if(clan == null){
                    return list;
                }
                for(Clan c : HSMP.getClans().getAllClans()){
                    if (c.getUniqueId().equalsIgnoreCase(clan.getUniqueId())) continue;
                    if(KriegProvider.getKrieg(clan, c) != null) continue;
                    list.add(c.getClanname());
                }
            }
        }

        return TabComplete.sort(input, list);
    }

    private static void sendDefault(Player p){
        p.sendMessage(Strings.prefix + " §7Alle Commands:");
        p.sendMessage(Strings.prefix + " §e/krieg invite <Clanname> §7- Fordere einen Clan zum Krieg heraus");
        p.sendMessage(Strings.prefix + " §e/krieg accept <Clanname> §7- Akzeptiere eine Krieg-Anfrage");
        p.sendMessage(Strings.prefix + " §e/krieg deny <Clanname> §7- Lehne eine Krieg-Anfrage ab");
    }
}
