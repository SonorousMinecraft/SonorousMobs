package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Beam extends CoreAbility {

    

    private Set<Location> locs = new HashSet<>();

    private Random random = new Random();

    private Location beamOrigin;

    public Beam(Entity entity, String name, Location beamOrigin) {
        super(entity, name);
        


            this.beamOrigin = beamOrigin.clone();
            abilityStatus = AbilityStatus.SHOOTING;
            start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration ) {
            abilityStatus = AbilityStatus.COMPLETE;
        }


        Location beamTarget = Locations.getFacingLocationObstructed(entity.getLocation().add(0,entity.getHeight()-0.5, 0), entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection(), range);

        if (Entities.getFacingEntity(entity, range, hitbox) != null) {
            beamTarget = Entities.getFacingEntity(entity, range, hitbox).getLocation();
        }


        Vector dir = Vectors.getDirectionBetweenLocations(beamOrigin, beamTarget).normalize();


        Location startLoc = beamOrigin.clone();


        if (locs.size() < 100) {
            for (int i = 0; i < 20; i++) {
                locs.add(startLoc.clone());
//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        locs.forEach(location -> {
            location.add(dir.clone().multiply(random.nextDouble() * speed).add(getRandomOffset().multiply(Math.log(location.distance(startLoc) / 2 + 2))));
//            Particles.spawnParticle(particle, location, 1, 0, 0);

            archetype.getArchetypeVisual().playVisual(location, size, size/2, 1, 1, 1);

            AbilityDamage.damageOne(location, this, entity, true, dir);

//            SunUtils.blockExplode(entity, name, location, 2, 0.25);

        });

        locs.removeIf(location -> location.distanceSquared(entity.getLocation().add(0,entity.getHeight()-0.5, 0)) > range * range);

//        if (entity.getLocation().getPitch() > 50 && Blocks.getFacingBlock(entity, range) != null) {
//            entity.setVelocity(dir.clone().multiply(-speed));
//        }
    }

    public Location getBeamOrigin() {
        return beamOrigin;
    }

    public Set<Location> getLocs() {
        return locs;
    }

    private Vector getRandomOffset() {
        Vector randomiser = Vectors.getRightSide(entity, random.nextDouble() - 0.5).add(new Vector(0, random.nextDouble() - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(entity), Math.toRadians(-entity.getLocation().add(0,entity.getHeight()-0.5, 0).getPitch())));
        return randomiser;
    }

    
}
