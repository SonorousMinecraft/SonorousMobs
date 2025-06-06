package com.sereneoasis.ability.utilities.blocks.forcetype.projectile;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.util.*;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a entity
 */
public class ShootBlocksFromLocGivenType extends CoreAbility {

    private Location loc;
    private String user;


    private boolean directable, autoRemove;


    private Vector dir;

    private long revertTime = 500;

    private LinkedHashMap<Vector, Double> directions = new LinkedHashMap<>();

    private long timeBetweenCurves = 50, lastCurveTime = System.currentTimeMillis();

    public ShootBlocksFromLocGivenType(Entity entity, String user) {
        super(entity, user);
        this.user = user;
        this.loc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();;
        this.directable = false;
        this.autoRemove = true;
        this.dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {


        if (loc.distance(entity.getLocation().add(0,entity.getHeight()-0.5, 0)) > range) {
            abilityStatus = AbilityStatus.COMPLETE;
            if (autoRemove) {
                this.remove();
            }

            return;
        }

        List<Location> locs = null;

        if (directable) {
            dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
            if (System.currentTimeMillis() > lastCurveTime + timeBetweenCurves) {
                directions.put(dir, speed);
                lastCurveTime = System.currentTimeMillis();
            }
//            locs = Locations.getBezierCurveLocations(loc, Math.round(Math.round(speed / (size/2))), directions, speed);
            locs = Locations.getShotLocations(loc, Math.round(Math.round(speed / (size))), dir, speed);


        } else {
            locs = Locations.getShotLocations(loc, Math.round(Math.round(speed / (size))), dir, speed);
        }


        for (Location point : locs) {
            if (directable) {
                new TempDisplayBlock(point, Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), 2000, size);
            } else {
                new TempDisplayBlock(point, Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), revertTime, size);
            }

        }

        DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, entity), entity, this, damage);
        loc.add(dir.clone().multiply(speed));

    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

    public Location getLoc() {
        return loc;
    }

    

    @Override
    public String getName() {
        return user;
    }
}
