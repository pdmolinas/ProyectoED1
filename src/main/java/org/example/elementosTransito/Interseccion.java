package org.example.elementosTransito;
import org.example.enums.urbanLevel;
public class Interseccion implements org.example.interfaces.cityPart {
    private final int id;
    private final String avenue;

    private int congestionLevel;
    private int riskLevel;
    public Interseccion(int id,  String avenue) {
        this.id = id;
        this.avenue = avenue;
        this.congestionLevel = 0; 
        this.riskLevel = 0; 
    } 
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return "Intersection " + id;
    }
    @Override
    public urbanLevel getLevel() {
        return urbanLevel.INTERSECTION;
    }
    public String getAvenue() {
        return avenue;
    }
    public int getCongestionLevel() {
        return congestionLevel;
    }
    public int getRiskLevel() {
        return riskLevel;
    }
    public void setCongestionLevel(int congestionLevel) {
        this.congestionLevel = congestionLevel;
    }
    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

}