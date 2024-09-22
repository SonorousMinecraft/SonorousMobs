package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DirectionalStream extends CoreAbility {

    

    protected Set<Location> locs = new HashSet<>();
    protected Particle particle;
    private Random random = new Random();
    private Vector dir;

    public DirectionalStream(Entity entity, String name, Particle particle, Vector dir) {
        super(entity, name);
        
        this.particle = particle;

        this.dir = dir;

            abilityStatus = AbilityStatus.SHOOTING;
            start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration ) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

//        Vector dir = new Vector(0,-1,0);

        Location startLoc = entity.getLocation().subtract(dir.clone().multiply(speed));
        Location endLoc = entity.getLocation().clone().subtract(dir.clone().multiply(range));

        if (locs.size() < 10000) {
            for (int i = 0; i < 100; i++) {
                Location location = startLoc.clone();
                location.setDirection(dir);
                locs.add(location);

//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }
        locs.forEach(location -> {
//            location.setDirection(dir.clone());
            Vector newDir = Vectors.getDirectionBetweenLocations(location, randomVertex(location, endLoc)).normalize();
            location.setDirection(location.getDirection().add(newDir.clone().multiply(0.2)));
            location.add(location.getDirection().clone());

            new ArchetypeVisuals.AirVisual().playVisual(location, size, 0, 1, 1, 1);

//            Particles.spawnParticle(particle, location, 1, 0, 0);

        });

        locs.removeIf(location -> {
            return ((location.distanceSquared(entity.getLocation()) > range * range));
        });
    }

    public Location randomVertex(Location start, Location end) {
        Vector diff = end.clone().subtract(start.clone()).toVector();
        Vector random = getRandomOffset();
        if (start.distanceSquared(end) > 1) {
            random.multiply(Math.sqrt(Math.log(diff.length()) * Math.log(diff.length())) / 10);
        }
        return (start.clone().add(random));
    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

    public Set<Location> getLocs() {
        return locs;
    }

    private Vector getRandomOffset() {
        Vector randomiser = Vectors.getRightSide(entity, random.nextDouble() - 0.5).add(Vectors.getUp(entity.getLocation(), random.nextDouble() - 0.5));
        return randomiser;
    }

    

}
