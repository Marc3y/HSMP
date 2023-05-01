package de.marcey.hsmp.listener;

import de.marcey.hsmp.teleportsystem.TeleportProvider;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location movedFrom = e.getFrom();
        Location movedTo = e.getTo();
        if (((int) movedFrom.getX() != (int) movedTo.getX()) || ((int) movedFrom.getY() != (int) movedTo.getY()) || ((int) movedFrom.getZ() != (int) movedTo.getZ())) {
            if(TeleportProvider.isInTeleportVorgang(p)) {
                TeleportProvider.removeTeleportVorgang(p);
                p.sendMessage(Strings.prefix + " §cDer Teleport-Vorgang wurde abgebrochen, da du dich bewegt hast.");
            }
        }
    }

}
