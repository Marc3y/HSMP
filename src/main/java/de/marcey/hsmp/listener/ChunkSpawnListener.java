package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.utils.data.Locs;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ChunkSpawnListener implements Listener {

    private static Config playerinfos = new Config("playerinfos");

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Locs.save(playerinfos, "LastLocation." + p.getUniqueId(), p.getLocation());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!Locs.containsLocation(playerinfos, "LastLocation." + p.getUniqueId())){
            Locs.save(playerinfos, "LastLocation." + p.getUniqueId(), p.getLocation());
        }
        if(toSpawn(p.getUniqueId())){
            p.teleport(HSMP.getSpawn());
            p.sendMessage(Strings.prefix + " §7Du wurdest zum Spawn teleportiert, da du dich in einem gegnerischen Clan-Chunk befunden hast. Dies kann passieren, wenn du zum Beispiel aus Clan's gekickt wirst.");
            removeToSpawn(p.getUniqueId());
        }
    }

    public static Location getLastLocation(UUID uuid){
        return Locs.get(playerinfos, "LastLocation." + uuid);
    }
    public static void setNextJoinToSpawn(UUID uuid){
        playerinfos.getConfig().set("SpawnTeleport." + uuid, true);
        playerinfos.save();
    }

    public static boolean toSpawn(UUID uuid){
        if(playerinfos.getConfig().contains("SpawnTeleport." + uuid)) {
            return playerinfos.getConfig().getBoolean("SpawnTeleport." + uuid);
        } else return false;
    }

    private static void removeToSpawn(UUID uuid){
        if(playerinfos.getConfig().contains("SpawnTeleport." + uuid)){
            playerinfos.getConfig().set("SpawnTeleport." + uuid, null);
            playerinfos.save();
        }
    }

}
