package org.example.elementosTransito;
import org.example.enums.urbanLevel;
import org.example.interfaces.cityPart;


public class district implements cityPart {
    private final int id;
    private final String name;

    public district(int id, String name) {
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
        return urbanLevel.DISTRICT;
    }
    
}
