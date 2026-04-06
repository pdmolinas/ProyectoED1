package org.example.elementosTransito;
import org.example.enums.urbanLevel;
import org.example.interfaces.cityPart;


public class avenue implements cityPart {
    private final int id;
    private final String name;
    private final String zone;

    public avenue(int id, String name, String zone) {
        this.id = id;
        this.name = name;
        this.zone = zone;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public urbanLevel getLevel() {
        return urbanLevel.AVENUE;
    }
    public String getZone() {
        return zone;
    }

}
