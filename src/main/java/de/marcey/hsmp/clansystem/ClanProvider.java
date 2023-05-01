package de.marcey.hsmp.clansystem;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.listener.ChunkSpawnListener;
import de.marcey.hsmp.listener.ChunkListener;
import de.marcey.hsmp.listener.ClanListener;
import de.marcey.hsmp.listener.SpawnListener;
import de.marcey.hsmp.objects.PlayerStat;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.PlayerStats;
import de.marcey.hsmp.scoreboard.ScoreboardManager;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.data.clans.ClanManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ClanProvider {

    private static ClanManager clans = HSMP.getClans();
    public static HashMap<UUID, String> ids = new HashMap<>();
    public static HashMap<String, Clan> cs = new HashMap<>();
    public static Clan getClan(Player p){
        if(ids.containsKey(p.getUniqueId())){
            Clan clan = cs.get(ids.get(p.getUniqueId()));
            if(clan != null) {
                if (clan.getMitglieder().contains(p.getUniqueId())) {
                    return cs.get(ids.get(p.getUniqueId()));
                } else {
                    ids.remove(p.getUniqueId());
                }
            } else {
                cs.remove(ids.get(p.getUniqueId()));
            }
        }
        return null;
    }

    public static Clan getClan(UUID uuid){
        if(ids.containsKey(uuid)){
            Clan clan = cs.get(ids.get(uuid));
            if(clan != null) {
                if (clan.getMitglieder().contains(uuid)) {
                    return cs.get(ids.get(uuid));
                } else {
                    ids.remove(uuid);
                }
            } else {
                cs.remove(ids.get(uuid));
            }
        }
        return null;
    }

    public static Clan getClanByClanname(String clanname){
        for(Clan c : clans.getAllClans()){
            if(c.getClanname().equalsIgnoreCase(clanname)){
                return c;
            }
        }
        return null;
    }

    public static Clan getClanById(String uniqueId){
        return cs.get(uniqueId);
    }

    public static boolean isClanExists(String id){
        return cs.containsKey(id);
    }

    public static void deleteClan(Clan clan){
        clans.deleteClan(clan.getUniqueId());
    }

    public static Clan setClan(Clan clan){
        clans.setClan(clan);
        return clan;
    }

    public static Clan updateClan(Clan clan){
        clans.setClan(clan);
        return clan;
    }

    public static boolean isClanNameExists(String clanname){
        return clans.isClannameExists(clanname);
    }
    public static boolean isClanTagExists(String clantag){
        return clans.isClanDisplaynameExists(clantag);
    }



    public static boolean leaveClan(Player p, boolean withconfirm){
        Clan clan = getClan(p);
        if(clan == null){
            p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return false;
        }
        if(clan.getHost().equals(p.getUniqueId()) && withconfirm){
            if(ChunkProvider.isInOwnChunk(clan, p)){
                p.sendMessage(Strings.prefix + " §cDu kannst derzeit deinen Clan nicht verlassen, da du dich in einem Clan-Chunk befindest.");
                return false;
            }
            List<UUID> list = new ArrayList<>(clan.getMitglieder());
            list.removeAll(Collections.singleton(p.getUniqueId()));
            clan.setMitglieder(list);
            for(UUID u : clan.getMitglieder()){
                Player target = Bukkit.getPlayer(u);
                if(target == null) continue;
                target.sendMessage(Strings.prefix + " §cDer Clan indem du drin warst, wurde vom Host gelöscht.");
            }
            clans.deleteClan(clan.getUniqueId());
            p.sendMessage(Strings.prefix + " §7Du hast den Clan §cverlassen§7. Da du der Host warst, wurde der Clan §cgelöscht§7.");
            ClanProvider.ids.remove(p.getUniqueId());
            ClanListener.reloadPlayer(p);
            ChunkProvider.init();
        } else if(clan.getHost().equals(p.getUniqueId())){
            p.sendMessage(Strings.prefix + " §cDu bist der Host dieses Clan's. Wenn du den Clan verlässt, wird er gelöscht! Willst du das wirklich? Nutze §e/clan leave confirm §cwenn du den Clan trotzdem verlassen willst.");
            return false;
        } else {
            if(ChunkProvider.isInOwnChunk(clan, p)){
                p.sendMessage(Strings.prefix + " §cDu kannst derzeit deinen Clan nicht verlassen, da du dich in einem Clan-Chunk befindest.");
                return false;
            }

            List<UUID> list = new ArrayList<>(clan.getMitglieder());
            list.removeAll(Collections.singleton(p.getUniqueId()));
            clan.setMitglieder(list);

            List<UUID> ops = new ArrayList<>(clan.getOps());
            list.removeAll(Collections.singleton(p.getUniqueId()));
            clan.setOps(list);

            setClan(clan);
            p.sendMessage(Strings.prefix + " §7Du hast den Clan §e" + clan.getClanname() + " §cverlassen§7.");
            ClanProvider.ids.remove(p.getUniqueId());
            for(UUID u : clan.getMitglieder()){
                Player target = Bukkit.getPlayer(u);
                if(target == null) continue;
                if(target.getUniqueId().equals(p.getUniqueId())) continue;
                target.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat den Clan §cverlassen§7.");
            }
            ClanListener.reloadPlayer(p);
        }
        return true;
    }

    public static void claimChunk(Clan clan, Location loc, Player p){
        int maxchunks = clan.getChunks();
        int currentchunks = clan.getChunkIdsAsList().size();
        if(currentchunks >= maxchunks){
            if(p != null){
                p.sendMessage(Strings.prefix + " §cDu hast das Maximum an Chunks erreicht! Nutze §e/clan chunk remove §cum einen Chunk zu entfernen.");
            }
            return;
        }
        if(!loc.getWorld().getName().equalsIgnoreCase("world")){
            p.sendMessage(Strings.prefix + " §cDu darfst in dieser Dimension keine Chunks claimen.");
            return;
        }
        if(!isDistanceToGegnerChunks(clan, loc)){
            p.sendMessage(Strings.prefix + " §cDu befindest dich zu nah an einem Gegner-Chunk. Bitte überprüfe, dass du mindestens 5 Chunks Abstand zwischen Gegner-Chunks hast.");
            return;
        }

        int x = Integer.parseInt(ChunkProvider.getChunkId(loc).split(",")[0]);
        int z = Integer.parseInt(ChunkProvider.getChunkId(loc).split(",")[2]);
        boolean save = true;
        for(Entity inchunk : Bukkit.getWorld("world").getChunkAt(x, z).getEntities()){
            if(!(inchunk instanceof Player)){
                continue;
            }
            Player inchunkp = ((Player) inchunk).getPlayer();
            if(!clan.getMitglieder().contains(inchunkp.getUniqueId())){
                save = false;
            }
        }

        if(!save){
            p.sendMessage(Strings.prefix + " §cDu kannst diesen Chunk derzeit nicht claimen, da sich gegnerische Spieler in diesem Chunk befinden.");
            return;
        }
        if(SpawnListener.isInSpawn(loc)){
            p.sendMessage(Strings.prefix + " §cDu befindest dich zu nah an dem Spawn.");
            return;
        }
        List<String> chunks = clan.getChunkIdsAsList();
        String id = ChunkProvider.getChunkId(loc);
        if(chunks.contains(id)){
            p.sendMessage(Strings.prefix + " §cDieser Chunk gehört bereits deinem Clan.");
            return;
        }
        chunks.add(id);
        clan.setChunkIdsFromList(chunks);
        ClanProvider.setClan(clan);
        p.sendMessage(Strings.prefix + " §7Du hast erfolgreich den Chunk §ageclaimed§7. §8(" + x + "/" + z + ")");
        ChunkListener.spawnParticlesWholeChunk(p, loc, Color.GREEN, 7);
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId()) && !current.getUniqueId().equals(p.getUniqueId())){
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat einen Chunk §ageclaimed§7. §8(" + x + "/" + z + ")");
            }
        }
        ChunkProvider.init();
    }

    public static void removeChunk(Clan clan, Location loc, Player p){
        String id = ChunkProvider.getChunkId(loc);
        int x = Integer.parseInt(id.split(",")[0]);
        int z = Integer.parseInt(id.split(",")[2]);

        if(!clan.getChunkIdsAsList().contains(id)){
            p.sendMessage(Strings.prefix + " §cDieser Chunk ist nicht geclaimed.");
            return;
        }
        List<String> chunks = clan.getChunkIdsAsList();
        chunks.remove(id);
        clan.setChunkIdsFromList(chunks);
        boolean isHomeRemoved = false;
        if(clan.getStatFromName("home") != null && !clan.getStatFromName("home").isEmpty()) {
            if (clan.getClanHome() != null) {
                if (ChunkProvider.getChunkId(clan.getClanHome()).equalsIgnoreCase(id)) {
                    clan.removeClanHome();
                    isHomeRemoved = true;
                }
            }
        }
        ClanProvider.updateClan(clan);
        p.sendMessage(Strings.prefix + " §7Du hast erfolgreich den Chunk §centfernt§7. §8(" + x + "/" + z + ")");
        if(isHomeRemoved) {
            p.sendMessage(Strings.prefix + " §7Dadurch wurde das Clan-Home ebenfalls §centfernt§7.");
        }
        ChunkListener.spawnParticlesWholeChunk(p, loc, Color.RED, 7);
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId()) && !current.getUniqueId().equals(p.getUniqueId())){
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(p).getFormattedDisplayname() + p.getName() + " §7hat einen Chunk §centfernt§7. §8(" + x + "/" + z + ")");
                if(isHomeRemoved) {
                    current.sendMessage(Strings.prefix + " §7Dadurch wurde das Clan-Home ebenfalls §centfernt§7.");
                }
            }
        }
        ChunkProvider.init();
    }

    private static boolean isDistanceToGegnerChunks(Clan clan, Location loc){
        int x = Integer.parseInt(ChunkProvider.getChunkId(loc).split(",")[0]);
        int z = Integer.parseInt(ChunkProvider.getChunkId(loc).split(",")[2]);
        Location chunk1 = getChunkCenterLocation(Bukkit.getWorld("world"), x, z);
        for(String ids : ChunkProvider.gegnerchunks.get(clan.getUniqueId())){
            int x2 = Integer.parseInt(ids.split(",")[0]);
            int z2 = Integer.parseInt(ids.split(",")[2]);
            Location chunk2 = getChunkCenterLocation(Bukkit.getWorld("world"), x2, z2);
            if(chunk1.getWorld().getName().equalsIgnoreCase(chunk2.getWorld().getName())){
                if(chunk1.distance(chunk2) < 80){
                    return false;
                }
            }
        }
        return true;
    }

    private static Location getChunkCenterLocation(World world, int chunkX, int chunkZ) {
        int blockX = chunkX << 4;
        int blockZ = chunkZ << 4;
        int blockY = world.getHighestBlockYAt(blockX, blockZ);
        return new Location(world, blockX + 8, 0, blockZ + 8);
    }

    public static void setClanHome(Player p, Clan clan, Location loc){
        if(!loc.getWorld().getName().equalsIgnoreCase("world")){
            p.sendMessage(Strings.prefix + " §cClan-Homes können nur in der Overworld gesetzt werden.");
            return;
        }
        if(!ChunkProvider.isInOwnChunk(clan, p)){
            p.sendMessage(Strings.prefix + " §cDu kannst den Clan-Home nur in Clan-Chunks setzen. Clan-Chunks können mit §e/clan chunk claim §cgesetzt werden.");
            return;
        }
        clan.setClanHome(loc);
        updateClan(clan);
        Rang rang = Rang.getRang(p);
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendMessage(Strings.prefix + " §7Der Spieler " + rang.getFormattedDisplayname() + p.getName() + " §7hat den Clan-Home §agesetzt§7. §8(" + (int) loc.getX() + ", " + (int) loc.getY() + ", " + (int) loc.getZ() + ")");
                current.sendMessage(Strings.prefix + " §7Du kannst dich nun mit §e/clan home §7zum Clan-Home teleportieren.");
            }
        }
    }

    public static HashMap<String, ArrayList<UUID>> joinrequests = new HashMap<String, ArrayList<UUID>>();

    private static boolean hasAlreadyRequested(UUID requester, Clan to){
        if(joinrequests.containsKey(to.getUniqueId())){
            return joinrequests.get(to.getUniqueId()).contains(requester);
        }
        return false;
    }
    private static void removeJoinRequest(UUID requester, Clan to){
        joinrequests.get(to.getUniqueId()).remove(requester);
    }

    private static void addJoinRequest(UUID requester, Clan clan){
        if(joinrequests.containsKey(clan.getUniqueId())){
            joinrequests.get(clan.getUniqueId()).add(requester);
        } else {
            ArrayList<UUID> request = new ArrayList<UUID>();
            request.add(requester);
            joinrequests.put(clan.getUniqueId(), request);
        }
    }

    public static void joinrequest(Player requester, Clan clan){
        Clan ownClan = ClanProvider.getClan(requester);
        if(ownClan != null){
            requester.sendMessage(Strings.prefix + " §cDu bist bereits in einem Clan.");
            return;
        }
        if(clan == null){
            requester.sendMessage(Strings.prefix + " §cDer Clan existiert nicht.");
            return;
        }
        if(hasAlreadyRequested(requester.getUniqueId(), clan)){
            requester.sendMessage(Strings.prefix + " §cDu hast dem Clan bereits eine Anfrage geschickt.");
            return;
        }
        if(clan.getMitglieder().contains(requester.getUniqueId())){
            requester.sendMessage(Strings.prefix + " §cDu befindest dich bereits in diesem Clan.");
            return;
        }
        if(!clan.isJoinRequestsEnabled()){
            requester.sendMessage(Strings.prefix + " §cDer Clan hat Join-Requests deaktiviert. Um den Clan beitreten zu können musst du eingeladen werden.");
            return;
        }
        if(joinrequests.containsKey(clan.getUniqueId())){
            joinrequests.get(clan.getUniqueId()).add(requester.getUniqueId());
        } else {
            ArrayList<UUID> request = new ArrayList<UUID>();
            request.add(requester.getUniqueId());
            joinrequests.put(clan.getUniqueId(), request);
        }
        requester.sendMessage(Strings.prefix + " §7Du hast eine Join-Request an den Clan §e" + clan.getClanname() + " §agesendet§7.");
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(requester).getFormattedDisplayname() + requester.getName() + " §7will den Clan beitreten. Clan-OP's können dies mit §e/clan joinrequests accept " + requester.getName() + " §7akzeptieren.");
            }
        }
        joinrequetsCooldown(requester.getUniqueId(),clan, requester.getName());
    }

    public static void acceptJoinRequest(Player accepted, UUID requester, String requesterName, Clan clan){
        if(clan == null){
            accepted.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(!hasAlreadyRequested(requester, clan)){
            accepted.sendMessage(Strings.prefix + " §cDu hast keine Join-Requests von diesem Spieler..");
            return;
        }
        Clan oldClan = ClanProvider.getClan(requester);
        if(oldClan != null){
            accepted.sendMessage(Strings.prefix + " §cDer Spieler ist bereits einem anderen Clan beigetreten.");
            return;
        }
        if(clan.getMitglieder().contains(requester)){
            accepted.sendMessage(Strings.prefix + " §cDer Spieler ist dem Clan bereits beigetreten.");
            return;
        }
        if(!clan.getOps().contains(accepted.getUniqueId())){
            accepted.sendMessage(Strings.prefix + " §cDies können nur Clan-OP's.");
            return;
        }
        removeJoinRequest(requester, clan);

        List<UUID> list = new ArrayList<>(clan.getMitglieder());
        list.addAll(Collections.singleton(requester));
        clan.setMitglieder(list);

        boolean isNewChunk = false;
        if(clan.getMitglieder().size() > clan.getChunks()){
            clan.setChunks(clan.getMitglieder().size());
            isNewChunk = true;
        }
        ClanProvider.updateClan(clan);
        accepted.sendMessage(Strings.prefix + " §7Du hast die Join-Request §aangenommen§7.");
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(accepted).getFormattedDisplayname() + accepted.getName() + " §7hat die Join-Request von " + Rang.getRang(requester).getFormattedDisplayname() + requesterName + " §aangenommen§7.");
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(requester).getFormattedDisplayname() + requesterName + " §7ist dem Clan §abeigetreten§7.");
                if(isNewChunk) {
                    current.sendMessage(Strings.prefix + " §7Der Clan hat ein neuen Chunk §eerhalten§7.");
                }
            }
        }
        ClanListener.reloadPlayer(requester);
        ChunkProvider.init();
        ScoreboardManager.updateScoreboard();
        PlayerStats stats = PlayerStats.getPlayerStats(requester);
        stats.addToStat(PlayerStat.CLANS_JOINED, 1);
    }

    public static void denyJoinRequest(Player denied, UUID requester, String requesterName, Clan clan){
        if(clan == null){
            denied.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(!hasAlreadyRequested(requester, clan)){
            denied.sendMessage(Strings.prefix + " §cDu hast keine Join-Request von diesem Spieler.");
            return;
        }
        if(!clan.getOps().contains(denied.getUniqueId())){
            denied.sendMessage(Strings.prefix + " §cDies können nur Clan-OP's.");
            return;
        }
        removeJoinRequest(requester, clan);
        Player target = Bukkit.getPlayer(requester);
        if(target != null) {
            target.sendMessage(Strings.prefix + " §7Deine Join-Request an den Clan §e" + clan.getClanname() + " §7wurde §cabgelehnt§7.");
        }
        for (Player current : Bukkit.getOnlinePlayers()) {
            if (clan.getMitglieder().contains(current.getUniqueId())) {
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(denied).getFormattedDisplayname() + denied.getName() + " §7hat die Join-Request von " + Rang.getRang(requester).getFormattedDisplayname() + requesterName + " §cabgelehnt§7.");
            }
        }
    }

    private static void joinrequetsCooldown(UUID requester, Clan clan, String requesterName){
        new BukkitRunnable() {
            int seconds = 300;
            @Override
            public void run() {

                if(hasAlreadyRequested(requester, clan)){
                    if(seconds == 0){
                        removeJoinRequest(requester, clan);
                        Player inv = Bukkit.getPlayer(requesterName);
                        if(inv != null){
                            inv.sendMessage(Strings.prefix + " §7Deine Join-Request, die du an den Clan §e" + clan.getClanname() + " §7gesendet hast, ist §cabgelaufen§7.");
                        }
                        for(Player current : Bukkit.getOnlinePlayers()){
                            if(clan.getMitglieder().contains(current.getUniqueId())){
                                current.sendMessage(Strings.prefix + " §7Die Join-Request von " + Rang.getRang(requester).getFormattedDisplayname() + requesterName + " §7ist §cabgelaufen§7.");
                            }
                        }
                        this.cancel();
                    }
                    seconds--;
                } else this.cancel();
            }
        }.runTaskTimer(HSMP.getInstance(), 0, 20);
    }


    public static HashMap<UUID, ArrayList<UUID>> anfrage = new HashMap<UUID, ArrayList<UUID>>();

    private static boolean hasAlreadyInvited(UUID inviter, UUID to){
        if(anfrage.containsKey(to)){
            return anfrage.get(to).contains(inviter);
        }
        return false;
    }
    private static void removeInvite(UUID inviter, UUID to){
        anfrage.get(to).remove(inviter);
    }
    public static void invitePlayerToClan(Clan clan, Player inviter, Player to){

        if(clan == null){
            inviter.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(!clan.getOps().contains(inviter.getUniqueId()) && !clan.isNormalMembersAllowedToInvitePlayers()){
            inviter.sendMessage(Strings.prefix + " §cDu hast keine Rechte um Spieler in den Clan einzuladen.");
            return;
        }
        Clan clan2 = ClanProvider.getClan(to);
        if(clan2 != null){
            if(clan2.getUniqueId().equalsIgnoreCase(clan.getUniqueId())){
                inviter.sendMessage(Strings.prefix + " §cDer Spieler ist bereits in deinem Clan.");
            } else {
                inviter.sendMessage(Strings.prefix + " §cDer Spieler muss seinen jetzigen Clan verlassen, damit er eingeladen werden kann.");
            }
            return;
        }
        if(hasAlreadyInvited(inviter.getUniqueId(), to.getUniqueId())){
            inviter.sendMessage(Strings.prefix + " §cDu hast dem Spieler bereits eine Anfrage geschickt.");
            return;
        }
        if(anfrage.containsKey(to.getUniqueId())){
            anfrage.get(to.getUniqueId()).add(inviter.getUniqueId());
        } else {
            ArrayList<UUID> request = new ArrayList<UUID>();
            request.add(inviter.getUniqueId());
            anfrage.put(to.getUniqueId(), request);
        }
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(inviter).getFormattedDisplayname() + inviter.getName() + " §7hat " + Rang.getRang(to).getFormattedDisplayname() + to.getName() + " §7zum Clan §eeingeladen§7.");
            }
        }
        to.sendMessage(Strings.prefix + " §7Du hast eine Clan-Einladung von " + Rang.getRang(inviter).getFormattedDisplayname() + inviter.getName() + " §7erhalten.");
        TextComponent message = new TextComponent();
        TextComponent accept = new TextComponent("§f[§eAkzeptieren§f]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " + inviter.getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Klicke hier").create()));
        TextComponent deny = new TextComponent("§f[§cAblehnen§f]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan deny " + inviter.getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Klicke hier").create()));
        message.addExtra("§8>> §r");
        message.addExtra(accept);
        message.addExtra(" ");
        message.addExtra(deny);
        to.spigot().sendMessage(message);
        cooldown(inviter.getUniqueId(), to.getUniqueId(), inviter.getName(), to.getName());
    }

    public static void invitePlayerToClan(Clan clan, Player inviter, UUID to){

        if(clan == null){
            inviter.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(!clan.getOps().contains(inviter.getUniqueId()) && !clan.isNormalMembersAllowedToInvitePlayers()){
            inviter.sendMessage(Strings.prefix + " §cDu hast keine Rechte um Spieler in den Clan einzuladen.");
            return;
        }
        Clan clan2 = ClanProvider.getClan(to);
        if(clan2 != null){
            if(clan2.getUniqueId().equalsIgnoreCase(clan.getUniqueId())){
                inviter.sendMessage(Strings.prefix + " §cDer Spieler ist bereits in deinem Clan.");
            } else {
                inviter.sendMessage(Strings.prefix + " §cDer Spieler muss seinen jetzigen Clan verlassen, damit er eingeladen werden kann.");
            }
            return;
        }
        if(hasAlreadyInvited(inviter.getUniqueId(), to)){
            inviter.sendMessage(Strings.prefix + " §cDu hast dem Spieler bereits eine Anfrage geschickt.");
            return;
        }
        if(anfrage.containsKey(to)){
            anfrage.get(to).add(inviter.getUniqueId());
        } else {
            ArrayList<UUID> request = new ArrayList<UUID>();
            request.add(inviter.getUniqueId());
            anfrage.put(to, request);
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(to);
        String targetName = null;
        if(target != null) {
            targetName = target.getName();
            for (Player current : Bukkit.getOnlinePlayers()) {
                if (clan.getMitglieder().contains(current.getUniqueId())) {
                    current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(inviter).getFormattedDisplayname() + inviter.getName() + " §7hat " + Rang.getRang(to).getFormattedDisplayname() + target.getName() + " §7zum Clan §eeingeladen§7.");
                }
            }
            cooldown(inviter.getUniqueId(), to, inviter.getName(), targetName);
        }
    }

    public static void kickPlayer(Clan clan, Player p, UUID toKick){
        if(clan == null){
            p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(!clan.getHost().equals(p.getUniqueId())){
            p.sendMessage(Strings.prefix + " §cDies kann nur der Clan-Leader machen.");
            return;
        }
        if(!clan.getMitglieder().contains(toKick)){
            p.sendMessage(Strings.prefix + " §cDer Spieler ist nicht in deinem Clan.");
            return;
        }
        Player target = Bukkit.getPlayer(toKick);
        if(target != null) {
            if (ChunkProvider.isInOwnChunk(clan, target)){
                target.teleport(HSMP.getSpawn());
                target.sendMessage(Strings.prefix + " §7Du wurdest aus dem Clan §cgekickt§7. Da du dich in einem Clan-Chunk befunden hast, wurdest du zum Spawn teleportiert.");
            } else target.sendMessage(Strings.prefix + " §7Du wurdest aus dem Clan §cgekickt§7.");
        } else {
            OfflinePlayer targetO = Bukkit.getOfflinePlayer(toKick);
            if(targetO != null){
                if (ChunkProvider.isInOwnChunk(clan, ChunkSpawnListener.getLastLocation(targetO.getUniqueId()))){
                    ChunkSpawnListener.setNextJoinToSpawn(toKick);
                }
            }
        }

        List<UUID> list = new ArrayList<>(clan.getMitglieder());
        list.removeAll(Collections.singleton(toKick));
        clan.setMitglieder(list);

        List<UUID> ops = new ArrayList<>(clan.getOps());
        ops.removeAll(Collections.singleton(toKick));
        clan.setOps(ops);

        ClanProvider.updateClan(clan);

        if(target != null){
            for(Player current : Bukkit.getOnlinePlayers()){
                if(clan.getMitglieder().contains(current.getUniqueId())){
                    current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(target).getFormattedDisplayname() + target.getName() + " §7wurde aus dem Clan §cgekickt§7.");
                }
            }
            ClanListener.reloadPlayer(target);
            ChunkProvider.init();
            ScoreboardManager.updateScoreboard();
        } else {
            OfflinePlayer targetO = Bukkit.getOfflinePlayer(toKick);
            if(targetO != null) {
                for (Player current : Bukkit.getOnlinePlayers()) {
                    if (clan.getMitglieder().contains(current.getUniqueId())) {
                        current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(targetO.getUniqueId()).getFormattedDisplayname() + targetO.getName() + " §7wurde aus dem Clan §cgekickt§7.");
                    }
                }
            }
        }
    }

    public static void acceptInvite(Player accepted, UUID inviter){

        if(!hasAlreadyInvited(inviter, accepted.getUniqueId())){
            accepted.sendMessage(Strings.prefix + " §cDu hast keine Einladung von diesem Spieler.");
            return;
        }

        Clan toJoin = ClanProvider.getClan(inviter);
        Clan oldClan = ClanProvider.getClan(accepted);
        if(oldClan != null){
            if(!oldClan.getUniqueId().equalsIgnoreCase(toJoin.getUniqueId())){
                accepted.sendMessage(Strings.prefix + " §cDu bist bereits in einem Clan. Um die Einladung anzunehmen, musst du dein jetzigen Clan mit §e/clan leave §cverlassen.");
                return;
            }
        }
        if(toJoin == null){
            accepted.sendMessage(Strings.prefix + " §cDer Clan, den du beitreten wolltest, existiert nicht mehr oder der Spieler hat den Clan verlassen.");
            return;
        }
        if(toJoin.getMitglieder().contains(accepted.getUniqueId())){
            accepted.sendMessage(Strings.prefix + " §cDu bist dem Clan bereits beigetreten.");
            return;
        }
        removeInvite(inviter, accepted.getUniqueId());

        List<UUID> list = new ArrayList<>(toJoin.getMitglieder());
        list.addAll(Collections.singleton(accepted.getUniqueId()));
        toJoin.setMitglieder(list);

        boolean isNewChunk = false;
        if(toJoin.getMitglieder().size() > toJoin.getChunks()){
            toJoin.setChunks(toJoin.getMitglieder().size());
            isNewChunk = true;
        }
        ClanProvider.updateClan(toJoin);
        accepted.sendMessage(Strings.prefix + " §7Du hast die Clan-Einladung §aangenommen§7.");
        accepted.sendMessage(Strings.prefix + " §7Du bist dem Clan §e" + toJoin.getClanname() + " §abeigetreten§7.");
        for(Player current : Bukkit.getOnlinePlayers()){
            if(toJoin.getMitglieder().contains(current.getUniqueId())){
                if(!current.getUniqueId().equals(accepted.getUniqueId())){
                    current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(accepted).getFormattedDisplayname() + accepted.getName() + " §7ist dem Clan §abeigetreten§7.");
                    if(isNewChunk) {
                        current.sendMessage(Strings.prefix + " §7Der Clan hat ein neuen Chunk §eerhalten§7.");
                    }
                }
            }
        }
        ClanListener.reloadPlayer(accepted);
        ChunkProvider.init();
        ScoreboardManager.updateScoreboard();
        PlayerStats stats = PlayerStats.getPlayerStats(accepted.getUniqueId());
        stats.addToStat(PlayerStat.CLANS_JOINED, 1);
    }

    public static void denyInvite(Player denied, UUID inviter){
        if(!hasAlreadyInvited(inviter, denied.getUniqueId())){
            denied.sendMessage(Strings.prefix + " §cDu hast keine Einladung von diesem Spieler.");
            return;
        }

        Clan toJoin = ClanProvider.getClan(inviter);
        if(toJoin == null){
            denied.sendMessage(Strings.prefix + " §cDer Clan existiert nicht mehr oder der Spieler hat den Clan verlassen.");
            removeInvite(inviter, denied.getUniqueId());
            return;
        }
        if(toJoin.getMitglieder().contains(denied.getUniqueId())){
            denied.sendMessage(Strings.prefix + " §cDu bist dem Clan bereits beigetreten.");
            removeInvite(inviter, denied.getUniqueId());
            return;
        }
        removeInvite(inviter, denied.getUniqueId());
        denied.sendMessage(Strings.prefix + " §7Du hast die Clan-Einladung §cabgelehnt§7.");
        OfflinePlayer target = Bukkit.getOfflinePlayer(inviter);
        if(target != null) {
            for (Player current : Bukkit.getOnlinePlayers()) {
                if (toJoin.getMitglieder().contains(current.getUniqueId())) {
                    if (!current.getUniqueId().equals(denied.getUniqueId())) {
                        current.sendMessage(Strings.prefix + " §7Der Spieler " + Rang.getRang(denied).getFormattedDisplayname() + denied.getName() + " §7hat die Clan-Einladung von " + Rang.getRang(target.getUniqueId()).getFormattedDisplayname() + target.getName() + " §cabgelehnt§7.");
                    }
                }
            }
        }
    }

    private static void cooldown(UUID inviter, UUID to, String inviterName, String toName){
        new BukkitRunnable() {
            int seconds = 300;
            @Override
            public void run() {

                if(hasAlreadyInvited(inviter, to)){
                    if(seconds == 0){
                        removeInvite(inviter, to);
                        Player inv = Bukkit.getPlayer(inviter);
                        if(inv != null){
                            inv.sendMessage(Strings.prefix + " §7Deine Einladung, die du an " + Rang.getRang(to).getFormattedDisplayname() + toName + " §7gesendet hast, ist §cabgelaufen§7.");
                        }
                        Player toP = Bukkit.getPlayer(to);
                        if(toP != null){
                            toP.sendMessage(Strings.prefix + " §7Die Einladung von " + Rang.getRang(inviter).getFormattedDisplayname() + inviterName + " §7ist §cabgelaufen§7.");
                        }
                        this.cancel();
                    }
                    seconds--;
                } else this.cancel();
            }
        }.runTaskTimer(HSMP.getInstance(), 0, 20);
    }

}
