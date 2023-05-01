package de.marcey.hsmp.listener;

import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.gui.SettingsGUI;
import de.marcey.hsmp.objects.PlayerStat;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.PlayerSettings;
import de.marcey.hsmp.player.PlayerStats;
import de.marcey.hsmp.player.gui.PlayerSettingsGUI;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.sounds.SoundProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null){
            return;
        }
        if(!e.getView().getTitle().endsWith("§8Settings")){
            return;
        }
        PlayerSettings settings = new PlayerSettings(p);
        if(e.getCurrentItem().getType().equals(Material.LIME_CONCRETE)){
            Material m = e.getClickedInventory().getItem(e.getSlot()-9).getType();
            ItemStack item = e.getClickedInventory().getItem(e.getSlot()-9);
            boolean is = false;
            if(m.equals(Material.NOTE_BLOCK)){
                settings.setMusicEnabled(is);
                SoundProvider.stopSounds(p);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.WRITABLE_BOOK)){
                settings.setMsgEnabled(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.PAPER)){
                settings.setChatEnabled(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.GRASS_BLOCK)){
                settings.setStatsPublic(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.BELL)){
                settings.setStreamNotifications(is);
                PlayerSettingsGUI.openGUI(p);
            }
            p.sendMessage(Strings.prefix + " §7Du hast die Einstellung " + item.getItemMeta().getDisplayName() + " §cdeaktiviert§7.");
        } else if(e.getCurrentItem().getType().equals(Material.RED_CONCRETE)){
            Material m = e.getClickedInventory().getItem(e.getSlot()-9).getType();
            ItemStack item = e.getClickedInventory().getItem(e.getSlot()-9);
            boolean is = true;
            if(m.equals(Material.NOTE_BLOCK)){
                settings.setMusicEnabled(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.WRITABLE_BOOK)){
                settings.setMsgEnabled(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.PAPER)){
                settings.setChatEnabled(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.GRASS_BLOCK)){
                settings.setStatsPublic(is);
                PlayerSettingsGUI.openGUI(p);
            } else if(m.equals(Material.BELL)){
                settings.setStreamNotifications(is);
                PlayerSettingsGUI.openGUI(p);
            }
            p.sendMessage(Strings.prefix + " §7Du hast die Einstellung " + item.getItemMeta().getDisplayName() + " §aaktiviert§7.");
        }
        e.setCancelled(true);
    }

}
