package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Basic particle based blast ability
 */
public class SourcedBlast extends CoreAbility {

    private boolean shot, directable, selfPush;

    private Location loc, origin;
    private Vector dir;

    private String name;

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private boolean shouldDamage;

    public SourcedBlast(Entity entity, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual, boolean selfPush, boolean shouldDamage) {
        super(entity, name);
        this.shot = false;
        this.selfPush = selfPush;
        this.shouldDamage = shouldDamage;
        
        this.directable = directable;
        this.archetypeVisual = archetypeVisual;
        this.loc = Locations.getFacingLocationObstructed(entity.getLocation().add(0,entity.getHeight()-0.5, 0), entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection(), sourceRange).subtract(entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().clone().multiply(radius));
        this.abilityStatus = AbilityStatus.SOURCE_SELECTED;


        start();
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            if (loc.distance(origin) > range) {
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
            for (Entity e : Entities.getEntitiesAroundPoint(loc, radius)) {
                if (e.getUniqueId() == entity.getUniqueId() && selfPush || e.getUniqueId() != entity.getUniqueId()) {
                    e.setVelocity(this.dir.clone().multiply(speed));
                }
            }
            loc.add(dir.clone().multiply(speed));
        }
        archetypeVisual.playVisual(loc, size, radius, 10, 10, 5);
        //TDBs.playTDBs(loc, DisplayBlock.AIR, 5, size, hitbox);
        //Particles.spawnParticle(particle, loc, 5, hitbox, 0);

    }

    public Location getLoc() {
        return loc;
    }

    public void setDir() {
        dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SHOT;
            this.origin = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();
            this.dir = origin.getDirection();
        }
    }

    

    
}
