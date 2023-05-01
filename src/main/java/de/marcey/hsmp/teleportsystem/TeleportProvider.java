package de.marcey.hsmp.teleportsystem;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.utils.ParticleProvider;
import de.marcey.hsmp.utils.Strings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeleportProvider {
    private static List<Player> taskUsing = new ArrayList<>();
    private static List<UUID> cooldownUsing = new ArrayList<>();

    public static void teleport(Player p, Location loc, int seconds, boolean withMessage, String teleportedMessage, boolean withCooldown, int cooldownSeconds){
        taskUsing.add(p);
        p.sendMessage(Strings.prefix + " §7Du wirst in §e" + seconds + " §7Sekunden teleportiert. Bitte bewege dich in dieser Zeit §cnicht§7.");

        new BukkitRunnable(){

            int s = seconds;
            int numer = 1;
            @Override
            public void run() {
                if(taskUsing.isEmpty()){
                    this.cancel();
                }

                if(taskUsing.contains(p)){

                    String points = "";
                    for(int i = 0; i < numer; i++){
                        points = points + ".";
                    }
                    numer = numer <= 2 ? (numer+1) : 1;

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6§lHardcore§e§lSMP §8>> §r§7Teleport-Vorgang läuft" + points));
                    if(s == 0){
                        if(taskUsing.contains(p)) {
                            if(withMessage) {
                                p.sendMessage(teleportedMessage);
                            }
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6§lHardcore§e§lSMP §8>> §r§7Du wurdest §ateleportiert§7."));
                            p.teleport(loc);
                            ParticleProvider.spawnCircle(p, p.getLocation());
                            if(withCooldown){
                                cooldown(p.getUniqueId(), cooldownSeconds);
                            }
                        }
                        taskUsing.remove(p);
                        this.cancel();
                    }
                    if(s < 0){
                        this.cancel();
                    }
                    s--;
                } else this.cancel();
            }
        }.runTaskTimer(HSMP.getInstance(), 0, 20);
    }

    private static void cooldown(UUID uuid, int seconds){
        cooldownUsing.add(uuid);

        new BukkitRunnable() {
            int s = seconds;
            @Override
            public void run() {

                if(cooldownUsing.contains(uuid)){
                    if(s <= 0){
                        cooldownUsing.remove(uuid);
                        this.cancel();
                    }
                    s--;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(HSMP.getInstance(), 0, 20);
    }

    public static boolean isOnCooldown(Player p){
        return cooldownUsing.contains(p.getUniqueId());
    }

    public static void removeCooldown(Player p){
        cooldownUsing.remove(p.getUniqueId());
    }

    public static boolean isInTeleportVorgang(Player p){
        return taskUsing.contains(p);
    }

    public static void removeTeleportVorgang(Player p){
        taskUsing.remove(p);
    }

}
