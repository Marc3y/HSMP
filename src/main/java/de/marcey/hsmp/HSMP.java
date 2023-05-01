package de.marcey.hsmp;

import de.marcey.hsmp.clansystem.ChunkProvider;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.commands.*;
import de.marcey.hsmp.listener.*;
import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.objects.PlayerStat;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.Playtime;
import de.marcey.hsmp.scoreboard.ScoreboardManager;
import de.marcey.hsmp.utils.Utils;
import de.marcey.hsmp.utils.config.ConfigManager;
import de.marcey.hsmp.utils.data.Locs;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.TimeProvider;
import de.marcey.hsmp.utils.data.krieg.KriegManager;
import de.marcey.hsmp.utils.mongodb.MongoDB;
import de.marcey.hsmp.utils.data.clans.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HSMP extends JavaPlugin {
    private static HSMP instance;
    private static MongoDB mongodb;
    private static ClanManager clans;
    private static KriegManager kriegmanager;
    private static String RESOURCE_PACK_URL = "";

    @Override
    public void onEnable() {
        instance = this;
        for(Player current : Bukkit.getOnlinePlayers()){
            current.kickPlayer(Strings.prefix + " §7Der Server reloadet nun. Bitte rejoine den Server!");
        }
        mongodb = new MongoDB("kenjihde_hsmp");
        mongodb.openConnection();
        registerEvents(getServer().getPluginManager());
        registerCommands();
        registerOther();
    }

    @Override
    public void onDisable() {
        mongodb.closeConnection();
    }

    public void registerEvents(PluginManager m){
        m.registerEvents(new RangListener(), this);
        m.registerEvents(new SpawnListener(), this);
        m.registerEvents(new SpawnElytraListener(this), this);
        m.registerEvents(new LogListener(), this);
        m.registerEvents(new AntiRaidFarmListener(),this);
        m.registerEvents(new AntiCrystalPvPListener(), this);
        m.registerEvents(new SitListener(), this);
        m.registerEvents(new ClanListener(), this);
        m.registerEvents(new ChunkListener(), this);
        m.registerEvents(new TeleportListener(), this);
        m.registerEvents(new ChunkSpawnListener(), this);
        m.registerEvents(new StatsListener(), this);
        m.registerEvents(new ResourcePackListener(), this);
        m.registerEvents(new PlayerListener(), this);
        m.registerEvents(new PvpListener(), this);
        m.registerEvents(new KriegListener(), this);
    }

    public void registerCommands(){
        getCommand("rang").setExecutor(new RangCommand());
        getCommand("clan").setExecutor(new ClanCommand());
        getCommand("spawnelytra").setExecutor(new SpawnElytraCommand());
        getCommand("hsmp").setExecutor(new HSmpCommand());
        getCommand("openend").setExecutor(new OpenEndCommand());
        getCommand("sit").setExecutor(new SitCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("playtime").setExecutor(new PlaytimeCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("pvp").setExecutor(new PvpCommand());
    }
    public void registerOther(){
        Utils.setDefaultConfigs();
        clans = new ClanManager();
        kriegmanager = new KriegManager();
        for(Clan c : clans.getAllClans()){
            ClanProvider.cs.put(c.getUniqueId(), c);
        }
        Rang.init();
        PlayerStat.init();
        ScoreboardManager.updater();
        TimeProvider.timer();
        LogListener.init();
        ChunkProvider.init();
        Playtime.updater();
        ConfigManager.init();
    }

    public static ClanManager getClans() {
        return clans;
    }

    public static KriegManager getKriegManager() {
        return kriegmanager;
    }

    public static MongoDB getMongodb() {
        return mongodb;
    }

    public static Location getSpawn(){
        Config config = new Config("Locations");
        return Locs.get(config, "Spawn");
    }

    public static String getResourcePackUrl() {
        return RESOURCE_PACK_URL;
    }

    public static HSMP getInstance() {
        return instance;
    }
}
