package de.marcey.hsmp.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AntiRaidFarmListener implements Listener {

    private static Cache<UUID, Long> lastRaidCache;
    public static void init(){
        lastRaidCache = CacheBuilder.newBuilder()
                .expireAfterWrite(180, TimeUnit.SECONDS)
                .build();
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent e){
        Player p = e.getPlayer();
        boolean hasCooldown = lastRaidCache.getIfPresent(p.getUniqueId()) != null;
        if(hasCooldown){
            e.setCancelled(true);
        } else {
            lastRaidCache.put(p.getUniqueId(), System.currentTimeMillis());
        }
    }

}
