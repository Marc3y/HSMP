package de.marcey.hsmp.commands;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.ChunkProvider;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.gui.SettingsGUI;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.scoreboard.ScoreboardManager;
import de.marcey.hsmp.teleportsystem.TeleportProvider;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanCommand implements TabExecutor {

    private int maxClannameLength = 16;
    private int minClannameLength = 3;
    private int maxClanTagLength = 6;
    private int minClanTagLength = 3;
    public static HashMap<Player, ArrayList<Player>> invites = new HashMap<Player, ArrayList<Player>>();
    public static HashMap<Player, List<Integer>> taskids = new HashMap<Player, List<Integer>>();

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if (!(s instanceof Player)) {
            return false;
        }
        Player p = (Player) s;

        if (args.length == 0) {
            p.sendMessage(Strings.prefix + " §7Bitte nutze folgende Commands:");
            p.sendMessage("§e/clan create <Clan-Name> <Clan-Tag> §7- Erstellt einen neuen Clan");
            p.sendMessage("§7TODO: Alle Sachen hier einfügen");
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                if(Rang.getRang(p).getPriority() > 3){
                    p.sendMessage(Strings.prefix + " §cDu hast nicht die Berechtigung einen Clan zu erstellen. §7Du benötigst mindestens den §dStreamer§7-Rang.");
                    return false;
                }
                if (ClanProvider.getClan(p) != null) {
                    p.sendMessage(Strings.prefix + " §cDu besitzt bereits einen Clan.");
                    return false;
                }
                String clanname = args[1];
                String clantag = args[2];
                if (ChatColor.stripColor(clantag.replace('&', '§')).length() > maxClanTagLength || clantag.length() < minClanTagLength) {
                    p.sendMessage(Strings.prefix + " §cDein Clan-Tag muss zwischen " + minClanTagLength + "-" + maxClanTagLength + " Zeichen haben.");
                    return false;
                }
                if (clanname.length() > maxClannameLength || clanname.length() < minClannameLength) {
                    p.sendMessage(Strings.prefix + " §cDein Clan-Name muss zwischen " + minClannameLength + "-" + maxClannameLength + " Zeichen haben.");
                    return false;
                }
                if (ClanProvider.isClanTagExists(clantag)) {
                    p.sendMessage(Strings.prefix + " §cDieser Clan-Tag existiert bereits!");
                    return false;
                }
                if (ClanProvider.isClanNameExists(clanname)) {
                    p.sendMessage(Strings.prefix + " §cDieser Clan-Name existiert bereits.");
                    return false;
                }

                Clan clan = Clan.createNewClan(clanname, clantag, p.getUniqueId());
                ClanProvider.setClan(clan);
                ChunkProvider.init();

                p.sendMessage(Strings.prefix + " §7Dein Clan §e" + clan.getClanname() + " §f[§r" + clan.getDisplayname().replace('&', '§') + "§r§f] §7wurde erfolgreich §aerstellt§7.");
                ScoreboardManager.updateScoreboard();
            } else if(args[0].equalsIgnoreCase("joinrequests")){
                if(args[1].equalsIgnoreCase("accept")){
                    String name = args[2];
                    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                    if(target == null){
                        p.sendMessage(Strings.prefix + " §cDer Spieler war noch nie auf dem Server.");
                        return false;
                    }
                    Clan clan = ClanProvider.getClan(p);
                    if(clan == null){
                        p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                        return false;
                    }
                    ClanProvider.acceptJoinRequest(p, target.getUniqueId(), target.getName(), ClanProvider.getClan(p));
                } else if(args[1].equalsIgnoreCase("deny")){
                    String name = args[2];
                    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                    if(target == null){
                        p.sendMessage(Strings.prefix + " §cDer Spieler war noch nie auf dem Server.");
                        return false;
                    }
                    Clan clan = ClanProvider.getClan(p);
                    if(clan == null){
                        p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                        return false;
                    }
                    ClanProvider.denyJoinRequest(p, target.getUniqueId(), target.getName(), ClanProvider.getClan(p));
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("leave") && args[1].equalsIgnoreCase("confirm")) {
                ClanProvider.leaveClan(p, true);
                ScoreboardManager.updateScoreboard();
            }
            if(args[0].equalsIgnoreCase("invite")){
                String name = null;
                Clan clan = ClanProvider.getClan(p);
                if(clan == null){
                    p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                    return false;
                }
                UUID inviterUUID = null;
                Player invP = Bukkit.getPlayer(args[1]);
                if(invP != null){
                    inviterUUID = invP.getUniqueId();
                } else {
                    OfflinePlayer invO = Bukkit.getOfflinePlayer(args[1]);
                    inviterUUID = invO.getUniqueId();
                }
                if(inviterUUID == null){
                    p.sendMessage(Strings.prefix + " §cDieser Spieler war noch nie auf dem Server.");
                    return false;
                }
                Player target = Bukkit.getPlayer(inviterUUID);
                if(target != null) {
                    ClanProvider.invitePlayerToClan(clan, p, target);
                } else ClanProvider.invitePlayerToClan(clan, p, inviterUUID);
            }
            if(args[0].equalsIgnoreCase("accept")){
                Clan clan = ClanProvider.getClan(p);
                UUID inviterUUID = null;
                Player invP = Bukkit.getPlayer(args[1]);
                if(invP != null){
                    inviterUUID = invP.getUniqueId();
                } else {
                    OfflinePlayer invO = Bukkit.getOfflinePlayer(args[1]);
                    inviterUUID = invO.getUniqueId();
                }
                if(inviterUUID == null){
                    p.sendMessage(Strings.prefix + " §cDieser Spieler war noch nie auf dem Server.");
                    return false;
                }
                ClanProvider.acceptInvite(p, inviterUUID);
            }
            if(args[0].equalsIgnoreCase("deny")){
                Clan clan = ClanProvider.getClan(p);
                UUID inviterUUID = null;
                Player invP = Bukkit.getPlayer(args[1]);
                if(invP != null){
                    inviterUUID = invP.getUniqueId();
                } else {
                    OfflinePlayer invO = Bukkit.getOfflinePlayer(args[1]);
                    inviterUUID = invO.getUniqueId();
                }
                if(inviterUUID == null){
                    p.sendMessage(Strings.prefix + " §cDieser Spieler war noch nie auf dem Server.");
                    return false;
                }
                ClanProvider.denyInvite(p, inviterUUID);
            }
            if(args[0].equalsIgnoreCase("op")){
                Clan clan = ClanProvider.getClan(p);
                if(clan == null){
                    p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                    return false;
                }
                if(!clan.getHost().equals(p.getUniqueId())){
                    p.sendMessage(Strings.prefix + " §cDies kann nur der Clan-Host tun.");
                    return false;
                }
                String name = args[1];
                Player target = Bukkit.getPlayer(name);
                if(target != null){
                    if(!clan.getMitglieder().contains(target.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §cbefindet sich nicht in deinem Clan.");
                        return false;
                    }
                    if(clan.getOps().contains(target.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §cist bereits ein Clan-Operator.");
                        return false;
                    }
                    List<UUID> ops = new ArrayList<>(clan.getOps());
                    ops.addAll(Collections.singleton(target.getUniqueId()));
                    clan.setOps(ops);
                    ClanProvider.updateClan(clan);
                    for(Player current : Bukkit.getOnlinePlayers()){
                        if(clan.getMitglieder().contains(current.getUniqueId())){
                            current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §7als §cClan-Operator §ahinzugefügt§7.");
                        }
                    }
                } else {
                    OfflinePlayer otarget = Bukkit.getOfflinePlayer(name);
                    if(otarget == null){
                        p.sendMessage(Strings.prefix + " §cDieser Spieler war noch nie auf dem Server.");
                        return false;
                    }
                    if(!clan.getMitglieder().contains(otarget.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(otarget.getUniqueId()).getFormattedDisplayname() + otarget.getName() + " §cbefindet sich nicht in deinem Clan.");
                        return false;
                    }
                    if(clan.getOps().contains(otarget.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(otarget.getUniqueId()).getFormattedDisplayname() + otarget.getName() + " §cist bereits ein Clan-Operator.");
                        return false;
                    }
                    List<UUID> ops = new ArrayList<>(clan.getOps());
                    ops.addAll(Collections.singleton(otarget.getUniqueId()));
                    clan.setOps(ops);
                    ClanProvider.updateClan(clan);
                    for(Player current : Bukkit.getOnlinePlayers()){
                        if(clan.getMitglieder().contains(current.getUniqueId())){
                            current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat " + Rang.getRang(otarget.getUniqueId()).getFormattedDisplayname() + otarget.getName() + " §7als §cClan-Operator §ahinzugefügt§7.");
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("deop")){
                Clan clan = ClanProvider.getClan(p);
                if(clan == null){
                    p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                    return false;
                }
                if(!clan.getHost().equals(p.getUniqueId())){
                    p.sendMessage(Strings.prefix + " §cDies kann nur der Clan-Host tun.");
                    return false;
                }
                String name = args[1];
                Player target = Bukkit.getPlayer(name);
                if(target != null){
                    if(!clan.getMitglieder().contains(target.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §cbefindet sich nicht in deinem Clan.");
                        return false;
                    }
                    if(!clan.getOps().contains(target.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §cist kein Clan-Operator.");
                        return false;
                    }
                    if(target.getUniqueId().equals(p.getUniqueId())){
                        p.sendMessage(Strings.prefix + " §cDu kannst dich nicht selbst als Clan-Operator entfernen.");
                        return false;
                    }
                    List<UUID> ops = new ArrayList<>(clan.getOps());
                    ops.removeAll(Collections.singleton(target.getUniqueId()));
                    clan.setOps(ops);
                    ClanProvider.updateClan(clan);
                    for(Player current : Bukkit.getOnlinePlayers()){
                        if(clan.getMitglieder().contains(current.getUniqueId())){
                            current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §7als §cClan-Operator §centfernt§7.");
                        }
                    }
                } else {
                    OfflinePlayer otarget = Bukkit.getOfflinePlayer(name);
                    if(otarget == null){
                        p.sendMessage(Strings.prefix + " §cDieser Spieler war noch nie auf dem Server.");
                        return false;
                    }
                    if(!clan.getMitglieder().contains(otarget.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(otarget.getUniqueId()).getFormattedDisplayname() + otarget.getName() + " §cbefindet sich nicht in deinem Clan.");
                        return false;
                    }
                    if(clan.getOps().contains(otarget.getUniqueId())){
                        p.sendMessage(Strings.prefix + " " + Rang.getRang(otarget.getUniqueId()).getFormattedDisplayname() + otarget.getName() + " §cist kein Clan-Operator.");
                        return false;
                    }
                    if(otarget.getUniqueId().equals(p.getUniqueId())){
                        p.sendMessage(Strings.prefix + " §cDu kannst dich nicht selbst als Clan-Operator entfernen.");
                        return false;
                    }
                    List<UUID> ops = new ArrayList<>(clan.getOps());
                    ops.removeAll(Collections.singleton(otarget.getUniqueId()));
                    clan.setOps(ops);
                    ClanProvider.updateClan(clan);
                    for(Player current : Bukkit.getOnlinePlayers()){
                        if(clan.getMitglieder().contains(current.getUniqueId())){
                            current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat " + Rang.getRang(otarget.getUniqueId()).getFormattedDisplayname() + otarget.getName() + " §7als §cClan-Operator §centfernt§7.");
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("chunk")) {
                if (args[1].equalsIgnoreCase("claim")) {
                    Clan clan = ClanProvider.getClan(p);
                    if (clan == null) {
                        p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan!");
                        return false;
                    }
                    if (!clan.getHost().equals(p.getUniqueId())) {
                        p.sendMessage(Strings.prefix + " §cDies kann nur der Host des Clan's tun.");
                        return false;
                    }
                    ClanProvider.claimChunk(ClanProvider.getClan(p), p.getLocation(), p);
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    Clan clan = ClanProvider.getClan(p);
                    if (clan == null) {
                        p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan!");
                        return false;
                    }
                    if (!clan.getHost().equals(p.getUniqueId())) {
                        p.sendMessage(Strings.prefix + " §cDies kann nur der Host des Clan's tun.");
                        return false;
                    }
                    ClanProvider.removeChunk(ClanProvider.getClan(p), p.getLocation(), p);
                }
            }
            if(args[0].equalsIgnoreCase("kick")){
                UUID uuid = null;
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    uuid = target.getUniqueId();
                } else {
                    OfflinePlayer targetO = Bukkit.getOfflinePlayer(args[1]);
                    if(targetO != null){
                        uuid = targetO.getUniqueId();
                    }
                }
                if(uuid == null){
                    p.sendMessage(Strings.prefix + " §cDer Spieler war noch nie auf dem Server.");
                    return false;
                }
                if(uuid.equals(p.getUniqueId())){
                    p.sendMessage(Strings.prefix + " §cDu kannst dich nicht selber kicken.");
                    return false;
                }
                ClanProvider.kickPlayer(ClanProvider.getClan(p), p, uuid);
            }
            if(args[0].equalsIgnoreCase("kick")){
                UUID uuid = null;
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    uuid = target.getUniqueId();
                } else {
                    OfflinePlayer targetO = Bukkit.getOfflinePlayer(args[1]);
                    if(targetO != null){
                        uuid = targetO.getUniqueId();
                    }
                }
                if(uuid == null){
                    p.sendMessage(Strings.prefix + " §cDer Spieler war noch nie auf dem Server.");
                    return false;
                }
                ClanProvider.kickPlayer(ClanProvider.getClan(p), p, uuid);
            }
            if(args[0].equalsIgnoreCase("join")){
                String clanname = args[1];
                Clan clan = ClanProvider.getClanByClanname(clanname);
                if(clan == null){
                    p.sendMessage(Strings.prefix + " §cDer Clan existiert nicht.");
                    return false;
                }
                ClanProvider.joinrequest(p, clan);
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("leave")) {
                ClanProvider.leaveClan(p, false);
            }
            if (args[0].equalsIgnoreCase("settings")) {
                Clan clan = ClanProvider.getClan(p);
                if (clan == null) {
                    p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                    return false;
                }
                if (!clan.getOps().contains(p.getUniqueId())) {
                    p.sendMessage(Strings.prefix + " §cDies können nur Clan-Operatoren.");
                    return false;
                }
                SettingsGUI.openGUI(p);
            }
            if (args[0].equalsIgnoreCase("sethome")) {
                Clan clan = ClanProvider.getClan(p);
                if (clan == null) {
                    p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                    return false;
                }
                if (!clan.getOps().contains(p.getUniqueId())) {
                    p.sendMessage(Strings.prefix + " §cDies dürfen nur Clan-OP's machen.");
                    return false;
                }
                ClanProvider.setClanHome(p, ClanProvider.getClan(p), p.getLocation());
            }
            if (args[0].equalsIgnoreCase("home")) {
                Clan clan = ClanProvider.getClan(p);
                if (clan == null) {
                    p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
                    return false;
                }
                String home = clan.getStatFromName("home");
                if (home == null || home.isEmpty()) {
                    p.sendMessage(Strings.prefix + " §cDas Clan-Home wurde noch nicht gesetzt. Dies kann ein Clan-Operator mit §e/clan sethome §cmachen.");
                    return false;
                }
                if (TeleportProvider.isInTeleportVorgang(p)) {
                    p.sendMessage(Strings.prefix + " §cEs läuft derzeit schon ein Teleport-Vorgang.");
                    return false;
                }
                if (TeleportProvider.isOnCooldown(p)) {
                    p.sendMessage(Strings.prefix + " §cDu hast dich erst vor kurzem teleportiert. Bitte warte ein wenig, bevor du dich nochmal teleportierst.");
                    return false;
                }
                Location loc = clan.getClanHome();
                TeleportProvider.teleport(p, loc, 5, true, Strings.prefix + " §7Du wurdest erfolgreich zum Clan-Home §ateleportiert§7.", true, 60);
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

        String current = "";

        if(args.length == 1){
            current = args[0];
            list.add("create");
            list.add("leave");
            list.add("chunk");
            list.add("stats");
            list.add("sethome");
            list.add("home");
            list.add("invite");
            list.add("join");
            list.add("accept");
            list.add("deny");
            list.add("kick");
            list.add("settings");
            list.add("joinrequests");
            list.add("op");
            list.add("deop");
        } else if(args.length == 2){
            current = args[1];
            if(args[0].equalsIgnoreCase("chunk")){
                list.add("claim");
                list.add("remove");
            } else if(args[0].equalsIgnoreCase("create")){
                list.add("<Clan-Name>");
            } else if(args[0].equalsIgnoreCase("invite")){
                Clan clan = ClanProvider.getClan(p);
                if(clan != null) {
                    list.add("<Name>");
                    for (Player c : Bukkit.getOnlinePlayers()) {
                        if(!clan.getMitglieder().contains(c.getUniqueId())){
                            list.add(c.getName());
                        }
                    }
                }
            } else if(args[0].equalsIgnoreCase("join")){
                Clan clan = ClanProvider.getClan(p);
                if(clan != null) {
                    for (Clan c : HSMP.getClans().getAllClans()) {
                        if(!clan.getUniqueId().equalsIgnoreCase(c.getUniqueId())){
                            list.add(c.getClanname());
                        }
                    }
                } else {
                    for (Clan c : HSMP.getClans().getAllClans()) {
                        list.add(c.getClanname());
                    }
                }
            } else if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny")){
                list.add("<Name>");
                for (Player c : Bukkit.getOnlinePlayers()) {
                    list.add(c.getName());
                }
            } else if(args[0].equalsIgnoreCase("kick")){
                Clan clan = ClanProvider.getClan(p);
                list.add("<Name>");
                if(clan != null) {
                    for (Player c : Bukkit.getOnlinePlayers()) {
                        if (clan.getMitglieder().contains(c.getUniqueId())) {
                            list.add(c.getName());
                        }
                    }
                }
            } else if(args[0].equalsIgnoreCase("op")){
                Clan clan = ClanProvider.getClan(p);
                list.add("<Name>");
                if(clan != null) {
                    for (Player c : Bukkit.getOnlinePlayers()) {
                        if (clan.getMitglieder().contains(c.getUniqueId()) && !clan.getOps().contains(c.getUniqueId())) {
                            list.add(c.getName());
                        }
                    }
                }
            } else if(args[0].equalsIgnoreCase("deop")){
                Clan clan = ClanProvider.getClan(p);
                list.add("<Name>");
                if(clan != null) {
                    for (Player c : Bukkit.getOnlinePlayers()) {
                        if (clan.getMitglieder().contains(c.getUniqueId()) && clan.getOps().contains(c.getUniqueId())) {
                            list.add(c.getName());
                        }
                    }
                }
            } else if(args[0].equalsIgnoreCase("joinrequests")){
                list.add("accept");
                list.add("deny");
            }
        } else if(args.length == 3){
            if(args[0].equalsIgnoreCase("create")){
                list.add("<Clan-Tag>");
            } else if(args[0].equalsIgnoreCase("joinrequests")){
                if(args[1].equalsIgnoreCase("accept") || args[1].equalsIgnoreCase("deny")){
                    Clan clan = ClanProvider.getClan(p);
                    if(clan != null){
                        for(UUID u : ClanProvider.joinrequests.get(clan.getUniqueId())){
                            OfflinePlayer of = Bukkit.getOfflinePlayer(u);
                            if(of == null){
                                continue;
                            }
                            list.add(of.getName());
                        }
                    }
                }
            }
        }

        return TabComplete.sort(current, list);
    }
}
