package de.marcey.hsmp.listener;

import de.marcey.hsmp.objects.PlayerStat;
import de.marcey.hsmp.player.PlayerStats;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Smoker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsListener implements Listener {

    public static HashMap<Player, Long> joined = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        joined.put(p, System.currentTimeMillis());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        if(e.getEntity().getKiller() == null){
            return;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(e.getEntity().getKiller().getUniqueId());
        stats.addToStat(PlayerStat.KILLS, 1);
    }

    @EventHandler
    public void onKill(EntityResurrectEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        Player p = (Player) e.getEntity();
        if(p.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) || p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)){
            PlayerStats stats = PlayerStats.getPlayerStats(p.getUniqueId());
            stats.addToStat(PlayerStat.TOTEM_USED, 1);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        PlayerStats stats = PlayerStats.getPlayerStats(e.getEntity().getUniqueId());
        stats.addToStat(PlayerStat.DEATHS, 1);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e){
        if(e.getEntity().getKiller() == null){
            return;
        }
        if(!e.getEntityType().isAlive()){
            return;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(e.getEntity().getKiller().getUniqueId());
        stats.addToStat(PlayerStat.MOB_KILLS, 1);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        if(!(e.getDamager() instanceof Player)){
            return;
        }
        Player p = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if(damager == null || p == null){
            return;
        }

        PlayerStats stats = PlayerStats.getPlayerStats(damager.getUniqueId());
        stats.addToStatDouble(PlayerStat.DAMAGE_DID, (int) e.getDamage());
    }

    @EventHandler
    public void onEaten(PlayerItemConsumeEvent e){
        if(e.getItem().getType().name().toLowerCase().contains("bottle")){
            return;
        }
        PlayerStats stats = PlayerStats.getPlayerStats(e.getPlayer().getUniqueId());
        stats.addToStat(PlayerStat.EATEN, 1);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        PlayerStats stats = PlayerStats.getPlayerStats(e.getPlayer().getUniqueId());
        stats.addToStat(PlayerStat.BLOCKS_PLACED, 1);
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        PlayerStats stats = PlayerStats.getPlayerStats(e.getPlayer().getUniqueId());
        stats.addToStat(PlayerStat.BLOCKS_BREAKED, 1);
    }

    @EventHandler
    public void onItemCrafted(CraftItemEvent e){
        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }
        Player p = (Player) e.getWhoClicked();
        PlayerStats stats = PlayerStats.getPlayerStats(p.getUniqueId());
        stats.addToStat(PlayerStat.ITEMS_CRAFTED, 1);
    }

    @EventHandler
    public void onSleep(PlayerBedLeaveEvent e){
        if(Bukkit.getServer().getWorld("world").getTime() <= 50){
            PlayerStats stats = PlayerStats.getPlayerStats(e.getPlayer().getUniqueId());
            stats.addToStat(PlayerStat.BED_USED, 1);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        PlayerStats stats = PlayerStats.getPlayerStats(e.getPlayer().getUniqueId());
        stats.setStat(PlayerStat.DISTANCE_TRAVELLED, (int) e.getPlayer().getStatistic(Statistic.WALK_ONE_CM)/100);
    }


    @EventHandler
    public void onInv(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }
        if(e.getClickedInventory() == null){
            return;
        }
        if(e.getClickedInventory().getSize() == (5*9) && e.getView().getTitle().contains("Stats | ")){
            e.setCancelled(true);
        }
    }

}
