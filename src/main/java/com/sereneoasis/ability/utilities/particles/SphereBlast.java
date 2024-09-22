package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Locations;
import org.bukkit.Bukkit;
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

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private boolean shouldDamage;


    public SphereBlast(Entity entity, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual, boolean shoulDamage) {
        super(entity, name);
        
        this.directable = directable;
        this.archetypeVisual = archetypeVisual;
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


        for (Location loc : Locations.getSphere(loc, radius, 12)) {
            archetypeVisual.playVisual(loc, size, 0, 1, 1, 1);
        }

        if (shouldDamage) {
            DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, entity), entity, this, damage);
            if (!Entities.getAffected(loc, hitbox, entity).isEmpty()) {
                Bukkit.broadcastMessage("should be damaging");
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
