package com.sereneoasis.ability;

import com.sereneoasis.ability.data.AbilityData;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.ability.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


/**
 * @author Sakrajin
 * Serves as a blueprint for abilities.
 * Handles config values, so they are automatically available in subclasses.
 * Used to handle all ability progression, removal and cooldowns.
 */
public abstract class CoreAbility implements Ability {

    private static final Set<CoreAbility> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    protected Entity entity;
    protected SereneEntity sEntity;
    protected Archetype archetype;
    protected long startTime, duration;
    protected double damage, hitbox, radius, range, speed, sourceRange, size;

    private String name;

    protected AbilityStatus abilityStatus;


    public CoreAbility(final Entity entity, String name) {

        this.entity = entity;
        this.name = name;
        this.sEntity = SereneEntity.getSereneAbilitiesEntity(entity);
        initialiseConfigVariables(AbilityDataManager.getAbilityData(name));
    }


    public static void progressAll() throws ReflectiveOperationException {

        for (CoreAbility abil : INSTANCES) {
            if (!abil.entity.isDead()) {
                abil.progress();
            } else {
                abil.remove();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    public SereneEntity getsEntity() {
        return sEntity;
    }

    public Archetype getArchetype() {
        return archetype;
    }



    public long getDuration() {
        return duration;
    }

    public double getDamage() {
        return damage;
    }

    public double getHitbox() {
        return hitbox;
    }

    public double getRadius() {
        return radius;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSourceRange() {
        return sourceRange;
    }
    public double getSize() {
        return size;
    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
    }


    private void initialiseConfigVariables(AbilityData abilityData) {

        this.startTime = System.currentTimeMillis();

//        this.duration = abilityData.getDuration();
//
//        this.damage = abilityData.getDamage();
//        this.hitbox = abilityData.getHitbox();
//        this.radius = abilityData.getRadius();
//        this.range = abilityData.getRange();
//        this.speed = abilityData.getSpeed();
//        this.size = abilityData.getSize();

        this.duration = 1000;

        this.damage = 1;
        this.hitbox = 1;
        this.radius = 1;
        this.range = 30;
        this.speed = 2;
        this.sourceRange = 20;
        this.size = 0.2;
    }

    public void start() {

        INSTANCES.add(this);

    }

    @Override
    public void remove() {
        INSTANCES.remove(this);
    }



}