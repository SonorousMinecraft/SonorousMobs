package com.sereneoasis.ability.utilities.particles;

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


    public Laser(Entity entity,  String name) {
        super(entity, name);

        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + duration) {
            this.abilityStatus = AbilityStatus.COMPLETE;
        }
        if (abilityStatus == AbilityStatus.SHOT) {

            Location loc = entity.getLocation().add(0,entity.getHeight()-0.5, 0);

            Vector dir = loc.getDirection().normalize();

            double distance = range;
            LivingEntity targetEntity = Entities.getFacingEntity(entity, range, hitbox);
            if (targetEntity != null) {
                DamageHandler.damageEntity(targetEntity, entity, this, damage);
                distance = targetEntity.getLocation().distance(loc);
            }

            for (double d = size; d < distance; d += size) {
                archetype.getArchetypeVisual().playDisplayBlock(loc.clone().add(dir.clone().multiply(d)), size, 0);
                if (!loc.clone().add(dir.clone().multiply(d)).getBlock().isPassable()) {
                    break;
                }
            }
        }


    }

    

    
}
