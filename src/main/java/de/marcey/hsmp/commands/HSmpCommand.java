package de.marcey.hsmp.commands;

import de.marcey.hsmp.clansystem.ChunkProvider;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import de.marcey.hsmp.listener.SpawnListener;
import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.teleportsystem.TeleportProvider;
import de.marcey.hsmp.utils.data.Locs;
import de.marcey.hsmp.utils.ParticleProvider;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class HSmpCommand implements CommandExecutor {

    public static boolean build = false;
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player)){
            return false;
        }

        Player p = (Player) s;
        if(!p.hasPermission("HSMP.Admin")){
            p.sendMessage(Strings.prefix + " §cDazu hast du keine Rechte.");
            return false;
        }

        Config config = new Config("Locations");

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("krieg")){
                Krieg krieg = new Krieg(ClanProvider.getClan(p), ClanProvider.getClanByClanname("okay"));
            }
            if(args[0].equalsIgnoreCase("setspawn")){
                Locs.save(config, "Spawn", p.getLocation());
                config.getConfig().set("Worldname", p.getLocation().getWorld().getName());
                config.save();
                p.sendMessage(Strings.prefix + " §7Du hast den Spawn §agesetzt§7.");
            }
            if(args[0].equalsIgnoreCase("build")){
                build = !build;
                p.sendMessage(Strings.prefix + " §7Building für Operator's ist nun auf §e" + build + "§7.");
            }
            if(args[0].equalsIgnoreCase("spawntest")){
                p.sendMessage("du bist beim spawn: " + SpawnListener.isInSpawn(p) + " und " + SpawnListener.isInSpawn(p.getLocation()));
            }
            if(args[0].equalsIgnoreCase("chunks")){
                p.sendMessage(ChunkProvider.gegnerchunks.get(ClanProvider.getClan(p).getUniqueId()).isEmpty() + "");
                for(String l : ChunkProvider.gegnerchunks.get(ClanProvider.getClan(p).getUniqueId())){
                    p.sendMessage(l);
                }
            }
            if(args[0].equalsIgnoreCase("chunksnoclan")){
                for(String l : ChunkProvider.gegnerchunksall){
                    p.sendMessage(l);
                }
            }
            if(args[0].equalsIgnoreCase("clearcooldown")){
                TeleportProvider.removeCooldown(p);
                p.sendMessage(Strings.prefix + " §7Cooldown entfernt.");
            }
            if(args[0].equalsIgnoreCase("partikel")){
                ParticleProvider.spawnCircle(p, p.getLocation());
                p.sendMessage(Strings.prefix + " §7Partikel gespawnt.");
            }
            if(args[0].equalsIgnoreCase("invites")){
                for(Map.Entry<UUID, ArrayList<UUID>> entry : ClanProvider.anfrage.entrySet()){
                    String stuff = "";
                    for(UUID a : entry.getValue()){
                        stuff = stuff + " " + a;
                    }
                    p.sendMessage(entry.getKey() + ": " + stuff);
                }
            }

        }
        if(args.length == 4){
            if(args[0].equalsIgnoreCase("sound")){
                String sound = args[1];
                int volume = Integer.parseInt(args[2]);
                int pitch = Integer.parseInt(args[3]);
                p.playSound(p.getLocation(), sound, volume, pitch);
                p.sendMessage("okay");
            }
        }

        return false;
    }
}
