package de.marcey.hsmp.clansystem.kriegsystem;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.objects.enums.CustomSound;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.sounds.SoundProvider;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Krieg {
    private String clan1Id;
    private String clan2Id;

    private String time;
    private int kills_Clan1;
    private int kills_Clan2;
    private String uniqueId;
    public Krieg(Clan clan1, Clan clan2) {
        this.clan1Id = clan1.getUniqueId();
        this.clan2Id = clan2.getUniqueId();
        kills_Clan1 = 0;
        kills_Clan2 = 0;
        uniqueId = clan1.getUniqueId() + clan2.getUniqueId();
    }

    public int getKillsOfClanId(String uniqueId){
        if(getClan1().getUniqueId().equalsIgnoreCase(uniqueId)){
            return getKills_Clan1();
        } else if(getClan2().getUniqueId().equalsIgnoreCase(uniqueId)){
            return getKills_Clan2();
        }
        return 0;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setKills_Clan1(int kills_Clan1) {
        this.kills_Clan1 = kills_Clan1;
    }

    public void setKills_Clan2(int kills_Clan2) {
        this.kills_Clan2 = kills_Clan2;
    }

    public int getKills_Clan1() {
        return kills_Clan1;
    }

    public int getKills_Clan2() {
        return kills_Clan2;
    }

    public Clan getClan1() {
        return ClanProvider.getClanById(clan1Id);
    }

    public Clan getClan2() {
        return ClanProvider.getClanById(clan2Id);
    }

    private void sendMessage(Clan clan, String message){
        if(clan == null){
            return;
        }
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendMessage(message);
            }
        }
    }

    private void sendTitle(Clan clan, String title, String subtitle, int fadeIn, int stay, int fadeOut){
        if(clan == null){
            return;
        }
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        }
    }

    private void playSound(Clan clan, Sound sound, int volume, int pitch){
        if(clan == null){
            return;
        }
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                current.playSound(current.getLocation(), sound, volume, pitch);
            }
        }
    }

    public List<Player> getOnlinePlayers(Clan clan){
        List<Player> list = new ArrayList<>();
        if(clan == null){
            return list;
        }
        for(Player current : Bukkit.getOnlinePlayers()){
            if(clan.getMitglieder().contains(current.getUniqueId())){
                list.add(current);
            }
        }
        return list;
    }
}
