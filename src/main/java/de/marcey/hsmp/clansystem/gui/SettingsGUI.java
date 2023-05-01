package de.marcey.hsmp.clansystem.gui;

import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.apis.gui.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SettingsGUI {

    private static String CLAN_CHAT_ABKÜRZUNG = "@Clan";

    public static void openGUI(Player p){
        Clan clan = ClanProvider.getClan(p);

        List<Integer> enabled = new ArrayList<>();
        List<Integer> disabled = new ArrayList<>();

        if(clan.isClanChatEnabled()){
            enabled.add(20);
        } else disabled.add(20);
        if(clan.isJoinRequestsEnabled()){
            enabled.add(21);
        } else disabled.add(21);
        if(clan.isStatsForClanMembersEnabled()){
            enabled.add(22);
        } else disabled.add(22);
        if(clan.isStatsPublic()){
            enabled.add(23);
        } else disabled.add(23);
        if(clan.isNormalMembersAllowedToInvitePlayers()){
            enabled.add(24);
        } else disabled.add(24);

        Inventory gui = new Gui()
                .withDisplayname("§8Settings | " + clan.getClanname())
                .withSize(4*9)

                .withItem("§eClan-Chat")
                .thatHasMaterial(Material.WRITABLE_BOOK)
                .thatAddsLore("§7", "§7Wenn der §eClan-Chat §7aktiviert ist,", "§7können Clan-Member mit", "§7@Clan (am Anfang der Nachricht)", "§7Nachrichten nur an den Clan schreiben.", "§7")
                .create(11)

                .withItem("§eJoin-Requests")
                .thatHasMaterial(Material.PAPER)
                .thatAddsLore("§7", "§7Wenn §eJoin-Requests §7aktiviert sind,", "§7können Spieler, die nicht im Clan sind,", "§7Anfragen zum Beitreten des Clan's schicken.", "§7")
                .create(12)

                .withItem("§eStats-Sichtbarkeit für Clan-Member")
                .thatHasMaterial(Material.DIAMOND_SWORD)
                .thatAddsLore("§7", "§7Wenn die §eStats-Sichtbarkeit für Clan-Member",  "§7aktiviert ist,", "§7kann jeder Clan-Member die ", "§7Statistiken des Clan's anschauen.", "§7Ansonsten können dies nur Clan-OP's.", "§7")
                .create(13)

                .withItem("§eStatistik-Veröffentlichung")
                .thatHasMaterial(Material.GRASS_BLOCK)
                .thatAddsLore("§7", "§7Wenn die §eStatistik-Veröffentlichung §7aktiviert ist,", "§7kann jeder Spieler (also auch Spieler, die", "§7nicht im Clan sind) die Statistiken dieses", "§7Clan's angucken.", "§7")
                .create(14)

                .withItem("§eMember-Einladungen")
                .thatHasMaterial(Material.NAME_TAG)
                .thatAddsLore("§7", "§7Wenn §eMember-Einladungen §7aktiviert sind,", "§7kann jeder Clan-Member Einladungen zum", "§7Beitreten des Clan's verschicken.", "§7Ansonsten können dies nur Clan-OP's.", "§7")
                .create(15)

                .withItem("§7Status: §a§lAktiviert")
                .thatHasMaterial(Material.LIME_CONCRETE)
                .thatAddsLore("§7")
                .create(enabled)

                .withItem("§7Status: §c§lDeaktiviert")
                .thatHasMaterial(Material.RED_CONCRETE)
                .thatAddsLore("§7")
                .create(disabled)

                .create();

        p.openInventory(gui);
    }

}
