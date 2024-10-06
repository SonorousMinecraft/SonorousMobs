package com.sereneoasis.ability.utilities.blocks.forcetype.projectile;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.util.*;
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


    public BlockSphereBlast(Entity entity, String name) {
        super(entity, name);

        
        this.gravity = false;

        this.origin = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();
        this.loc = origin.clone();
        spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.2), Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), size);
        this.abilityStatus = AbilityStatus.CHARGED;
        start();
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.CHARGED) {
            dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().clone();
            abilityStatus = AbilityStatus.SHOT;
        }
        if (abilityStatus == AbilityStatus.SHOT) {

            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.2),
                    Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), size);

            if (loc.distance(origin) > range || !loc.getBlock().isPassable()) {
                this.abilityStatus = AbilityStatus.COMPLETE;
                remove();

            }

            if (gravity) {
                dir = dir.setY(dir.getY() - 0.01);
            }

            loc.add(dir.clone().multiply(speed));



            boolean isFinished = AbilityDamage.damageSeveral(loc, this, entity, true, entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection());

            if (isFinished) {
                this.abilityStatus = AbilityStatus.COMPLETE;
                remove();
            }
        }
    }

    public void moveToLoc(Location targetLoc) {
        if (loc.distanceSquared(targetLoc) > 1.0) {
//            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
//            loc.add(dir.clone().multiply(speed));
            loc = targetLoc.clone();
            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(targetLoc, radius, size), Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), size);
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
