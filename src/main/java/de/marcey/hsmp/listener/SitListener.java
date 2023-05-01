package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitListener implements Listener {

    public static Map<Player, ArmorStand> sit = new HashMap<>();
    public static List<Player> sneakCooldown = new ArrayList<>();

    @EventHandler
    public void dismountEvent(EntityDismountEvent e){
        if(sit.containsKey((Player) e.getEntity())){
            Player p = (Player) e.getEntity();
            if(p.isSneaking() && sneakCooldown.contains(p)){
                e.setCancelled(true);
                return;
            }
            sit.get(p).remove();
            sit.remove(p);
            sneakCooldown.remove(p);
            p.teleport(p.getLocation().add(0, 1, 0));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        if(e.getClickedBlock() == null){
            return;
        }
        if(e.getClickedBlock().getType().name().toLowerCase().contains("stair") || e.getClickedBlock().getType().name().toLowerCase().contains("slab")){

            if(!sit.containsKey(p)){
                if(p.isSneaking()) {
                    if (e.getClickedBlock().getType().name().toLowerCase().contains("slab")) {
                        sit(p, e.getClickedBlock().getLocation().add(0.5, 0.25, 0.5));
                    } else {
                        sit(p, e.getClickedBlock().getLocation().add(0.45, 0.25, 0.45));
                    }
                }
            }
        }
    }

    public static void sit(Player p, Location location){
        if(sit.containsKey(p)){
            Location l = location;
            World w = p.getWorld();
            ArmorStand arrow = w.spawn(l, ArmorStand.class, stand -> {
                stand.setMarker(true);
                stand.setInvisible(true);
                stand.setCustomNameVisible(false);
            });
            sit.put(p, arrow);
            arrow.addPassenger(p);
            sneakCooldown.add(p);
            Bukkit.getScheduler().runTaskLater(HSMP.getInstance(), new Runnable() {
                @Override
                public void run() {
                    sneakCooldown.remove(p);
                }
            }, 20);
            return;
        }
        Location l = location;
        World w = p.getWorld();
        ArmorStand arrow = w.spawn(l, ArmorStand.class, stand -> {
            stand.setMarker(true);
            stand.setInvisible(true);
            stand.setCustomNameVisible(false);
        });
        sit.put(p, arrow);
        arrow.addPassenger(p);
        sneakCooldown.add(p);
        Bukkit.getScheduler().runTaskLater(HSMP.getInstance(), new Runnable() {
            @Override
            public void run() {
                sneakCooldown.remove(p);
            }
        }, 20);
        return;
    }

}
