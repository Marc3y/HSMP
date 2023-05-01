package de.marcey.hsmp.utils.config;

import org.bukkit.entity.Player;

public class ConfigManager {

    public static Config getPlayerConfig(Player p){
        return new Config("playerdata//" + p.getUniqueId());
    }

    public static void init(){

    }

}
