package de.marcey.hsmp.listener;

import de.marcey.hsmp.commands.HSmpCommand;
import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.utils.data.Locs;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpawnListener implements Listener {

    public static int spawnRadius = 170;
    public static Config config;
    private static List<UUID> inspawnradius = new ArrayList<>();

    public SpawnListener(){
        config = new Config("Locations");
    }

    @Deprecated
    public static boolean isInSpawn(Player p){
        if(!p.getLocation().getWorld().getName().equalsIgnoreCase("world")) return false;
        return inspawnradius.contains(p.getUniqueId());
    }

    public static boolean isInSpawn(Location loc){
        return checkIsInSpawn(loc);
    }

    private static boolean checkIsInSpawn(Location loc){
        if(!loc.getWorld().getName().equalsIgnoreCase("world")) return false;
        return Locs.get(config, "Spawn").distance(loc) < spawnRadius;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(checkIsInSpawn(p.getLocation())){
            setPlayerOnSpawn(p.getUniqueId(), true);
        } else setPlayerOnSpawn(p.getUniqueId(), false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        setPlayerOnSpawn(p.getUniqueId(), checkIsInSpawn(p.getLocation()));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if(e.getTo() != null) {
            setPlayerOnSpawn(p.getUniqueId(), checkIsInSpawn(e.getTo()));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(isInSpawn(p) && isValid(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(isInSpawn(p) && isValid(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(isInSpawn(p) && isValid(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onChangeWorld(EntityChangeBlockEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        Player p = (Player) e.getEntity();
        if(isInSpawn(p) && isValid(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)){
            return;
        }
        Player p = (Player) e.getDamager();
        if(isInSpawn(p) && isValid(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        Player p = (Player) e.getEntity();
        if(isInSpawn(p) && isValid(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        if(e.getEntity() == null){
            return;
        }
        if(isInSpawn(e.getEntity().getLocation())) e.setCancelled(true);
    }
    private static boolean isValid(Player p){
        if(!HSmpCommand.build) return true;
        if(p.hasPermission("HSMP.Admin")) return false;
        return true;
    }

    private static void setPlayerOnSpawn(UUID uuid, boolean isOnSpawn){
        if(!isOnSpawn){
            inspawnradius.remove(uuid);
        } else if(!inspawnradius.contains(uuid)){
            inspawnradius.add(uuid);
        }
    }
}
