package de.marcey.hsmp.utils;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {

    public static void setDefaultConfigs(){
        Config cfg = new Config("Locations");
        FileConfiguration locs = cfg.getConfig();
        if(!locs.contains("Radius")){
            locs.set("Radius", 10);
            cfg.save();
        }
        if(!locs.contains("Boost")){
            locs.set("Boost", 5);
            cfg.save();
        }
        if(!locs.contains("BoostEnabled")){
            locs.set("BoostEnabled", true);
            cfg.save();
        }
        if(!locs.contains("Worldname")){
            locs.set("Worldname", "world");
            cfg.save();
        }
        if(!locs.contains("Spawn")){
            World loc = Bukkit.getWorld("world");
            if(loc != null) {
                locs.set("Spawn", loc.getSpawnLocation());
                cfg.save();
            }
        }
        if(!HSMP.getInstance().getConfig().contains("OpenEnd")){
            HSMP.getInstance().getConfig().set("OpenEnd", false);
            HSMP.getInstance().saveConfig();
        }
    }

}
