package de.marcey.hsmp.clansystem.gui;

import de.marcey.hsmp.apis.colorapi.ColorGradient;
import de.marcey.hsmp.apis.customheads.CustomHeads;
import de.marcey.hsmp.apis.gui.ForValue;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.apis.gui.Gui;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import de.marcey.hsmp.objects.DateTime;
import de.marcey.hsmp.objects.enums.SortierType;
import de.marcey.hsmp.utils.DateUtil;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.utils.config.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Date;

public class KriegeGUI {

    public static void openInv(Player p, Clan clan){
        if(clan == null){
            p.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        SortierType sortierType = getSortierType(p);

        ItemStack back = CustomHeads.getCustomHeadFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=");
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName("§7Vorherige Seite");
        meta.setLore(Arrays.asList("§7", "§7Gehe zur vorherigen Seite zurück.", "§7"));
        back.setItemMeta(meta);

        ItemStack forward = CustomHeads.getCustomHeadFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=");
        ItemMeta forwardmeta = forward.getItemMeta();
        forwardmeta.setDisplayName("§7Nächste Seite");
        forwardmeta.setLore(Arrays.asList("§7", "§7Gehe zur nächsten Seite.", "§7"));
        forward.setItemMeta(forwardmeta);

        Inventory gui = new Gui()

                .withDisplayname("§cAktive Kriege §7| §e" + clan.getClanname())

                .withItem(new ColorGradient("» Sortierung").withFrom(ChatColor.of("#27bfc4")).execute())
                .thatHasMaterial(Material.FLOWER_BANNER_PATTERN)
                .thatHasItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .thatHasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                .thatHasItemFlag(ItemFlag.HIDE_ENCHANTS)
                .thatAddsLore("§7",
                        (sortierType.equals(SortierType.A_TO_Z) ? "§r§f§l» " : "") + new ColorGradient("Alphabetisch (A-Z)").withFrom(ChatColor.of("#E7E11D")).withTo(ChatColor.of("#D6D11C")).withBold(sortierType.equals(SortierType.A_TO_Z)).execute(),
                        (sortierType.equals(SortierType.TIME) ? "§r§f§l» " : "") + new ColorGradient("Zeit übrig").withFrom(ChatColor.of("#D6D11C")).withTo(ChatColor.of("#C4BF1A")).withBold(sortierType.equals(SortierType.TIME)).execute(),
                        (sortierType.equals(SortierType.BEDROHUNG) ? "§r§f§l» " : "") + new ColorGradient("Größte Bedrohung").withFrom(ChatColor.of("#C4BF1A")).withTo(ChatColor.of("#B5B118")).withBold(sortierType.equals(SortierType.BEDROHUNG)).execute(),
                        "§7"
                )
                .create(46)
                .withItem("§7Inventar schließen")
                .thatHasMaterial(Material.OAK_DOOR)
                .thatAddsLore("§7", "§7Beim Klick auf dieses Item, wird", "§7das Inventar geschlossen.", "§7")
                .create(49)
                .withItem("§7Gespielte Kriege")
                .thatHasMaterial(Material.SKELETON_SKULL)
                .thatAddsLore("§7", "§7Beim Klick auf dieses Item, wird", "§7ein neues Inventar mit allen", "§7Informationen zu bereits gespielten" , "§7Kriegen geöffnet.")
                .create(52)
                .withItemStack(forward, 53)
                .withItemStack(back, 45)
                .withItem("§7")
                .thatHasMaterial(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                .create(new ForValue(0, 8), new ForValue(36, 44))
                .create();
        if(clan.getActiveKriege().size() < 27) {
            for (Krieg krieg : clan.getActiveKriege()) {
                Clan gegnerClan = krieg.getClan1().getUniqueId().equalsIgnoreCase(clan.getUniqueId()) ? krieg.getClan2() : krieg.getClan1();
                ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
                SkullMeta sm = (SkullMeta) stack.getItemMeta();
                sm.setDisplayName("§c§l" + gegnerClan.getClanname() + " §r§f[" + clan.getDisplayname().replace('&', '§') + "§r§f]");

                //TODO bei mitglieder also in der Lore muss noch gechekt werden ob die jeweiligen mitglieder tot bzw gebannt sind oder nicht weil gerade ist nur die ganz normale Mitgliederanzahl

                sm.setLore(Arrays.asList("§7",
                        "§fBedrohung: " + getColorFromBedrohung(getBedrohung(clan.getMitglieder().size(), gegnerClan.getMitglieder().size(), krieg.getKillsOfClanId(clan.getUniqueId()), krieg.getKillsOfClanId(gegnerClan.getUniqueId()))),
                        "§fLebende Gegner: §c" + gegnerClan.getMitglieder().size(),
                        "§fKills vom Gegner-Clan: §c" + krieg.getKillsOfClanId(gegnerClan.getUniqueId()),
                        "§fLebende Mitglieder: §e" + clan.getMitglieder().size(),
                        "§fKills von deinem Clan: §e" + krieg.getKillsOfClanId(clan.getUniqueId()),
                        "§fKrieg-Ende in: " + getRemainingTime(krieg.getTime()) + " §r§f/ §r" + getDate(krieg.getTime()),
                        "§7")
                );
                OfflinePlayer target = Bukkit.getOfflinePlayer(gegnerClan.getHost());
                if(target != null){
                    sm.setOwningPlayer(target);
                }
                stack.setItemMeta(sm);
                gui.addItem(stack);
            }
        }
        p.openInventory(gui);
    }

    public static String getRemainingTime(String time){
        Date date = DateUtil.stringToDate(time);
        DateTime dateTime = DateUtil.getRemaining(date);
        if(dateTime.getFormattedDays() > 0){
            return "§c" + dateTime.getFormattedDays() + "§7d §c" + dateTime.getFormattedHours() + "§7h §c" + dateTime.getFormattedMinutes() + "§7min §c" + dateTime.getFormattedSeconds() + "§7sec";
        } else if(dateTime.getFormattedHours() > 0){
            return "§c" + dateTime.getFormattedHours() + "§7h §c" + dateTime.getFormattedMinutes() + "§7min §c" + dateTime.getFormattedSeconds() + "§7sec";
        } else if(dateTime.getFormattedMinutes() > 0){
            return "§c" + dateTime.getFormattedMinutes() + "§7min §c" + dateTime.getFormattedSeconds() + "§7sec";
        } else if(dateTime.getFormattedSeconds() > 0){
            return "§c" + dateTime.getFormattedSeconds() + "§7sec";
        }
        return "§cpaar Sekunden";
    }

    public static String getDate(String time){
        Date date = DateUtil.stringToDate(time);
        return DateUtil.formatDate(date, "§c%D.%M.%Y %H:%MIN");
    }

    public static double getBedrohung(int team1Mitglieder, int team2Mitglieder, int team1Kills, int team2Kills) {
        double team1Bedrohung = team1Kills > 0 ? (double) team1Kills / team1Mitglieder : 0.0;
        double team2Bedrohung = team2Kills > 0 ? (double) team2Kills / team2Mitglieder : 0.0;

        double bedrohungProzent = team2Kills > 0 ? team2Bedrohung / (team1Bedrohung + team2Bedrohung) * 100 : 0.0;
        bedrohungProzent = Math.round(bedrohungProzent * 100.0) / 100.0;

        return bedrohungProzent;
    }

    public static org.bukkit.ChatColor getColorFromBedrohung(double bedrohung){
        if(bedrohung <= 25){
            return org.bukkit.ChatColor.GREEN;
        } else if(bedrohung <= 40){
            return org.bukkit.ChatColor.YELLOW;
        } else if(bedrohung <= 60){
            return org.bukkit.ChatColor.GOLD;
        } else if(bedrohung <= 80){
            return org.bukkit.ChatColor.RED;
        } else {
            return org.bukkit.ChatColor.DARK_RED;
        }
    }

    public static void setSortierType(Player p, SortierType type){
        Config config = ConfigManager.getPlayerConfig(p);
        config.set("KriegeGUI.SortierType", type.toString());
        config.save();
    }

    public static void switchToNextSortierType(Player p){
        SortierType current = getSortierType(p);
        SortierType toSet = SortierType.A_TO_Z;
        if(current.equals(SortierType.A_TO_Z)) toSet = SortierType.TIME;
        if(current.equals(SortierType.TIME)) toSet = SortierType.BEDROHUNG;
        Config config = ConfigManager.getPlayerConfig(p);
        config.set("KriegeGUI.SortierType", toSet.toString());
        config.save();
    }

    public static SortierType getSortierType(Player p){
        Config config = ConfigManager.getPlayerConfig(p);
        if(!config.contains("KriegeGUI.SortierType")){
            return SortierType.A_TO_Z;
        }
        return SortierType.valueOf(config.getString("KriegeGUI.SortierType"));
    }

}
