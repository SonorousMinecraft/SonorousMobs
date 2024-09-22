package com.sereneoasis.ability.utilities.blocks;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Entities;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Basic particle based Laser ability
 */
public class Laser extends CoreAbility {


    private Location loc;
    private Vector dir;

    private String name;

    private DisplayBlock displayBlock;


    public Laser(Entity entity, Location startLoc, String name, DisplayBlock displayBlock) {
        super(entity, name);

        
        this.displayBlock = displayBlock;
        this.loc = startLoc.clone();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location newLoc) {
        this.loc = newLoc;
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + duration) {
            this.abilityStatus = AbilityStatus.COMPLETE;
        }
        if (abilityStatus == AbilityStatus.SHOT) {

            dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().normalize();

            double distance = range;
            LivingEntity targetEntity = Entities.getFacingEntity(entity, range, hitbox);
            if (targetEntity != null) {
                DamageHandler.damageEntity(targetEntity, entity, this, damage);
                distance = targetEntity.getLocation().distance(loc);
            }

            for (double d = size; d < distance; d += size) {
                new TempDisplayBlock(loc.clone().add(dir.clone().multiply(d)), displayBlock, 100, size);
                if (!loc.clone().add(dir.clone().multiply(d)).getBlock().isPassable()) {
                    break;
                }
            }
        }


    }

    

    
}
