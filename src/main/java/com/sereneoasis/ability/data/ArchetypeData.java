package com.sereneoasis.ability.data;

import com.sereneoasis.util.Colors;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Sakrajin
 * Represents all configuration data for a specific archetype
 */
public class ArchetypeData {


    private Set<Material> blocks = new HashSet<>();

    private String color;

    public ArchetypeData(Set<Material> blocks, String color) {

        this.blocks = blocks;
        this.color = color;
    }


    public Set<Material> getBlocks() {
        return blocks;
    }

    public String getColor() {
        return color;
    }

    public Color getRealColor() {
        return Colors.hexToColor(this.getColor());
    }
}
