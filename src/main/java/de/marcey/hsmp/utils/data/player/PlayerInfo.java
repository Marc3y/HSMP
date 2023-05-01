package de.marcey.hsmp.utils.data.player;

import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerInfo {

    private Player player;
    private UUID uuid;
    private Clan clan;
    private String clanId;
    private List<Krieg> kriege;

    public PlayerInfo(Player p){
        this.player = p;
        this.uuid = p.getUniqueId();
        this.clan = ClanProvider.getClan(this.uuid);
        this.kriege = new ArrayList<>();
        if(this.clan != null) {
            this.clanId = this.clan.getUniqueId();
            for(Krieg krieg : HSMP.getKriegManager().getAllKriege()){
                if(krieg.getClan1().getUniqueId().equalsIgnoreCase(this.clanId) || krieg.getClan2().getUniqueId().equalsIgnoreCase(this.clanId)){
                    if(kriege.contains(krieg)) continue;
                    kriege.add(krieg);
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Clan getClan() {
        return clan;
    }

    public String getClanId() {
        return clanId;
    }

    public List<Krieg> getKriege() {
        return kriege;
    }
}
