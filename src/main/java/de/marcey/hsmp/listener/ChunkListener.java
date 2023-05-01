package de.marcey.hsmp.listener;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.objects.ChunkBorder;
import de.marcey.hsmp.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChunkListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
    }

    public static List<Player> isUsing = new ArrayList<>();

    public static void spawnParticlesWholeChunk(Player p, Location loc, Color color, int minY){
        int chunkX = loc.getBlockX() >> 4;
        int chunkZ = loc.getBlockZ() >> 4;

        double startX = (chunkX << 4) + 0.5;
        double startZ = (chunkZ << 4) + 0.5;

        HashMap<Integer, List<Location>> list = new HashMap<>();

        for (int i = 0; i < 16; i++) {
            double x = startX + i;
            double z1 = startZ;
            double z2 = startZ + 15;

            for (int y = 0; y < 256; y++) {
                Location particleLoc1 = new Location(loc.getWorld(), x, y, z1);
                Location particleLoc2 = new Location(loc.getWorld(), x, y, z2);
                List<Location> list1 = list.get(y) != null ? list.get(y) : new ArrayList<>();
                list1.add(particleLoc1);
                list1.add(particleLoc2);
                list.put(y, list1);
            }

        }

        for (int i = 1; i < 15; i++) {
            double z = startZ + i;
            double x1 = startX;
            double x2 = startX + 15;
            for (int y = 0; y < 256; y++) {
                Location particleLoc1 = new Location(loc.getWorld(), x1, y, z);
                Location particleLoc2 = new Location(loc.getWorld(), x2, y, z);
                List<Location> list1 = list.get(y) != null ? list.get(y) : new ArrayList<>();
                list1.add(particleLoc1);
                list1.add(particleLoc2);
                list.put(y, list1);
            }
        }

        if(!isUsing.contains(p)){
            isUsing.add(p);
        } else {
            return;
        }


        if(list == null){
            return;
        }
        if(p.getLocation().getY() < 250) {

            new BukkitRunnable() {
                int y = Math.min((int) (p.getLocation().getY() - minY), 255);
                @Override
                public void run() {
                    if (isUsing.isEmpty()) {
                        this.cancel();
                    }
                    if (isUsing.contains(p)) {
                        if (list.get(y) != null) {
                            for (int i = 0; i < list.get(y).size(); i++) {
                                try {
                                    spawnDustParticles(p, list.get(y).get(i), color, 5);
                                } catch (Exception ignored) {
                                }
                            }
                            if (y == (int) (p.getLocation().getY() <= 200 ? p.getLocation().getY() + 20 : 255)) {
                                isUsing.remove(p);
                                this.cancel();
                            }
                            if(y >= 400){
                                isUsing.remove(p);
                                this.cancel();
                            }
                            y++;
                        }
                    } else this.cancel();
                }
            }.runTaskTimer(HSMP.getInstance(), 0, 1);
        }
    }

    public static void spawnDustParticles(Player player, Location location, Color color, int count) {
        player.spawnParticle(Particle.REDSTONE, location, count, 0, 0, 0, 0, new Particle.DustOptions(color, 1));
    }

}
