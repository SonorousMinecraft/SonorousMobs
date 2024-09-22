package com.sereneoasis.ability;

import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

/**
 * @author Sakrajin
 * Enums to represent different archetypes
 */
public enum Archetype {
    NONE("none"),

    EARTH("earth"),

    CHAOS("chaos"),

    SUN("sun"),

    SKY("sky"),
    OCEAN("ocean");


    private String name;


    Archetype(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

}
