package de.marcey.hsmp.player.gui;

import de.marcey.hsmp.objects.PlayerStat;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.PlayerStats;
import de.marcey.hsmp.player.Playtime;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerStatsGUI {

    private static Inventory getGuiOfPlayer(UUID uuid){
        PlayerStats stats = PlayerStats.getPlayerStats(uuid);

        Inventory inv = Bukkit.createInventory(null, 5*9, "Stats | " + Bukkit.getOfflinePlayer(uuid).getName());

        for(PlayerStat stat : PlayerStat.getStats()){
            ItemStack stack = new ItemStack(stat.getMaterial(), 1);

            if(stat.getMaterial().equals(Material.PLAYER_HEAD)){
                stack = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
                stack.setItemMeta(skullMeta);
            }

            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§e" + stat.getDisplayname());

            List<String> lore = new ArrayList<>();
            lore.add("§7");
            lore.addAll(stat.getDescriptionAsList(uuid));
            lore.add("§7");
            if(stat.getDisplayname().equalsIgnoreCase("Damage gemacht")) {
                lore.add(stat.getDisplay().replaceAll("\\{count}", stats.getStatDouble(stat) + ""));
            } else if(stat.getDisplayname().toLowerCase().contains("spielzeit") || stat.getDisplayname().toLowerCase().contains("playtime")){
                lore.add(stat.getDisplay().replaceAll("\\{count}", stat.getDisplay().replaceAll("\\{count}", Playtime.getFormattedPlaytime(uuid))));
            } else {
                lore.add(stat.getDisplay().replaceAll("\\{count}", stats.getStat(stat) + ""));
            }
            lore.add("§7");

            meta.setLore(lore);
            stack.setItemMeta(meta);
            inv.setItem(stat.getSlot(), stack);
        }
        return inv;
    }
    public static void openStatsInv(Player toOpen, UUID fromStats, boolean isMod){
        if(isMod){
            toOpen.openInventory(getGuiOfPlayer(fromStats));
            PlayerStats stats = PlayerStats.getPlayerStats(fromStats);
            if(!toOpen.getUniqueId().equals(fromStats) && !stats.isStatsPublic()){
                toOpen.sendMessage(Strings.prefix + " §cDieser Spieler hat seine Statistiken versteckt. §7Du hast jedoch die Rechte die Statistiken anzuschauen. Das Veröffentlichen diesen Statistiken ohne einen guten Grund, kann zu einer " +
                        "§cRang-Entfernung §7führen.");
            }
            toOpen.sendMessage(Strings.prefix + " §7Du hast die Statistiken von " + Rang.getRang(fromStats).getFormattedDisplayname() + Bukkit.getOfflinePlayer(fromStats).getName() + " §ageöffnet§7.");
        } else {
            PlayerStats stats = PlayerStats.getPlayerStats(fromStats);
            if(!toOpen.getUniqueId().equals(fromStats) && !stats.isStatsPublic()){
                toOpen.sendMessage(Strings.prefix + " §cDieser Spieler hat seine Statistiken versteckt.");
                return;
            }
            toOpen.sendMessage(Strings.prefix + " §7Du hast die Statistiken von " + Rang.getRang(fromStats).getFormattedDisplayname() + Bukkit.getOfflinePlayer(fromStats).getName() + " §ageöffnet§7.");
            toOpen.openInventory(getGuiOfPlayer(fromStats));
        }
    }


}
