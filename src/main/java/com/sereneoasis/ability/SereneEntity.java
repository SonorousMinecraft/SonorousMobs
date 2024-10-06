package com.sereneoasis.ability;


import com.sereneoasis.ability.data.ArchetypeDataManager;
import com.sereneoasis.util.Colors;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SereneEntity {

    private Entity entity;

    private Archetype archetype;

    private static final Map<String, SereneEntity> SERENITY_PLAYER_MAP = new ConcurrentHashMap<>();

    public static SereneEntity getSereneAbilitiesEntity(Entity entity) {
        return SERENITY_PLAYER_MAP.get(entity.getUniqueId().toString());
    }


    public SereneEntity(Entity entity, Archetype archetype){
        this.entity = entity;
        this.archetype = archetype;
        SERENITY_PLAYER_MAP.put(entity.getUniqueId().toString(), this);
    }

    public Archetype getArchetype() {
        return archetype;
    }


    public String getStringColor() {
        return ArchetypeDataManager.getArchetypeData(this.getArchetype()).getColor();
    }

    public Color getColor() {
        return Colors.hexToColor(ArchetypeDataManager.getArchetypeData(this.getArchetype()).getColor());
    }
}
