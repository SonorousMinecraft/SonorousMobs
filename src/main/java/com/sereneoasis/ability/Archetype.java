package com.sereneoasis.ability;

import com.sereneoasis.ability.visuals.*;

/**
 * @author Sakrajin
 * Enums to represent different archetypes
 */
public enum Archetype {
    NONE("none", null),

    EARTH("earth", new EarthVisual()),

    CHAOS("chaos", new ChaosVisual()),

    SUN("sun", new SunVisual()),

    SKY("sky", new SkyVisual()),
    OCEAN("ocean", new OceanVisual());


    private String name;

    private ArchetypeVisual archetypeVisual;


    Archetype(String name, ArchetypeVisual archetypeVisual) {
        this.name = name;
        this.archetypeVisual = archetypeVisual;
    }

    public ArchetypeVisual getArchetypeVisual() {
        return archetypeVisual;
    }

    @Override
    public String toString() {
        return name;
    }

}
