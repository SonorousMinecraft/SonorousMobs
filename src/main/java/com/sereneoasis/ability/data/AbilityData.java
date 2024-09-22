package com.sereneoasis.ability.data;


import com.sereneoasis.ability.Archetype;

/**
 * @author Sakrajin
 * Stores all the data for a specific ability.
 */
public class AbilityData {

    protected Archetype archetype;

    protected long duration;

    protected double damage, hitbox, radius, range, speed, size;

    public AbilityData(Archetype archetype,
                    long duration,
                       double damage, double hitbox, double radius, double range, double speed, double size) {

        this.archetype = archetype;


        this.duration = duration;

        this.damage = damage;
        this.hitbox = hitbox;
        this.radius = radius;
        this.range = range;
        this.speed = speed;
        this.size = size;
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

    public double getSize() {
        return size;
    }
}