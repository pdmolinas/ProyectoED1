package org.example.elementosTransito;

import org.example.enums.EventType;

public class Event {

    private final String name;
    private final EventType type;
    private final int intersectionId;
    private int riskLevel;
    private long reportTime;


    public Event(String name, EventType type, int intersectionId, int riskLevel, long reportTime) {
        this.name = name;
        this.type = type;
        this.intersectionId = intersectionId;
        this.riskLevel = riskLevel;
        this.reportTime = reportTime;
    }
    public String getName() {
        return name;
    }
    public EventType getType() {
        return type;
    }
    public int getIntersectionId() {
        return intersectionId;
    }
    public int getRiskLevel() {
        return riskLevel;
    }
    public long getReportTime() {
        return reportTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event e = (Event) o;
        return intersectionId == e.intersectionId && riskLevel == e.riskLevel
                && name.equals(e.name) && type == e.type;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, type, intersectionId, riskLevel);
    }
}
