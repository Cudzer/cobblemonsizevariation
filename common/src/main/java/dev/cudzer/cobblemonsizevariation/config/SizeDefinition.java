package dev.cudzer.cobblemonsizevariation.config;

public class SizeDefinition {
    private final String name;
    private final float min;
    private final float max;
    private final String color;

    public SizeDefinition(String name, float min, float max, String color){
        this.name = name;
        this.min = min;
        this.max = max;
        this.color = color;
    }

    public boolean isInRange(float size){
        return size >= min && size <= max;
    }

    public String getName() {
        return name;
    }

    public String getColor(){
        return color;
    }
}
