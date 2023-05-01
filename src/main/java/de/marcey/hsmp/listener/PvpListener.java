package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {
    private static FileConfiguration config = HSMP.getInstance().getConfig();
    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            if(!config.getBoolean("PVP")){
                e.setCancelled(true);
            }
        }
    }
}
