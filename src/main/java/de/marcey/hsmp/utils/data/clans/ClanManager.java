package de.marcey.hsmp.utils.data.clans;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.utils.mongodb.MongoDB;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanManager {

    //ID, CLANNAME, DISPLAYNAME, MITGLIEDER, OPS, CHUNKS, HOST, STATS

    private static MongoDB mongoDB = HSMP.getMongodb();


    public boolean isInClan(UUID uuid){
        for(Clan clan : getAllClans()){
            if(clan.getMitglieder().contains(uuid)){
                return true;
            }
        }
        return false;
    }

    public void setClan(Clan clan){
        if(isClanIdExists(clan.getUniqueId())){
            Bson updates = Updates.combine(
                    Updates.set("uniqueid", clan.getUniqueId()),
                    Updates.set("clanname", clan.getClanname()),
                    Updates.set("displayname", clan.getDisplayname()),
                    Updates.set("mitglieder", clan.getMitglieder()),
                    Updates.set("ops", clan.getOps()),
                    Updates.set("chunks", clan.getChunks()),
                    Updates.set("host", clan.getHost().toString()),
                    Updates.set("chunkids", clan.getChunkIdsAsList()),
                    Updates.set("stats", clan.getStats())
            );
            Document search = mongoDB.find("clans", "uniqueid", clan.getUniqueId());
            mongoDB.update("clans", search, updates);
        } else {
            Document document = new Document();
            document.append("uniqueid", clan.getUniqueId());
            document.append("clanname", clan.getClanname());
            document.append("displayname", clan.getDisplayname());
            document.append("mitglieder", clan.getMitglieder());
            document.append("ops", clan.getOps());
            document.append("chunks", clan.getChunks());
            document.append("host", clan.getHost().toString());
            document.append("chunkids", clan.getChunkIdsAsList());
            document.append("stats", clan.getStats());
            mongoDB.set("clans", document);
        }
    }

    public List<UUID> getAllMitglieder(String id) {
        List<UUID> list = new ArrayList<>();
        if(isClanIdExists(id)){
            list = getClan(id).getMitglieder();
        }
        return list;
    }

    public Clan getClan(String id){
        Document doc = mongoDB.find("clans", "uniqueid", id);
        if(doc == null){
            return null;
        }
        Clan clan = new Clan();
        clan.setUniqueId(doc.getString("uniqueid"));
        clan.setClanname(doc.getString("clanname"));
        clan.setDisplayname(doc.getString("displayname"));
        clan.setMitglieder(doc.getList("mitglieder", UUID.class));
        clan.setOps(doc.getList("ops", UUID.class));
        clan.setChunks(doc.getInteger("chunks"));
        clan.setHost(UUID.fromString(doc.getString("host")));
        clan.setStats(doc.getString("stats"));
        clan.setChunkIdsFromList(doc.getList("chunkids", String.class));
        return clan;
    }

    public List<String> getAllChunkIds(String id) {
        List<String> list = new ArrayList<>();
        if(isClanIdExists(id)){
            return getClan(id).getChunkIdsAsList();
        }
        return list;
    }


    public boolean isClanIdExists(String id){
        return getClan(id) != null;
    }

    public List<Clan> getAllClans(){
        List<Clan> list = new ArrayList<>();
        if(mongoDB.getCollection("clans").find().first() != null) {
            for (Document doc : mongoDB.getCollection("clans").find()) {
                Clan clan = new Clan();
                clan.setUniqueId(doc.getString("uniqueid"));
                clan.setClanname(doc.getString("clanname"));
                clan.setDisplayname(doc.getString("displayname"));
                clan.setMitglieder(doc.getList("mitglieder", UUID.class));
                clan.setOps(doc.getList("ops", UUID.class));
                clan.setChunks(doc.getInteger("chunks"));
                clan.setHost(UUID.fromString(doc.getString("host")));
                clan.setStats(doc.getString("stats"));
                clan.setChunkIdsFromList(doc.getList("chunkids", String.class));
                list.add(clan);
            }
        }
        return list;
    }

    public UUID getHost(String id) {
        if(isClanIdExists(id)){
            return getClan(id).getHost();
        }
        return null;
    }


    public int getChunks(String id) {
        if(isClanIdExists(id)){
            return getClan(id).getChunks();
        }
        return 0;
    }

    public String getDisplayname(String id) {
        if(isClanIdExists(id)){
            return getClan(id).getDisplayname();
        }
        return null;
    }

    public String getStats(String id) {
        if(isClanIdExists(id)){
            return getClan(id).getStats();
        }
        return null;
    }

    public String getClanname(String id) {
        if(isClanIdExists(id)){
            return getClan(id).getClanname();
        }
        return null;
    }

    public void deleteClan(String id){
        if(isClanIdExists(id)){
            Document document = new Document("uniqueid", id);
            mongoDB.getCollection("clans").deleteOne(document);
            ClanProvider.cs.remove(id);
        }
    }

    public boolean isClannameExists(String name){
        Document document = new Document("clanname", name);
        return mongoDB.getCollection("clans").find(document).first() != null;
    }

    public boolean isClanDisplaynameExists(String name){
        Document document = new Document("displayname", name);
        return mongoDB.getCollection("clans").find(document).first() != null;
    }

}
