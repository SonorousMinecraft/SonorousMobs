package com.sereneoasis.ability.utilities.blocks.forcetype.projectile;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.ability.temp.TempDisplayBlock;
import com.sereneoasis.util.Collections;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a entity
 */
public class ShootBlockFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private boolean directable, autoRemove;
    private Vector dir;
    private TempDisplayBlock block;

    public ShootBlockFromLoc(Entity entity, String user) {
        super(entity, user);
        this.user = user;
        this.loc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();;
        this.directable = false;
        this.autoRemove = true;
        this.dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;

        block = new TempDisplayBlock(loc, Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), 60000, size);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public boolean isDirectable() {
        return directable;
    }

    public void setDirectable(boolean directable) {
        this.directable = directable;
    }

    public void setRange(double newRange) {
        this.range = newRange;
    }

    public void setGlowing(Color color) {
        block.getBlockDisplay().setGlowColorOverride(color);
        block.getBlockDisplay().setGlowing(true);
    }

    @Override
    public void progress() {


        if (abilityStatus == AbilityStatus.SHOT || abilityStatus == AbilityStatus.HIT_SOLID) {

            loc.add(dir.clone().multiply(speed));
            block.moveToAndMaintainFacing(loc);

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
        block.revert();
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

    public TempDisplayBlock getBlock() {
        return block;
    }
}
