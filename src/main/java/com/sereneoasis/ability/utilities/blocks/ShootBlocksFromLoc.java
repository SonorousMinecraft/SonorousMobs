package com.sereneoasis.ability.utilities.blocks;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Locations;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a entity
 */
public class ShootBlocksFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private Material type;

    private boolean directable, autoRemove;


    private Vector dir;

    private LinkedHashMap<Vector, Double> directions = new LinkedHashMap<>();

    private long timeBetweenCurves = 150, lastCurveTime = System.currentTimeMillis();

    private int amount;

    private List<TempDisplayBlock> blocks = new ArrayList<>();

    public ShootBlocksFromLoc(Entity entity, String user, Location startLoc, Material type, boolean directable, boolean autoRemove, int amount) {
        super(entity, user);
        this.user = user;
        this.type = type;
        this.loc = startLoc;
        this.directable = directable;
        this.autoRemove = autoRemove;
        this.dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;
        this.amount = amount;


        List<Location> locs = Locations.getShotLocations(loc, amount, dir, speed);

//        if (directable){
//            locs = Locations.getBezierCurveLocations(loc, amount, directions, speed);
//            directions.put(dir, speed);
//        }
        directions.put(dir, speed);
        for (Location point : locs) {
            TempDisplayBlock tdb = new TempDisplayBlock(point, type, 60000, size);
            blocks.add(tdb);
        }


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
            locs = Locations.getBezierCurveLocations(loc, amount, directions, speed);

        } else {
            locs = Locations.getShotLocations(loc, amount, dir, speed);
        }


        for (int i = 0; i < amount; i++) {
            if (!directable || locs.size() == amount) {
                blocks.get(i).moveTo(locs.get(i).clone().add(Math.random(), Math.random(), Math.random()));
            }
        }

        DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, entity), entity, this, damage);
        loc.add(dir.clone().multiply(speed));

    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(TempDisplayBlock::revert);

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
