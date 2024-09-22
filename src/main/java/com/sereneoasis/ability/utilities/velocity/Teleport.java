package com.sereneoasis.ability.utilities.velocity;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Locations;
import com.sereneoasis.util.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Teleport extends CoreAbility {

    

    private Location targetLoc, origin;

    private double distance;

    public Teleport(Entity entity, String name, double distance) {
        super(entity, name);
        

            this.distance = distance;
            abilityStatus = AbilityStatus.TELEPORTING;

            Location hitLoc = Blocks.getFacingBlockOrLiquidLoc(entity, distance);

            Vector dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().clone();

            if (hitLoc == null) {
                targetLoc = Locations.getFacingLocation(entity.getLocation().add(0,entity.getHeight()-0.5, 0), dir, distance).subtract(0, 1, 0);
            } else {
                targetLoc = hitLoc.clone().subtract(dir.clone());
            }
//            Particles.spawnVibrationParticleEntity(targetLoc, 10, 0.2, 1, entity, Math.round((float) chargeTime /50));
            start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.TELEPORTING) {
            if (System.currentTimeMillis() > startTime + chargeTime) {

                targetLoc.setDirection(entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection());
                this.origin = entity.getLocation().clone();
                entity.teleport(targetLoc);
                Particles.spawnParticle(Particle.FLASH, targetLoc, 1, 0, 0);

                abilityStatus = AbilityStatus.COMPLETE;
            } else {
                Particles.spawnParticleOffset(Particle.SCULK_SOUL, targetLoc, 10, 0.25, 1, 0.25, 0);
                Particles.spawnVibrationParticleBlock(entity.getLocation(), 1, 0, 1, targetLoc.getBlock(), Math.round((float) (chargeTime - (System.currentTimeMillis() - startTime)) / 50));
//                    Particles.spawnVibrationParticleEntity(targetLoc, 10, 0.2, 0, entity, 20);

            }
        }


    }

    public Location getOrigin() {
        return origin;
    }

    
}
