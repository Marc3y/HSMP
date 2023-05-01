package de.marcey.hsmp.player.gui;

import de.marcey.hsmp.apis.gui.Gui;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.player.PlayerSettings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PlayerSettingsGUI {

    public static void openGUI(Player p){

        List<Integer> enabled = new ArrayList<>();
        List<Integer> disabled = new ArrayList<>();

        PlayerSettings settings = new PlayerSettings(p);
        if(settings.isMusicEnabled()){
            enabled.add(20);
        } else disabled.add(20);
        if(settings.isMsgsEnabled()){
            enabled.add(21);
        } else disabled.add(21);
        if(settings.isChatEnabled()){
            enabled.add(22);
        } else disabled.add(22);
        if(settings.isStatsPublic()){
            enabled.add(23);
        } else disabled.add(23);
        if(settings.isStreamNotificationsEnabled()){
            enabled.add(24);
        } else disabled.add(24);

        Inventory inv = new Gui()
                .withDisplayname(Rang.getRang(p).getColor() + p.getName() + " §r§8| §8Settings")
                .withSize(4*9)

                .withItem("§eCustom Musik/Sounds")
                .thatHasMaterial(Material.NOTE_BLOCK)
                .thatAddsLore("§7", "§7Wenn §cCustom Musik/Sounds §7aktiviert sind,", "§7werden mit mehr Sounds und Musik gearbeitet.", "§7Dies erhöht den Vibe des Spiel's.", "§7")
                .create(11)

                .withItem("§eMSGs")
                .thatHasMaterial(Material.WRITABLE_BOOK)
                .thatAddsLore("§7", "§7Wenn §eMSGs §7aktiviert sind,", "§7können Spieler, dir eine MSG mit,", "§f/msg §7schreiben.", "§7")
                .create(12)

                .withItem("§eChat")
                .thatHasMaterial(Material.PAPER)
                .thatAddsLore("§7", "§7Wenn §eChat §7aktiviert ist,",  "§7kannst du den Chat sehen und", "§7Nachrichten in den Chat schreiben. ", "§7")
                .create(13)

                .withItem("§eStats-Veröffentlichung")
                .thatHasMaterial(Material.GRASS_BLOCK)
                .thatAddsLore("§7", "§7Wenn die §eStats-Veröffentlichung §7aktiviert ist,", "§7kann jeder Spieler deine", "§7Statistiken anschauen.", "§7")
                .create(14)

                .withItem("§eStream-Notifications")
                .thatHasMaterial(Material.BELL)
                .thatAddsLore("§7", "§7Wenn §eStream-Notifications §7aktiviert sind,", "§7wirst du im Chat benachrichtigt", "§7wenn Kenjih oder Tjan auf", "§7Twitch live gehen.", "§7")
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
        p.openInventory(inv);

    }

}
