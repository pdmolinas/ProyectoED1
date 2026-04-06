package org.example.elementosTransito;
import org.example.enums.urbanLevel;
import org.example.interfaces.cityPart;

public class zone implements cityPart {
    private final int id;
    private final String name;
    private final String district;

    public zone(int id, String name, String district) {
        this.id = id;
        this.name = name;
        this.district = district;
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
        return urbanLevel.ZONE;
    }
    public String getDistrict() {
        return district;
    }


}
