package de.marcey.hsmp.commands;

import de.marcey.hsmp.listener.SitListener;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if(!(s instanceof Player)){
            return false;
        }
        Player p = (Player) s;
        if(!SitListener.sit.containsKey(p)){
            if(!p.isOnGround() || p.isSwimming() || p.isGliding() || p.isFlying() || p.isSleeping() || p.isFrozen() || p.isInWater()){
                p.sendMessage(Strings.prefix + " §cDu musst dafür auf einem stabilen Boden sein.");
                return false;
            }
            SitListener.sit(p, p.getLocation());
        } else p.sendMessage(Strings.prefix + " §cDu kannst gerade nicht sitzen!");
        return false;
    }
}
