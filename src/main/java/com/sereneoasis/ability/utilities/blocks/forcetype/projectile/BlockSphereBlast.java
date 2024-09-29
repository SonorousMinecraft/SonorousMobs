package com.sereneoasis.ability.utilities.blocks.forcetype.projectile;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Locations;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the entity
 */
public class BlockSphereBlast extends CoreAbility {

    private boolean gravity;

    private Location loc, origin;
    private Vector dir;

    private String name;


    private HashMap<Integer, TempDisplayBlock> spike = new HashMap<>();


    public BlockSphereBlast(Entity entity, String name, Location startLoc, boolean gravity) {
        super(entity, name);

        
        this.gravity = gravity;

        this.origin = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();
        this.loc = startLoc.clone();
        spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, size),
                DisplayBlock.SUN, size);
        this.abilityStatus = AbilityStatus.CHARGED;
        start();
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.CHARGED) {
            dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().clone();
        }
        if (abilityStatus == AbilityStatus.SHOT) {
            if (loc.distance(origin) > range || !loc.getBlock().isPassable()) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }

            if (gravity) {
                dir = dir.setY(dir.getY() - 0.01);
            }

            loc.add(dir.clone().multiply(speed));

            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, size),
                    DisplayBlock.SUN, size);

            boolean isFinished = AbilityDamage.damageSeveral(loc, this, entity, true, entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection());

            if (isFinished) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    public void moveToLoc(Location targetLoc) {
        if (loc.distanceSquared(targetLoc) > 1.0) {
//            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
//            loc.add(dir.clone().multiply(speed));
            loc = targetLoc.clone();
            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(targetLoc, radius, size), DisplayBlock.SUN, size);
        }
    }

    public Location getLoc() {
        return loc;
    }

    @Override
    public void remove() {
        super.remove();
        revert();
    }

    public void revert() {
        for (TempDisplayBlock tb : spike.values()) {
            tb.revert();
        }
        spike.clear();
    }


    
}
