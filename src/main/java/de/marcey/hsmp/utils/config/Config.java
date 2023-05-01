package de.marcey.hsmp.utils.config;

import de.marcey.hsmp.HSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    private final Plugin plugin = HSMP.getInstance();
    private final String file;
    private final File folder;
    private FileConfiguration cfg;
    private File cfgFile;

    public Config(String file){
        this.file = !file.contains("//") ? (file + ".yml") : (file.split("//")[1] + ".yml");
        folder = new File(plugin.getDataFolder() + "//settings//" + (file.contains("//") ? file.split("//")[0] + "//" : ""));
        cfg = null;
        cfgFile = null;
        reload();
    }

    public void reload() {
        if(!folder.exists()){
            folder.mkdirs();
        }
        cfgFile = new File(folder, file);
        if(!cfgFile.exists()){
            try {
                cfgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }
    public FileConfiguration getConfig(){
        if(cfg == null){
            reload();
        }
        return cfg;
    }

    public void save(){
        if(cfg == null || cfgFile == null){
            return;
        }
        try {
            getConfig().save(cfgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value){
        getConfig().set(path, value);
    }
    public String getString(String path){
        return getConfig().getString(path);
    }
    public int getInt(String path){
        return getConfig().getInt(path);
    }
    public boolean getBoolean(String path){
        return getConfig().getBoolean(path);
    }
    public double getDouble(String path){
        return getConfig().getDouble(path);
    }
    public List<String> getStringList(String path){
        return getConfig().getStringList(path);
    }
    public boolean contains(String path){
        if(getConfig().contains(path)){
            return getConfig().get(path) != null;
        }
        return getConfig().contains(path);
    }
}
