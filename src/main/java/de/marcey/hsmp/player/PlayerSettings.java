package de.marcey.hsmp.player;

import de.marcey.hsmp.utils.config.Config;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerSettings {

    private Config config;
    private UUID uuid;

    public PlayerSettings(Player p){
        this.uuid = p.getUniqueId();
        this.config = new Config("settings" + "//" + this.uuid);
    }
    public PlayerSettings(UUID uuid){
        this.uuid = uuid;
        this.config = new Config("settings" + "//" + this.uuid);
    }

    public boolean isMusicEnabled(){
        return getBoolean("Music",true);
    }

    public boolean isMsgsEnabled(){
        return getBoolean("Msg", true);
    }
    public boolean isChatEnabled(){
        return getBoolean("Chat", true);
    }
    public boolean isStatsPublic(){
        return getBoolean("PublicStats", true);
    }
    public boolean isStreamNotificationsEnabled(){
        return getBoolean("StreamNotifications", true);
    }

    public void setMusicEnabled(boolean is){
        getConfig().set("Music", is);
        getConfig().save();
    }
    public void setMsgEnabled(boolean is){
        getConfig().set("Msg", is);
        getConfig().save();
    }

    public void setChatEnabled(boolean is){
        getConfig().set("Chat", is);
        getConfig().save();
    }
    public void setStatsPublic(boolean is){
        getConfig().set("PublicStats", is);
        getConfig().save();
    }
    public void setStreamNotifications(boolean is){
        getConfig().set("StreamNotifications", is);
        getConfig().save();
    }

    public Config getConfig() {
        return config;
    }

    public UUID getUuid() {
        return uuid;
    }

    //gets

    private int getInt(String path){
        if(!config.contains(path)){
            return 0;
        } else return config.getInt(path);
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
