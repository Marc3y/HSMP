package de.marcey.hsmp.commands;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.teleportsystem.TeleportProvider;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(TeleportProvider.isInTeleportVorgang(p)){
            p.sendMessage(Strings.prefix + " §cEs läuft bereits ein Teleport-Vorgang.");
            return false;
        }
        if(TeleportProvider.isOnCooldown(p)){
            p.sendMessage(Strings.prefix + " §cDu hast dich erst vor kurzem teleportiert. Bitte warte ein wenig, bevor du dich nochmal teleportierst.");
            return false;
        }
        TeleportProvider.teleport(p, HSMP.getSpawn(), 5, true,Strings.prefix + " §7Du hast dich erfolgreich zum Spawn §ateleportiert§7.", true, 60);
        return false;
    }
}
