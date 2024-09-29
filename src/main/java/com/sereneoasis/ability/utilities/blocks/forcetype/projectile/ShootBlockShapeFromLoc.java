package com.sereneoasis.ability.utilities.blocks.forcetype.projectile;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Set;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a entity
 */
public class ShootBlockShapeFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private boolean directable, autoRemove;
    private Vector dir;
    private Set<TempDisplayBlock> blocks;

    public ShootBlockShapeFromLoc(Entity entity, String user, Location loc, Set<TempDisplayBlock> blocks, double radius, boolean autoRemove, Vector dir) {
        super(entity, user);
        this.user = user;

        this.blocks = blocks;

        this.directable = false;
        this.autoRemove = autoRemove;
        this.dir = dir.clone();
        this.loc = loc.clone();
        this.hitbox = radius;


        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public boolean isDirectable() {
        return directable;
    }

    public void setDirectable(boolean directable) {
        this.directable = directable;
    }


//    public ShootBlockShapeFromLoc(Entity entity, String user, Location startLoc, Material type, boolean directable, boolean autoRemove) {
//        super(entity, user);
//        this.user = user;
//        this.loc = startLoc;
//        this.directable = directable;
//        this.autoRemove = autoRemove;
//        this.dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
//        this.abilityStatus = AbilityStatus.SHOT;
//
//        block = new TempDisplayBlock(loc, type, 60000, size);
//        abilityStatus = AbilityStatus.SHOT;
//        start();
//    }

    public void setRange(double newRange) {
        this.range = newRange;
    }

    public void setGlowing(Color color) {
        blocks.forEach(block -> {
            block.getBlockDisplay().setGlowColorOverride(color);
            block.getBlockDisplay().setGlowing(true);
        });

    }

    @Override
    public void progress() {


        if (abilityStatus == AbilityStatus.SHOT || abilityStatus == AbilityStatus.HIT_SOLID) {

            loc.add(dir.clone().multiply(speed));
            blocks.forEach(block -> {
                block.moveToAndMaintainFacing(block.getLoc().add(dir.clone().multiply(speed)));

            });

            if (directable) {
                dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
            }


            if (Blocks.isSolid(loc)) {
                abilityStatus = AbilityStatus.HIT_SOLID;
                if (autoRemove) {
                    this.remove();
                }
            } else if (AbilityDamage.damageOne(loc, this, entity, true, dir)) {
                abilityStatus = AbilityStatus.DAMAGED;
                if (autoRemove) {
                    this.remove();
                }
            } else if (loc.distance(entity.getLocation().add(0,entity.getHeight()-0.5, 0)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                if (autoRemove) {
                    this.remove();
                }
            }

        }

    }

    public Vector getDir() {
        return dir;
    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(block -> {
            block.revert();
        });
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public String getName() {
        return user;
    }

}
