package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Entities;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Basic particle based blast ability
 */
public class Blast extends CoreAbility {

    private boolean directable;

    private Location loc, origin;
    private Vector dir;


    private String name;


    private double angle = 0;

    private boolean shouldDamage;


    public Blast(Entity entity, String name, boolean directable, boolean shouldDamage) {
        super(entity, name);
        
        this.directable = directable;

        this.shouldDamage = shouldDamage;
        this.loc = entity.getLocation().add(0,entity.getHeight()-0.5, 0);
        this.origin = loc.clone();
        this.dir = loc.getDirection();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }


    @Override
    public void progress() {
        if (abilityStatus != AbilityStatus.COMPLETE) {
            if (loc.distanceSquared(origin) > range * range) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }

            if (directable) {
                dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
            }

            if (shouldDamage) {
                if (DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, entity), entity, this, damage)) {
                    abilityStatus = AbilityStatus.DAMAGED;

                }
            }

            loc.add(dir.clone().multiply(speed));

            archetype.getArchetypeVisual().playShotVisual(loc, dir, angle, size, radius, 10, 1, 5);

            angle += 36 * speed;
        }
    }

    public Location getLoc() {
        return loc;
    }


    
}
