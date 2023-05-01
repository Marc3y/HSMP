package de.marcey.hsmp.utils;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.listener.ChunkListener;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ParticleProvider {

    public static void spawnCircle(Player p, Location l){
        new BukkitRunnable(){
            org.bukkit.Location loc = l;
            double t = 0;
            double r = 1;
            @Override
            public void run() {
                t = t + Math.PI/8;
                double x = r*cos(t);
                double y = Math.min(t/2, 2);
                double z = r*sin(t);
                loc.add(x, y, z);
                ChunkListener.spawnDustParticles(p, l, Color.AQUA, 5);
                loc.subtract(x, y, z);
                if(t > Math.PI*4){
                    this.cancel();
                }
            }
        }.runTaskTimer(HSMP.getInstance(), 0, 1);
    }

}
