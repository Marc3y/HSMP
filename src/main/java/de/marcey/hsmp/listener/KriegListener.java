package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.gui.KriegeGUI;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import de.marcey.hsmp.clansystem.kriegsystem.KriegProvider;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class KriegListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player dead = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if(killer == null){
            return;
        }
        Clan clanOfDeathPerson = ClanProvider.getClan(dead.getUniqueId());
        Clan clanOfKiller = ClanProvider.getClan(killer.getUniqueId());
        if(clanOfDeathPerson == null || clanOfKiller == null){
            return;
        }
        if(!HSMP.getKriegManager().isInKrieg(clanOfKiller, clanOfDeathPerson)){
            return;
        }
        Krieg krieg = KriegProvider.getKrieg(clanOfKiller, clanOfDeathPerson);
        if(krieg == null){
            return;
        }
        if(krieg.getClan1().getUniqueId().equalsIgnoreCase(clanOfKiller.getUniqueId())){
            krieg.setKills_Clan1(krieg.getKills_Clan1()+1);
        } else krieg.setKills_Clan2(krieg.getKills_Clan2()+1);
        if(clanOfDeathPerson.getHost().equals(dead.getUniqueId())){
            hostDead(krieg, killer, dead, clanOfDeathPerson, clanOfKiller);
        }
    }

    public static void hostDead(Krieg krieg, Player killer, Player dead, Clan clanOfDeathPerson, Clan clanOfKiller){
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clanOfDeathPerson.getMitglieder().contains(current.getUniqueId())) {
                current.playSound(current.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 40, 0.7F);
                current.sendTitle("§c§lKrieg verloren", Rang.getRang(dead).getColor() + dead.getName() + " §7ist gestorben", 40, 100, 40);
                current.sendMessage();
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null){
            return;
        }
        if(e.getView().getTitle().toLowerCase().contains("aktive kriege")){
            if(e.getCurrentItem().getType().equals(Material.OAK_DOOR)){
                p.closeInventory();
            } else if(e.getCurrentItem().getType().name().toLowerCase().contains("banner")){
                KriegeGUI.switchToNextSortierType(p);
                KriegeGUI.openInv(p, ClanProvider.getClan(p));
            } else if(e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                if(lore != null){
                    p.sendMessage(Strings.prefix + " §7Informationen zum Krieg:");
                    p.sendMessage("§7");
                    p.sendMessage(Strings.prefix + " §bGegner-Clan: §c" + e.getCurrentItem().getItemMeta().getDisplayName());
                    for(String l : lore){
                        p.sendMessage(Strings.prefix + " " + l);
                    }
                    p.closeInventory();
                }
            }
            e.setCancelled(true);
        }
    }

}
