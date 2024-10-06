package com.sereneoasis.ability.utilities.velocity;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Velocity is in units of 1/8000 of a block per server tick (50ms); for example, -1343 would move (-1343 / 8000) = −0.167875 blocks per tick (or −3.3575 blocks per second).
 */
public class Jet extends CoreAbility {

    

    public Jet(Entity entity, String name) {
        super(entity, name);

        abilityStatus = AbilityStatus.MOVING;
        
        start();
    }

    @Override
    public void progress() {

        if (System.currentTimeMillis() > startTime + (duration)) {
            abilityStatus = AbilityStatus.COMPLETE;
        }


        Vector dir = entity.getLocation().add(0, 1, 0).getDirection().normalize();
        entity.setVelocity(dir.multiply(speed));

    }

    

    
}
