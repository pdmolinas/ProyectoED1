package org.example.elementosTransito;
import org.example.enums.urbanLevel;

public class city implements org.example.interfaces.cityPart {
    private final int id;
    private final String name;

    public city(int id, String name) {
        this.id = id;
        this.name = name;
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
        return urbanLevel.CITY;
    }

}
