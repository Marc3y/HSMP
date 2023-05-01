package de.marcey.hsmp.utils.data;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Locs {

    public static void save(Config config, String path, Location loc){
        World world = loc.getWorld();
        String configname = path;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        config.getConfig().set(configname + ".World", world.getName());
        config.getConfig().set(configname + ".X", x);
        config.getConfig().set(configname + ".Y", y);
        config.getConfig().set(configname + ".Z", z);
        config.getConfig().set(configname + ".Yaw", yaw);
        config.getConfig().set(configname + ".Pitch", pitch);
        config.save();
    }

    public static Location get(Config config, String path){
        String configname = path;
        if(config.getConfig().contains(configname + ".World")){
            String world = config.getConfig().getString(configname + ".World");
            double x = config.getConfig().getDouble(configname + ".X");
            double y = config.getConfig().getDouble(configname + ".Y");
            double z = config.getConfig().getDouble(configname + ".Z");
            float yaw = config.getConfig().getInt(configname + ".Yaw");
            float pitch = config.getConfig().getInt(configname + ".Pitch");
            if(Bukkit.getWorld(world) == null){
                new WorldCreator(world).createWorld();
            }
            Location locToReturn = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            return locToReturn;
        }
        return null;
    }
    public static void remove(Config config, String path){
        String configname = path;
        if(config.getConfig().contains(configname + ".World")){
            config.getConfig().set(configname, null);
            HSMP.getInstance().saveConfig();
        }
    }

    public static boolean containsLocation(Config config, String path){
        if(config.getConfig().contains(path + ".World")){
            return true;
        }
        return false;
    }

}
