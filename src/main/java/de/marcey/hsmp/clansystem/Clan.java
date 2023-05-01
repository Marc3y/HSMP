package de.marcey.hsmp.clansystem;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Clan {

    private String clanname;
    private String displayname;
    private List<UUID> mitglieder = new ArrayList<>();
    private int chunks;
    private UUID host;
    private List<UUID> ops = new ArrayList<>();
    private String uniqueId;
    private String stats;
    private String chunkIds;

    public static Clan createNewClan(String clanname, String displayname, UUID host){
        Clan clan = new Clan();
        clan.setChunks(1);
        clan.setClanname(clanname);
        clan.setDisplayname(displayname);
        clan.setHost(host);
        clan.setMitglieder(Arrays.asList(host));
        clan.setOps(Arrays.asList(host));
        clan.setUniqueId(UUID.randomUUID().toString());
        clan.setChunkIdsFromList(new ArrayList<>());
        ClanProvider.cs.put(clan.getUniqueId(), clan);
        ClanProvider.ids.put(host, clan.getUniqueId());
        return clan;
    }

    public String getClanname() {
        return clanname;
    }

    public void setClanname(String clanname) {
        this.clanname = clanname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public List<UUID> getMitglieder() {
        return mitglieder;
    }

    public String getMitgliederAsString() {
        String mitglieder = null;
        for(UUID u : getMitglieder()){
            if(mitglieder == null){
                mitglieder = u.toString();
            } else mitglieder = mitglieder + " " + u.toString();
        }
        return mitglieder;
    }

    public List<Krieg> getActiveKriege(){
        List<Krieg> list = new ArrayList<>();
        for(Krieg krieg : HSMP.getKriegManager().getAllKriege()){
            if(krieg.getClan1().getUniqueId().equalsIgnoreCase(getUniqueId()) || krieg.getClan2().getUniqueId().equalsIgnoreCase(getUniqueId())){
                if(list.contains(krieg)) continue;
                list.add(krieg);
            }
        }
        return list;
    }

    public void setMitglieder(List<UUID> mitglieder) {
        this.mitglieder = mitglieder;
    }

    public void setMitgliederFromString(String str) {
        List<UUID> mitglieder = new ArrayList<>();
        for(String a : str.split("\\s+")){
            a = a.replaceAll(" ", "");
            if(a != null && !a.isEmpty()){
                mitglieder.add(UUID.fromString(a));
            }
        }
        this.mitglieder = mitglieder;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public UUID getHost() {
        return host;
    }

    public void setHost(UUID host) {
        this.host = host;
    }

    public List<UUID> getOps() {
        return ops;
    }

    public String getOpsAsString() {
        String mitglieder = null;
        for(UUID u : getOps()){
            if(mitglieder == null){
                mitglieder = u.toString();
            } else mitglieder = mitglieder + " " + u.toString();
        }
        return mitglieder;
    }

    public boolean isHost(UUID uuid){
        return getHost().equals(uuid);
    }

    public boolean isHost(Player p){
        return getHost().equals(p.getUniqueId());
    }

    public Location getClanHome(){
        String[] stat = getStatFromName("home").split(",");
        try {
            double x = Double.parseDouble(stat[0]);
            double y = Double.parseDouble(stat[1]);
            double z = Double.parseDouble(stat[2]);
            float yaw = Float.parseFloat(stat[3]);
            float pitch = Float.parseFloat(stat[4]);
            return new org.bukkit.Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
        } catch (Exception ignored){
        }
        return null;
    }

    public boolean isClanChatEnabled(){
        return getStatFromName("chat") != null ? getStatFromName("chat").equalsIgnoreCase("true") : true;
    }

    public boolean isJoinRequestsEnabled(){
        return getStatFromName("joinrequests") != null ? getStatFromName("joinrequests").equalsIgnoreCase("true") : true;
    }

    public boolean isStatsForClanMembersEnabled(){
        return getStatFromName("statsforclanmembers") != null ? getStatFromName("statsforclanmembers").equalsIgnoreCase("true") : true;
    }

    public boolean isStatsPublic(){
        return getStatFromName("isstatspublic") != null ? getStatFromName("isstatspublic").equalsIgnoreCase("true") : true;
    }
    public boolean isNormalMembersAllowedToInvitePlayers(){
        return getStatFromName("inviteplayerswithoutop") != null ? getStatFromName("inviteplayerswithoutop").equalsIgnoreCase("true") : false;
    }

    public void setClanChatEnabled(boolean enabled){
        setStat("chat", enabled + "");
    }
    public void setJoinRequestsEnabled(boolean enabled){
        setStat("joinrequests", enabled + "");
    }
    public void setStatsForClanMembersEnabled(boolean enabled){
        setStat("statsforclanmembers", enabled + "");
    }
    public void setStatsPublic(boolean enabled){
        setStat("isstatspublic", enabled + "");
    }
    public void setNormalMembersAllowedToInvitePlayers(boolean enabled){
        setStat("inviteplayerswithoutop", enabled + "");
    }

    public void setClanHome(Location l){
        String id = l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
        setStat("home", id);
    }

    public void removeClanHome(){
        removeStat("home");
    }

    public String getStatFromName(String statname){
        if(getStats() == null) return null;
        if(getStats().isEmpty()) return null;
        String response = null;
        for(String a : getStats().split("\\s+")){
            a = a.toLowerCase();
            if(a.startsWith(statname)){
                response = a.replaceAll(statname.toLowerCase() + "=", "");
            }
        }
        return response;
    }

    public void setStat(String value, String response){
        if(stats == null || stats.isEmpty()){
            stats = value + "=" + response;
        } else {
            if(stats.contains(value)){
                String toRemove = "";
                for(String a : stats.split("\\s+")){
                    if(a.contains(value + "=")){
                        toRemove = a;
                    }
                }
                stats = stats.replace(toRemove, "");
            }
            stats = stats + (stats.endsWith(" ") ? "" : " ") + value + "=" + response;
        }
    }

    public void removeStat(String value){
        if(stats == null || stats.isEmpty()){
            return;
        }
        if(stats.contains(value)){
            String toRemove = "";
            for(String a : stats.split("\\s+")){
                if(a.contains(value + "=")){
                    toRemove = a;
                }
            }
            stats = stats.replace(toRemove, "");
        }
    }

    public void setOps(List<UUID> ops) {
        this.ops = ops;
    }

    public void setOpsFromString(String str) {
        List<UUID> ops = new ArrayList<>();
        for(String a : str.split("\\s+")){
            a = a.replaceAll(" ", "");
            if(a != null && !a.isEmpty()){
                ops.add(UUID.fromString(a));
            }
        }
        this.ops = ops;
    }

    public List<String> getChunkIdsAsList() {
        List<String> list = new ArrayList<>();
        for(String s : chunkIds.split("\\s+")){
            s = s.trim();
            if(s != null && !s.isEmpty()){
                list.add(s);
            }
        }
        return list;
    }

    public void setChunkIds(String chunkIds) {
        this.chunkIds = chunkIds;
    }

    public void setChunkIdsFromList(List<String> chunkIds) {
        String str = "";
        for(String s : chunkIds){
            if(!s.isEmpty()) {
                str = str + " " + s;
            }
        }
        this.chunkIds = str;
    }

    public String getChunkIds() {
        return chunkIds;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }
}
