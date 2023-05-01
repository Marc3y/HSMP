package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.Strings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpawnElytraListener implements Listener {

    private int spawnRadius;
    private int multiplyValue;
    private String worldname;
    private boolean isBoostEnabled;

    private final List<Player> flying = new ArrayList<>();
    private final List<Player> boosted = new ArrayList<>();
    public SpawnElytraListener(Plugin plugin){
        this.spawnRadius = 170;
        this.multiplyValue = 5;
        this.worldname = "world";
        this.isBoostEnabled = true;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(Bukkit.getWorld(worldname) != null){
                Bukkit.getWorld(worldname).getPlayers().forEach(player -> {
                    if(player.getGameMode() != GameMode.SURVIVAL) return;
                    player.setAllowFlight(isInSpawnRadius(player));

                    if(flying.contains(player) && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()){
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.setGliding(false);
                        boosted.remove(player);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            flying.remove(player);
                        }, 5);
                    }
                });
            }
        }, 0, 3);
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent e){
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        if(!isInSpawnRadius(e.getPlayer())) return;
        e.setCancelled(true);
        e.getPlayer().setGliding(true);
        if(isBoostEnabled) {
            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new ComponentBuilder("§7Drücke §6")
                            .append(new KeybindComponent("key.swapOffhand"))
                            .append(" §7um dich zu boosten")
                            .create());
        }
        flying.add(e.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntityType() == EntityType.PLAYER
                && (e.getCause() == EntityDamageEvent.DamageCause.FALL
                || e.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL)
                && flying.contains(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onToggleGlide(EntityToggleGlideEvent e){
        if(e.getEntityType() == EntityType.PLAYER && flying.contains(e.getEntity())) e.setCancelled(true);
    }
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e){
        if(boosted.contains(e.getPlayer())) return;
        if(isBoostEnabled && flying.contains(e.getPlayer())) {
            e.setCancelled(true);
            boosted.add(e.getPlayer());
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(multiplyValue));
        }
    }

    private boolean isInSpawnRadius(Player p){
        return SpawnListener.isInSpawn(p);
    }

}
