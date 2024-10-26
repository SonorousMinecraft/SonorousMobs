package com.sereneoasis.ability.config;

import com.sereneoasis.ability.Archetype;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;

import java.util.*;


/**
 * @author Sakrajin
 * Used to create the configs for Archetypes and initialise defaults
 */
public class ConfigManager {

    private static final Map<Archetype, ConfigFile> configs = new HashMap<>();

    public ConfigManager() {
        for (Archetype archetype : Archetype.values()) {
            ConfigFile config = new ConfigFile(archetype.toString());
            configs.put(archetype, config);
        }

        loadConfig();
    }

    public static ConfigFile getConfig(Archetype archetype) {
        return configs.get(archetype);
    }

    private void saveConfigValuesAbility(FileConfiguration config, String name, String archetype,
                                          long duration,
                                         double damage, double hitbox, double radius, double range, double speed, double size) {
        String directory = archetype + ".ability." + name;
        if (duration != 0) {
            config.addDefault(directory + ".duration", duration);
        }

        if (damage != 0) {
            config.addDefault(directory + ".damage", damage);
        }

        if (hitbox != 0) {
            config.addDefault(directory + ".hitbox", hitbox);
        }

        if (radius != 0) {
            config.addDefault(directory + ".radius", radius);
        }
        if (range != 0) {
            config.addDefault(directory + ".range", range);
        }
        if (speed != 0) {
            config.addDefault(directory + ".speed", speed);
        }
        if (size != 0) {
            config.addDefault(directory + ".size", size);
        }
    }

    private void saveArchetypeBlocks(FileConfiguration config, Archetype archetype, Set<Tag<Material>> tags, Set<Material> blocks) {
        List<String> tagList = new ArrayList<>();
        tags.forEach(tag -> tagList.add(String.valueOf(tag.getKey())));
        List<String> blockList = new ArrayList<>();
        blocks.forEach(block -> blockList.add(block.toString()));
        config.addDefault(archetype.toString() + ".blocks", blockList);
        config.addDefault(archetype.toString() + ".tags", tagList);
    }

    private void saveArchetypeCosmetics(FileConfiguration config, Archetype archetype, String color) {
        String dir = archetype.toString() + ".cosmetics.";
        config.addDefault(dir + ".color", color);
    }

    public void loadConfig() {
        //Archetype config values are added on top of base values (which are below)
        /*
        saveConfigValuesArchetype(ocean, Archetype.NONE, 0, 0, 2, 0, 4, 0.4,0.0, 20, 0.13);

         */


        FileConfiguration none = getConfig(Archetype.NONE).getConfig();

        //Ability configuration

        none.addDefault(Archetype.NONE.toString() + ".blocks", "DIRT");

        saveArchetypeCosmetics(none, Archetype.NONE, "#ffffff");
        none.options().copyDefaults(true);
        getConfig(Archetype.NONE).saveConfig();


        FileConfiguration earth = getConfig(Archetype.EARTH).getConfig();

        Set<Tag<Material>> earthTags = new HashSet<>();
        earthTags.add(Tag.DIRT);
        earthTags.add(Tag.STONE_BRICKS);
        earthTags.add(Tag.SAND);
        earthTags.add(Tag.TERRACOTTA);
        earthTags.add(Tag.CONCRETE_POWDER);
        earthTags.add(Tag.BASE_STONE_OVERWORLD);
        earthTags.add(Tag.BASE_STONE_NETHER);
        Set<Material> earthBlocks = new HashSet<>();
        earthBlocks.add(Material.GRASS_BLOCK);
        earthBlocks.add(Material.STONE);
        earthBlocks.add(Material.GRAVEL);

        saveArchetypeBlocks(earth, Archetype.EARTH, earthTags, earthBlocks);

        saveArchetypeCosmetics(earth, Archetype.EARTH, "#50C878");
        earth.options().copyDefaults(true);
        getConfig(Archetype.EARTH).saveConfig();


        FileConfiguration chaos = getConfig(Archetype.CHAOS).getConfig();

        Set<Tag<Material>> chaosTags = new HashSet<>();
        chaosTags.add(Tag.ANCIENT_CITY_REPLACEABLE);
        Set<Material> chaosBlocks = new HashSet<>();
        chaosBlocks.add(Material.END_STONE);

        saveArchetypeBlocks(chaos, Archetype.CHAOS, chaosTags, chaosBlocks);

        saveArchetypeCosmetics(chaos, Archetype.CHAOS, "#C5B4E3");
        chaos.options().copyDefaults(true);

        getConfig(Archetype.CHAOS).saveConfig();

        FileConfiguration sun = getConfig(Archetype.SUN).getConfig();


        Set<Tag<Material>> sunTags = new HashSet<>();
        sunTags.add(Tag.INFINIBURN_NETHER);
        Set<Material> sunBlocks = new HashSet<>();
        sunBlocks.add(Material.RAW_GOLD_BLOCK);

        saveArchetypeBlocks(chaos, Archetype.SUN, sunTags, sunBlocks);

        saveArchetypeCosmetics(sun, Archetype.SUN, "#f9d71c");
        sun.options().copyDefaults(true);
        getConfig(Archetype.SUN).saveConfig();


        FileConfiguration sky = getConfig(Archetype.SKY).getConfig();

        Set<Tag<Material>> skyTags = new HashSet<>();
        Set<Material> skyBlocks = new HashSet<>();
        skyBlocks.add(Material.WHITE_STAINED_GLASS);

        saveArchetypeBlocks(chaos, Archetype.SKY, skyTags, skyBlocks);
        

        saveArchetypeCosmetics(sky, Archetype.SKY, "#BCC8C6");
        sky.options().copyDefaults(true);
        getConfig(Archetype.SKY).saveConfig();


        FileConfiguration ocean = getConfig(Archetype.OCEAN).getConfig();

        Set<Tag<Material>> oceanTags = new HashSet<>();
        oceanTags.add(Tag.ICE);
        oceanTags.add(Tag.SNOW);
        oceanTags.add(Tag.FLOWERS);
        oceanTags.add(Tag.CROPS);
        oceanTags.add(Tag.LEAVES);
        Set<Material> oceanBlocks = new HashSet<>();
        oceanBlocks.add(Material.WATER);
        oceanBlocks.add(Material.SHORT_GRASS);
        oceanBlocks.add(Material.TALL_GRASS);

        saveArchetypeBlocks(ocean, Archetype.OCEAN, oceanTags, oceanBlocks);


        saveArchetypeCosmetics(ocean, Archetype.OCEAN, "#005EB8");

        ocean.options().copyDefaults(true);
        getConfig(Archetype.OCEAN).saveConfig();


    }


}



