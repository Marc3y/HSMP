package de.marcey.hsmp.objects;

import javax.xml.stream.Location;
import java.util.List;

public class ChunkBorder {

    private List<Location> locations;
    private int y;

    public ChunkBorder(List<Location> list, int y){
        this.y = y;
        this.locations = list;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public int getY() {
        return y;
    }
}
