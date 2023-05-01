package de.marcey.hsmp.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiCrystalPvPListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){
            return;
        }
        if(!e.getDamager().getType().equals(EntityType.ENDER_CRYSTAL)){
            return;
        }
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        e.setCancelled(true);
    }

}
