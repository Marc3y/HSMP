package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.Strings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class EndListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent e){
        Player p = e.getPlayer();
        if(e.getTo().getWorld().getName().contains("end")){
            if(!HSMP.getInstance().getConfig().getBoolean("OpenEnd")) {
                e.setCancelled(true);
            } else p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Strings.prefix + " §7Du hast das §eEnde §abetreten§7."));
        }
    }

}
