package de.marcey.hsmp.objects;

import de.marcey.hsmp.utils.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rang {

    public static Rang SPIELER;
    public static Rang SUB;
    public static Rang STREAMER;
    public static Rang SMP_MOD;
    public static Rang DEVELOPER;
    public static Rang FOUNDER;

    private String name;
    private String displayname;
    private String formattedDisplayname;
    private ChatColor color;
    private boolean bold;
    private int priority;
    public static List<Rang> rangs = new ArrayList<>();

    //Init nur weil ansonsten die Class nicht initialized wird
    public static void init(){
        SPIELER = new Rang("Spieler", ChatColor.GRAY, 5).withEmpty();
        SUB = new Rang("Sub", ChatColor.DARK_PURPLE, 4);
        STREAMER = new Rang("Streamer", ChatColor.LIGHT_PURPLE, 3);
        SMP_MOD = new Rang("SMP-Mod", ChatColor.DARK_GREEN, 2);
        DEVELOPER = new Rang("Developer", ChatColor.BLUE, 1);
        FOUNDER = new Rang("Founder", ChatColor.DARK_RED, 0);
    }

    public Rang(String displayname, ChatColor color, int priority){
        this.displayname = displayname;
        this.name = displayname;
        this.color = color;
        this.bold = false;
        this.priority = priority;
        this.formattedDisplayname = color + this.displayname + " §r§7| §r" + color;
        rangs.add(this);
    }

    public Rang withEmpty(){
        this.formattedDisplayname = "";
        return this;
    }

    public Rang withBold(){
        this.bold = true;
        this.formattedDisplayname = color + "" + ChatColor.BOLD + this.displayname + " §r§8| §r" + color + ChatColor.BOLD + " ";
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDisplayname() {
        return displayname;
    }

    public int getPriority() {
        return priority;
    }

    public ChatColor getColor() {
        return color;
    }

    public static List<Rang> getRangs() {
        return rangs;
    }

    public String getFormattedDisplayname() {
        return formattedDisplayname;
    }

    public static void setRang(Player p, Rang rang){
        Config cfg = new Config("Rangs");
        FileConfiguration config = cfg.getConfig();
        config.set("Rang." + p.getUniqueId(), rang.getDisplayname());
        cfg.save();
    }

    private static Rang getRangByName(String name){
        for(Rang r : rangs){
            if(r.getDisplayname().equalsIgnoreCase(name) || r.getName().equalsIgnoreCase(name)){
                return r;
            }
        }
        return null;
    }

    public static Rang getRang(Player p){
        FileConfiguration config = new Config("Rangs").getConfig();
        Rang rang = Rang.getRangByName(config.getString("Rang." + p.getUniqueId()));
        return rang != null ? rang : Rang.SPIELER;
    }
    public static Rang getRang(UUID uuid){
        FileConfiguration config = new Config("Rangs").getConfig();
        Rang rang = Rang.getRangByName(config.getString("Rang." + uuid));
        return rang != null ? rang : Rang.SPIELER;
    }

}
