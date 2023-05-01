package de.marcey.hsmp.player;

import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.objects.PlayerStat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStats {

    public static PlayerStats getPlayerStats(UUID uuid){
        for(PlayerStats ps : getPlayerStats()){
            if(ps.getUuid().equals(uuid)){
                return ps;
            }
        }
        return new PlayerStats(uuid);
    }

    private Config config;
    private UUID uuid;
    private static List<PlayerStats> playerStats = new ArrayList<>();

    private static List<PlayerStats> getPlayerStats() {
        return playerStats;
    }

    public PlayerStats(UUID uuid){
        this.uuid = uuid;
        this.config = new Config("playerdata//" + uuid);
        playerStats.add(this);
    }

    public boolean isStatsPublic(){
        return getBoolean("IsPublic", true);
    }

    public void setStatsPublic(boolean statsPublic){
        getConfig().set("IsPublic", statsPublic);
        getConfig().save();
    }



    private Config getConfig() {
        return config;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getStat(PlayerStat stat) {
        return getInt(stat.getName());
    }

    public double getStatDouble(PlayerStat stat) {
        return getDouble(stat.getName());
    }

    public void setStat(PlayerStat stat, int toSet){
        config.set(stat.getName(), toSet);
        config.save();
    }

    public void addToStat(PlayerStat stat, int toAdd){
        config.set(stat.getName(), getStat(stat)+toAdd);
        config.save();
    }

    public void addToStatDouble(PlayerStat stat, double toAdd){
        config.set(stat.getName(), getStat(stat)+round(toAdd));
        config.save();
    }

    //get stuff

    private double round(double doubl){
        return Double.parseDouble(String.format("%.2f", doubl));
    }

    private int getInt(String path){
        if(!getConfig().contains(path)){
            return 0;
        } else return getConfig().getInt(path);
    }
    private String getString(String path){
        if(!getConfig().contains(path)){
            return null;
        } else return getConfig().getString(path);
    }
    private double getDouble(String path){
        if(!getConfig().contains(path)){
            return 0;
        } else return getConfig().getDouble(path);
    }

    private boolean getBoolean(String path, boolean Default){
        if(!getConfig().contains(path)){
            return Default;
        } else return getConfig().getBoolean(path);
    }
}
