package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourcePackListener implements Listener {

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent e){
        Player p = e.getPlayer();
        if(e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED) || e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)){
            p.kickPlayer(Strings.prefix + " §cUm auf dem HSMP spielen zu können musst du das Resource-Pack akzeptieren. §7Wenn bei dir keine Info zum downloaden eines Resourcepacks kommt, gehe sicher, dass du Server-Resource-Packs aktiviert hast. Dies kannst du in der Server-List tun.");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
     //   p.setResourcePack(HSMP.getResourcePackUrl());
    }

}
