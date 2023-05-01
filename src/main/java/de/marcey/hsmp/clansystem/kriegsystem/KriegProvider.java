package de.marcey.hsmp.clansystem.kriegsystem;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.objects.enums.CustomSound;
import de.marcey.hsmp.objects.enums.TimeSymbol;
import de.marcey.hsmp.utils.DateUtil;
import de.marcey.hsmp.utils.Strings;
import de.marcey.hsmp.utils.data.krieg.KriegManager;
import de.marcey.hsmp.utils.data.player.PlayerInfo;
import de.marcey.hsmp.utils.sounds.SoundProvider;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class KriegProvider {

    private static KriegManager manager = HSMP.getKriegManager();

    public static Krieg getKrieg(Clan clan1, Clan clan2){
        Krieg krieg1 = manager.getKriegById(clan1.getUniqueId() + clan2.getUniqueId());
        return krieg1 != null ? krieg1 : manager.getKriegById(clan2.getUniqueId() + clan1.getUniqueId());
    }

    public static void inviteToKrieg(Player inviter, Clan clanToInvite){
        Clan inviterClan = ClanProvider.getClan(inviter);
        if(inviterClan == null){
            inviter.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            inviter.playSound(inviter.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
            return;
        }
        if(!inviterClan.getHost().equals(inviter.getUniqueId())){
            inviter.sendMessage(Strings.prefix + " §cNur der Clan-Host kann Kriege starten.");
            inviter.playSound(inviter.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
            return;
        }
        if(clanToInvite == null) {
            inviter.sendMessage(Strings.prefix + " §cDer Clan, den du zum Krieg herausfordern willst, existiert nicht.");
            inviter.playSound(inviter.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
            return;
        }
        if(manager.isInKrieg(inviterClan, clanToInvite)){
            inviter.sendMessage(Strings.prefix + " §cDu bist bereits mit dem Clan §e" + clanToInvite.getClanname() + " §cin einem Krieg.");
            inviter.playSound(inviter.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
            return;
        }
        if(hasAlreadyRequested(inviterClan, clanToInvite)){
            inviter.sendMessage(Strings.prefix + " §cDu hast bereits diesen Clan zum Krieg herausgefordert.");
            inviter.playSound(inviter.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
            return;
        }

        addKriegRequest(inviterClan, clanToInvite);

        for(Player current : Bukkit.getOnlinePlayers()){
            if(!clanToInvite.getMitglieder().contains(current.getUniqueId())) continue;
            current.sendMessage(Strings.prefix + " §cDein Clan hat eine Herausforderung zum Krieg von dem Clan §e" + inviterClan.getClanname() + " §eerhalten.");
            current.sendMessage(Strings.prefix + " §7Der Clan-Leader kann dies mit §c/krieg <accept/deny> " + inviterClan.getClanname() + " §7akzeptieren/ablehnen.");
            current.playSound(current.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 10, 0);
        }
    }

    public static void acceptKriegRequest(Player accepter, Clan clanToAccept){
        Clan accepterClan = ClanProvider.getClan(accepter);
        if(accepterClan == null){
            accepter.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(clanToAccept == null){
            accepter.sendMessage(Strings.prefix + " §cDer Clan existiert nicht.");
            return;
        }
        if(!accepterClan.getHost().equals(accepter.getUniqueId())){
            accepter.sendMessage(Strings.prefix + " §cNur der Host kann Kriege akzeptieren.");
            return;
        }
        if(!hasAlreadyRequested(clanToAccept, accepterClan)){
            accepter.sendMessage(Strings.prefix + " §cDu hast keine Krieg-Herausforderung von diesem Clan.");
            return;
        }

        Krieg krieg = new Krieg(clanToAccept, accepterClan);
        Date endDate = DateUtil.getTime(3, TimeSymbol.DAYS);
        krieg.setTime(DateUtil.dateToString(endDate));
        krieg.setKills_Clan2(0);
        krieg.setKills_Clan1(0);
        manager.createNewKrieg(krieg);

        for(Player current : Bukkit.getOnlinePlayers()){
            if(clanToAccept.getMitglieder().contains(current.getUniqueId()) || accepterClan.getMitglieder().contains(current.getUniqueId())) {
                SoundProvider.playSoundForce(current, CustomSound.WAR_START, 25, 1);
                SoundProvider.playSoundForce(current, CustomSound.WAR_START_MUSIC, 0.5F, 1);
                current.sendTitle("§c§lKrieg", "§7mit dem Clan §e" + (clanToAccept.getMitglieder().contains(current.getUniqueId()) ? accepterClan.getClanname() : clanToAccept.getClanname()), 65, 100, 60);
            }
            current.sendMessage(Strings.prefix + " §e§lDer Clan §6§l" + clanToAccept.getClanname() + " §e§lführt nun §c§lKrieg §e§lmit §6§l" + accepterClan.getClanname());
        }
    }

    public static void denyKriegRequest(Player accepter, Clan clanToAccept){
        Clan denyClan = ClanProvider.getClan(accepter);
        if(denyClan == null){
            accepter.sendMessage(Strings.prefix + " §cDu bist in keinem Clan.");
            return;
        }
        if(clanToAccept == null){
            accepter.sendMessage(Strings.prefix + " §cDer Clan existiert nicht.");
            return;
        }
        if(!denyClan.getHost().equals(accepter.getUniqueId())){
            accepter.sendMessage(Strings.prefix + " §cNur der Host kann Kriege ablehnen.");
            return;
        }
        if(!hasAlreadyRequested(clanToAccept, denyClan)){
            accepter.sendMessage(Strings.prefix + " §cDu hast keine Krieg-Herausforderung von diesem Clan.");
            return;
        }

        removeKriegRequest(clanToAccept, denyClan);

        for(Player current : Bukkit.getOnlinePlayers()){
            if(clanToAccept.getMitglieder().contains(current.getUniqueId())) {
                current.sendMessage(Strings.prefix + " §7Der Clan §e" + denyClan.getClanname() + " §7hat den Krieg §cabgelehnt§7.");
                current.playSound(current.getLocation(), Sound.ENTITY_VILLAGER_NO, 20, 1);
            } else if(denyClan.getMitglieder().contains(current.getUniqueId())) {
                current.sendMessage(Strings.prefix + " §7Der Clan-Leader hat die Krieg-Anfrage von §e" + clanToAccept.getClanname() + " §cabgelehnt§7.");
                current.playSound(current.getLocation(), Sound.ENTITY_VILLAGER_NO, 20, 1);
            }
        }
    }


    private static HashMap<String, ArrayList<String>> requests = new HashMap<String, ArrayList<String>>();

    private static boolean hasAlreadyRequested(Clan requester, Clan clanTo){
        if(requests.containsKey(clanTo.getUniqueId())){
            return requests.get(clanTo.getUniqueId()).contains(requester.getUniqueId());
        }
        return false;
    }
    private static void removeKriegRequest(Clan requester, Clan to){
        requests.get(to.getUniqueId()).remove(requester.getUniqueId());
    }

    private static void addKriegRequest(Clan requester, Clan clanTo){
        if(requests.containsKey(clanTo.getUniqueId())){
            requests.get(clanTo.getUniqueId()).add(requester.getUniqueId());
        } else {
            ArrayList<String> request = new ArrayList<String>();
            request.add(requester.getUniqueId());
            requests.put(clanTo.getUniqueId(), request);
        }
    }

}
