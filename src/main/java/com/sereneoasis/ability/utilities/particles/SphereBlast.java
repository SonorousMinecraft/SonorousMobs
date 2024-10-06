package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the entity
 */
public class SphereBlast extends CoreAbility {

    private boolean directable;

    private Location loc, origin;
    private Vector dir;


    private String name;

    private boolean shouldDamage;


    public SphereBlast(Entity entity, String name, boolean directable, boolean shoulDamage) {
        super(entity, name);
        
        this.directable = directable;

        this.shouldDamage = shoulDamage;
        this.loc = entity.getLocation().add(0,entity.getHeight()-0.5, 0);
        this.origin = loc.clone();
        this.dir = loc.getDirection();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {

        if (loc.distance(origin) > range) {
            this.abilityStatus = AbilityStatus.COMPLETE;
        }

        if (directable) {
            dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
        }

        loc.add(dir.clone().multiply(speed));


        for (Location loc : Locations.getSphere(loc, radius, 4)) {
            archetype.getArchetypeVisual().playVisual(loc, size, 0.1, 1, 1, 5);
        }

        if (shouldDamage) {

            if (DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, entity), entity, this, damage)){
                abilityStatus = AbilityStatus.DAMAGED;
            }
        }

    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    

    
}
