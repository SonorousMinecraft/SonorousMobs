package com.sereneoasis.ability.utilities.velocity;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Entity;

public class Jump extends CoreAbility {

    


    public Jump(Entity entity, String name) {
        super(entity, name);

        

        entity.setVelocity(entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().clone().normalize().multiply(speed * 3));
        abilityStatus = AbilityStatus.COMPLETE;
    }

    @Override
    public void progress() {

    }
    
}
