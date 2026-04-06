package org.example.elementosTransito;

import org.example.enums.EventType;

public class Event {

    private final String name;
    private final EventType type;
    private final int intersectionId;
    private int riskLevel;
    private int reportTime;


    public Event(String name, EventType type, int intersectionId, int riskLevel, int reportTime) {
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
    public int getReportTime() {
        return reportTime; 
    }
    
}
