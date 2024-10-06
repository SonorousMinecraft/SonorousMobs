package com.sereneoasis.ability.data;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Sakrajin
 * Used to instantiate configuration data from archetypes and create a {@link ArchetypeData archetype data structure}.
 * Keeps track of all data structures to represent archetypes.
 */
public class ArchetypeDataManager {

    private static final Map<Archetype, ArchetypeData> ARCHETYPE_DATA_MAP = new ConcurrentHashMap<>();

    public ArchetypeDataManager() {
        for (Archetype archetype : Archetype.values()) {
            FileConfiguration config = ConfigManager.getConfig(archetype).getConfig();

            ConfigurationSection section2 = config.getConfigurationSection(archetype.toString());

            Set<String> archetypeBlocksString = new HashSet<>(section2.getStringList("blocks"));
            Set<Material> archetypeBlocks = archetypeBlocksString.stream().map(s -> Material.valueOf(s)).collect(Collectors.toSet());

            section2.getStringList("tags").forEach(tag -> {
                Tag<Material> tagBlocks = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.fromString(tag), Material.class);
                archetypeBlocks.addAll(tagBlocks.getValues());
            });

            ConfigurationSection section3 = config.getConfigurationSection(archetype.toString() + ".cosmetics");
            String color = section3.getString("color");

            ArchetypeData archetypeData = new ArchetypeData(archetypeBlocks, color);
            ARCHETYPE_DATA_MAP.put(archetype, archetypeData);

        }
    }

    public static ArchetypeData getArchetypeData(Archetype archetype) {
        return ARCHETYPE_DATA_MAP.get(archetype);
    }
}
