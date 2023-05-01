package de.marcey.hsmp.clansystem;

import de.marcey.hsmp.HSMP;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChunkProvider {

    private static String format = "%d,%d,%d";

    //ClanId, List<String> chunkids von den gegnern
    public static HashMap<String, List<String>> gegnerchunks = new HashMap<>();
    public static List<String> gegnerchunksall = new ArrayList<>();

    public static String getChunkId(Location loc){
        Chunk chunk = loc.getChunk();
        int chunkX = chunk.getX();
        int chunkY = 0;
        int chunkZ = chunk.getZ();
        return String.format(format, chunkX, chunkY, chunkZ);
    }

    public static boolean isInChunk(Location loc, String chunkId){
        if(String.format(format, loc.getChunk().getX(), 0, loc.getChunk().getZ()).equalsIgnoreCase(chunkId)){
            return true;
        } else return false;
    }

    public static boolean isInGegnerChunk(Clan clan, Location loc){
        String id = getChunkId(loc);
        if(clan != null) {
            for (String ids : gegnerchunks.get(clan.getUniqueId())) {
                if (!ids.isEmpty()) {
                    if (ids.equalsIgnoreCase(id)) {
                        return true;
                    }
                }
            }
        } else {
            for(String ids : gegnerchunksall){
                if(ids.contains(id)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInOwnChunk(Clan clan, Player p){
        String id = getChunkId(p.getLocation());
        if(clan.getChunkIdsAsList().contains(id)){
            return true;
        } else return false;
    }
    public static boolean isInOwnChunk(Clan clan, Location loc){
        String id = getChunkId(loc);
        if(clan.getChunkIdsAsList().contains(id)){
            return true;
        } else return false;
    }

    public static void init(){
        gegnerchunks.clear();
        gegnerchunksall.clear();
        for(Clan clan : HSMP.getClans().getAllClans()){
            List<String> list = new ArrayList<>();
            for(Clan c : HSMP.getClans().getAllClans()){
                if(!c.getUniqueId().equalsIgnoreCase(clan.getUniqueId())){
                    list.addAll(c.getChunkIdsAsList());
                }
            }
            gegnerchunksall.addAll(clan.getChunkIdsAsList());
            gegnerchunks.put(clan.getUniqueId(), list);
        }
    }

}
