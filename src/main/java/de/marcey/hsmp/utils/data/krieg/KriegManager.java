package de.marcey.hsmp.utils.data.krieg;

import com.mongodb.client.model.Updates;
import de.marcey.hsmp.HSMP;
import de.marcey.hsmp.clansystem.Clan;
import de.marcey.hsmp.clansystem.ClanProvider;
import de.marcey.hsmp.clansystem.kriegsystem.Krieg;
import de.marcey.hsmp.utils.mongodb.MongoDB;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KriegManager {

    //ID, CLAN1ID, CLAN2ID, TIME, KIILS.1, KILLS.2

    private static MongoDB mongoDB = HSMP.getMongodb();


    public boolean isInKrieg(Clan clan1, Clan clan2){
        boolean exists = false;
        if(clan1 == null || clan2 == null){
            return false;
        }
        Document doc = mongoDB.find("kriege", "uniqueid", clan1.getUniqueId() + clan2.getUniqueId());
        if(doc != null) return true;
        Document doc2 = mongoDB.find("kriege", "uniqueid", clan2.getUniqueId() + clan1.getUniqueId());
        return doc2 != null;
    }

    public void createNewKrieg(Krieg krieg){
        if(isInKrieg(krieg.getClan1(), krieg.getClan2())){
            return;
        }
        Document document = new Document();
        document.append("uniqueid", krieg.getUniqueId());
        document.append("clan1id", krieg.getClan1().getUniqueId());
        document.append("clan2id", krieg.getClan2().getUniqueId());
        document.append("time", krieg.getTime());
        document.append("kills.1", krieg.getKills_Clan1());
        document.append("kills.2", krieg.getKills_Clan2());
        mongoDB.set("kriege", document);
    }

    public Krieg getKriegById(String id){
        Document doc = mongoDB.find("kriege", "uniqueid", id);
        if(doc == null){
            return null;
        }
        Clan clan1 = ClanProvider.getClanById(doc.getString("clan1id"));
        Clan clan2 = ClanProvider.getClanById(doc.getString("clan2id"));
        if(!isInKrieg(clan1, clan2)){
            return null;
        }
        Krieg krieg = new Krieg(clan1, clan2);
        krieg.setTime(doc.getString("time"));
        krieg.setKills_Clan1(doc.getInteger("kills.1"));
        krieg.setKills_Clan2(doc.getInteger("kills.2"));
        return krieg;
    }

    public void updateKrieg(Krieg krieg){
        if(!isInKrieg(krieg.getClan1(), krieg.getClan2())){
            createNewKrieg(krieg);
            return;
        }
        Bson updates = Updates.combine(
                Updates.set("uniqueid", krieg.getUniqueId()),
                Updates.set("clan1id", krieg.getClan1().getUniqueId()),
                Updates.set("clan2id", krieg.getClan2().getUniqueId()),
                Updates.set("time", krieg.getTime()),
                Updates.set("kills.1", krieg.getKills_Clan1()),
                Updates.set("kills.2", krieg.getKills_Clan2())
        );
        Document search = mongoDB.find("kriege", "uniqueid", krieg.getUniqueId());
        mongoDB.update("kriege", search, updates);
    }

    public List<Krieg> getAllKriege(){
        List<Krieg> list = new ArrayList<>();
        if(mongoDB.getCollection("kriege").find().first() != null) {
            for (Document doc : mongoDB.getCollection("kriege").find()) {
                Clan clan1 = ClanProvider.getClanById(doc.getString("clan1id"));
                Clan clan2 = ClanProvider.getClanById(doc.getString("clan2id"));
                if(clan1 == null || clan2 == null) continue;
                Krieg krieg = new Krieg(clan1, clan2);
                krieg.setTime(doc.getString("time"));
                krieg.setKills_Clan1(doc.getInteger("kills.1"));
                krieg.setKills_Clan2(doc.getInteger("kills.2"));
                list.add(krieg);
            }
        }
        return list;
    }

    public void deleteKrieg(Krieg krieg){
        Document document = new Document("uniqueid", krieg.getUniqueId());
        mongoDB.getCollection("kriege").deleteOne(document);
    }

}
