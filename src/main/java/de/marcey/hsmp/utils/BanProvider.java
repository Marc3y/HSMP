package de.marcey.hsmp.utils;

import de.marcey.hsmp.utils.config.Config;
import de.marcey.hsmp.objects.Rang;
import de.marcey.hsmp.objects.enums.PlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanProvider {

    private static Config cfg = new Config("Moderation");
    private static FileConfiguration config = cfg.getConfig();
    //TODO change messages
    private static String banMessage = "Du wurdest gebannt";
    private static String publicBanMessage = Strings.prefix + " §cDer Spieler {Player} §r§cwurde permanent vom HSMP gebannt.";
    private static String muteMessage = Strings.prefix + " §cDu wurdest PERMANENT aus dem HSMP-Chat verbannt!";
    private static String publicMuteMessage = Strings.prefix + " §cDer Spieler {Player} §r§cwurde permanent aus dem HSMP-Chat verbannt.";


    public static void ban(UUID uuid, boolean notifyAllPlayers){
        PlayerStatus status = PlayerStatus.PERMABANNED;
        Player target = Bukkit.getPlayer(uuid);
        if(target != null){
            target.kickPlayer(banMessage);
        }
        List<String> list = new ArrayList<>();
        if(config.getStringList(uuid + ".Status") != null){
            list = config.getStringList(uuid + ".Status");
            if(!list.contains(status.toString())) {
                list.add(status.toString());
            }
        } else {
            list.add(status.toString());
        }
        config.set(uuid + ".Status", list);
        cfg.save();

        if(notifyAllPlayers){
            if(target != null){
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(publicBanMessage.replaceAll("\\{Player}", Rang.getRang(target).getFormattedDisplayname() + target.getName()));
                });
            } else {
                OfflinePlayer targetOf = Bukkit.getOfflinePlayer(uuid);
                if(targetOf != null){
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(publicBanMessage.replaceAll("\\{Player}", Rang.getRang(targetOf.getUniqueId()).getFormattedDisplayname() + targetOf.getName()));
                    });
                }
            }
        }
    }

    public static void mute(UUID uuid, boolean notifyAllPlayers){
        PlayerStatus status = PlayerStatus.PERMAMUTED;
        Player target = Bukkit.getPlayer(uuid);
        if(target != null){
            target.sendMessage();
        }
        List<String> list = new ArrayList<>();
        if(config.getStringList(uuid + ".Status") != null){
            list = config.getStringList(uuid + ".Status");
            if(!list.contains(status.toString())) {
                list.add(status.toString());
            }
        } else {
            list.add(status.toString());
        }
        config.set(uuid + ".Status", list);
        cfg.save();

        if(notifyAllPlayers){
            if(target != null){
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(publicMuteMessage.replaceAll("\\{Player}", Rang.getRang(target).getFormattedDisplayname() + target.getName()));
                });
            } else {
                OfflinePlayer targetOf = Bukkit.getOfflinePlayer(uuid);
                if(targetOf != null){
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(publicMuteMessage.replaceAll("\\{Player}", Rang.getRang(targetOf.getUniqueId()).getFormattedDisplayname() + targetOf.getName()));
                    });
                }
            }
        }
    }

}
