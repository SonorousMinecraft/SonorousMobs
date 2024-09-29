package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Particles;
import com.sereneoasis.util.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Breath extends CoreAbility {

    

    private Set<Location> locs = new HashSet<>();

    private Random random = new Random();


    public Breath(Entity entity, String name) {
        super(entity, name);
        

            abilityStatus = AbilityStatus.SHOOTING;
            start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration ) {
            abilityStatus = AbilityStatus.COMPLETE;
        }


        Vector dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection();
        Location startLoc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).add(dir.clone().multiply(speed));
        if (locs.size() < 100) {
            for (int i = 0; i < 20; i++) {
                locs.add(startLoc.clone());
//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        locs.forEach(location -> {
            location.add(dir.clone().multiply(random.nextDouble() * speed).add(getRandomOffset().multiply(Math.log(location.distance(startLoc) + 2))));

            archetype.getArchetypeVisual().playVisual(location, size, 0.1, 1, 1, 1);


            AbilityDamage.damageOne(location, this, entity, true, dir);
        });

        locs.removeIf(location -> location.distance(entity.getLocation().add(0,entity.getHeight()-0.5, 0)) > range * range);

        if (entity.getLocation().getPitch() > 50 && Blocks.getFacingBlock(entity, range) != null) {
            entity.setVelocity(dir.clone().multiply(-speed));
        }
    }


    public Set<Location> getLocs() {
        return locs;
    }

    private Vector getRandomOffset() {
        Vector randomiser = Vectors.getRightSide(entity, random.nextDouble() - 0.5).add(new Vector(0, random.nextDouble() - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(entity), Math.toRadians(-entity.getLocation().add(0,entity.getHeight()-0.5, 0).getPitch())));
        return randomiser;
    }

    
}
