package org.example.elementosTransito;
import org.example.enums.urbanLevel;
import org.example.interfaces.cityPart;

public class avenue implements cityPart {
    private final int id;
    private final String name;

    public avenue(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override public int getId()           { return id; }
    @Override public String getName()      { return name; }
    @Override public urbanLevel getLevel() { return urbanLevel.AVENUE; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof avenue)) return false;
        return name.equals(((avenue) o).name);
    }

    @Override public int hashCode()    { return name.hashCode(); }
    @Override public String toString() { return name; }
}
