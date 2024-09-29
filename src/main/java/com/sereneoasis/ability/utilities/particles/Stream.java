package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.*;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Stream extends CoreAbility {

    

    protected Set<Location> locs = new HashSet<>();
    private Random random = new Random();

    public Stream(Entity entity, String name) {
        super(entity, name);
        
            abilityStatus = AbilityStatus.SHOOTING;
            start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

        Vector dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection();

        Location startLoc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).add(dir.clone().multiply(speed));
        Location endLoc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).add(dir.clone().multiply(range));


        if (Entities.getEntityBetweenPoints(startLoc, endLoc) != null) {
            DamageHandler.damageEntity(Entities.getEntityBetweenPoints(startLoc, endLoc), entity, this, damage);
        }


        if (locs.size() < 100) {
            for (int i = 0; i < 10; i++) {
                Location location = startLoc.clone();
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
                location.setDirection(newDir);
                locs.add(location);

//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        locs.forEach(location -> {
//            location.setDirection(dir.clone());
            Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
            location.setDirection(location.getDirection().add(newDir.clone().multiply(0.1)).normalize());
            location.add(location.getDirection().clone());
            archetype.getArchetypeVisual().playVisual(location, size, 0.1, 1, 1, 1);


        });

        locs.removeIf(location -> location.distanceSquared(entity.getLocation().add(0,entity.getHeight()-0.5, 0)) > range * range);

    }


    public Location randomMidwayVertex(Location start, Location end) {
        Vector midpoint = end.clone().subtract(start.clone()).toVector().multiply(0.5);
        Vector random = Vectors.getRandom();
        if (start.distanceSquared(end) > 1) {
            random.multiply(Math.log(start.distance(end)) / 4);
        }
        return (start.clone().add(midpoint).add(random));
    }


    public Set<Location> getLocs() {
        return locs;
    }

    private Vector getRandomOffset() {
        Vector randomiser = Vectors.getRightSide(entity, random.nextDouble() - 0.5).add(new Vector(0, random.nextDouble() - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(entity), Math.toRadians(-entity.getLocation().add(0,entity.getHeight()-0.5, 0).getPitch())));
        return randomiser;
    }

    
}
