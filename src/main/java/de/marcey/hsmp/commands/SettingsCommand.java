package de.marcey.hsmp.commands;

import de.marcey.hsmp.player.gui.PlayerSettingsGUI;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        PlayerSettingsGUI.openGUI(p);
        p.sendMessage(Strings.prefix + " §7Du hast die Einstellungen §ageöffnet§7.");

        return false;
    }
}
