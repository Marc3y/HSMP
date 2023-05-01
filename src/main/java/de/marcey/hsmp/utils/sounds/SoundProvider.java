package de.marcey.hsmp.utils.sounds;

import de.marcey.hsmp.objects.enums.CustomSound;
import de.marcey.hsmp.player.PlayerSettings;
import de.marcey.hsmp.player.gui.PlayerSettingsGUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundProvider {

    public static void playSound(Player p, CustomSound sound, float volume, float pitch){
        PlayerSettings settings = new PlayerSettings(p);
        if(settings.isMusicEnabled()) {
            p.playSound(p.getLocation(), "custom:" + sound.toString().toLowerCase(), volume, pitch);
        }
    }
    public static void playSound(Player p, CustomSound sound, Sound alternativeSound, float volume, float pitch){
        PlayerSettings settings = new PlayerSettings(p);
        if(settings.isMusicEnabled()) {
            p.playSound(p.getLocation(), "custom:" + sound.toString().toLowerCase(), volume, pitch);
        } else p.playSound(p.getLocation(), alternativeSound, volume, pitch);
    }

    public static void playSoundForce(Player p, CustomSound sound, float volume, float pitch){
        PlayerSettings settings = new PlayerSettings(p);
        p.playSound(p.getLocation(), "custom:" + sound.toString().toLowerCase(), volume, pitch);
    }


    public static void stopSound(Player p, CustomSound sound){
        p.stopSound(sound.toString());
    }

    public static void stopSounds(Player p){
        p.stopAllSounds();
    }

}
