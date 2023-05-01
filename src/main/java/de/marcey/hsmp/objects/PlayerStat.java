package de.marcey.hsmp.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerStat {

    public static PlayerStat KILLS;
    public static PlayerStat TOTEM_USED;
    public static PlayerStat DEATHS;
    public static PlayerStat MOB_KILLS;
    public static PlayerStat DAMAGE_DID;
    public static PlayerStat EATEN;
    public static PlayerStat BLOCKS_PLACED;
    public static PlayerStat BLOCKS_BREAKED;
    public static PlayerStat ITEMS_CRAFTED;
    public static PlayerStat PLAYTIME;
    public static PlayerStat BED_USED;
    public static PlayerStat DISTANCE_TRAVELLED;
    public static PlayerStat CHAT_MESSAGES_SEND;
    public static PlayerStat CLANS_JOINED;

    public static void init(){
        KILLS = new PlayerStat("Kills", "KILLS", Material.DIAMOND_SWORD, 12, "§fKills: §e{count}", "§7Diese Statistik zeigt,", "§7wie viele Spieler", "§7{name} §7getötet hat.");
        TOTEM_USED = new PlayerStat("Totems genutzt","TOTEM_USED", Material.TOTEM_OF_UNDYING, 14, "§fBenutzt: §e{count}","§7Diese Statistik zeigt,", "§7wie oft {name} ein", "§7Totem benutzt hat.");
        DEATHS = new PlayerStat("Tode","DEATHS", Material.DEAD_BUSH, 19, "§fTode: §e{count}","§7Diese Statistik zeigt,", "§7wie oft {name} gestorben ist.");
        MOB_KILLS = new PlayerStat("Mob-Kills","MOB_KILLS", Material.ZOMBIE_HEAD, 20, "§fMob-Kills: §e{count}","§7Diese Statistik zeigt,", "§7wie viele Mobs {name}", "§7getötet hat.");
        DAMAGE_DID = new PlayerStat("Damage gemacht","DAMAGE_DID", Material.APPLE, 21,"§fDamage: §e{count}", "§7Diese Statistik zeigt,", "§7wie viel Damage {name}", "§7an Spieler verteilt hat.");
        EATEN = new PlayerStat("Gegessen","EATEN", Material.COOKED_BEEF, 22, "§fAnzahl: §e{count}","§7Diese Statistik zeigt,", "§7wie oft {name} etwas", "§7gegessen hat.");
        BLOCKS_PLACED = new PlayerStat("Blöcke platziert","BLOCKS_PLACED", Material.GRASS_BLOCK, 23, "§fBlöcke platziert: §e{count}","§7Diese Statistik zeigt,", "§7wie viele Blöcke {name}", "§7platziert hat.");
        BLOCKS_BREAKED = new PlayerStat("Blöcke abgebaut","BLOCKS_BREAKED", Material.IRON_PICKAXE, 24,"§fBlöcke abgebaut: §e{count}", "§7Diese Statistik zeigt,", "§7wie viele Blöcke {name}", "§7abgebaut hat.");
        ITEMS_CRAFTED = new PlayerStat("Items gecrafted","ITEMS_CRAFTED", Material.CRAFTING_TABLE, 25, "§fItems gecraftet: §e{count}","§7Diese Statistik zeigt,", "§7wie viele Items {name}", "§7gecraftet hat.");
        PLAYTIME = new PlayerStat("Spielzeit","PLAYTIME", Material.PLAYER_HEAD, 29, "§fSpielzeit: §e{count}","§7Diese Statistik zeigt,", "§7wie lange {name}", "§7auf dem HSMP gespielt hat.");
        BED_USED = new PlayerStat("Geschlafen","BED_USED", Material.RED_BED, 30, "§fGeschlafen: §e{count}","§7Diese Statistik zeigt,", "§7wie oft {name} in einem Bett", "§7geschlafen hat.");
        DISTANCE_TRAVELLED = new PlayerStat("Distanz gelaufen","DISTANCE_TRAVELLED", Material.DIAMOND_BOOTS, 31, "§fDistanz: §e{count}","§7Diese Statistik zeigt,", "§7wie viele Blöcke {name}", "§7getravellet ist.");
        CHAT_MESSAGES_SEND = new PlayerStat("Nachrichten gesendet","CHAT_MESSAGES_SEND", Material.WRITABLE_BOOK, 32, "§fNachrichten gesendet: §e{count}","§7Diese Statistik zeigt,", "§7wie viele Nachrichten {name}", "§7in den Chat gesendet hat.");
        CLANS_JOINED = new PlayerStat("Clans beigetreten","CLANS_JOINED", Material.TORCH, 33, "§fClans beigetreten: §e{count}","§7Diese Statistik zeigt,", "§7wie oft {name} einem Clan beigetreten", "§7ist.");
    }

    private static List<PlayerStat> stats = new ArrayList<>();

    public static List<PlayerStat> getStats() {
        return stats;
    }

    private String name;
    private Material material;
    private String displayname;
    private int slot;
    private String[] description;
    private String display;

    public PlayerStat(String displayname, String name, Material material, int slot, String display, String... description){
        this.material = material;
        this.name = name;
        this.displayname = displayname;
        this.slot = slot;
        this.description = description;
        this.display = display;
        stats.add(this);
    }

    public List<String> getDescriptionAsList(UUID fromStats) {
        List<String> list = new ArrayList<>();
        String name = Bukkit.getOfflinePlayer(fromStats).getName();
        for(String s : description){
            list.add(s.replaceAll("\\{name}", name));
        }
        return list;
    }

    public String getDisplayname() {
        return displayname;
    }

    public int getSlot() {
        return slot;
    }

    public String getDisplay() {
        return display;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
