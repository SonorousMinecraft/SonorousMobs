package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Arc extends CoreAbility {

    

    private Set<Location> locs = new HashSet<>();

    private Random random = new Random();
    private Particle particle;

    private Vector offset = null;

    private long sinceLastDirChange = System.currentTimeMillis();

    private OutputLocation outputLocation;

    public Arc(Entity entity, String name, Particle particle, OutputLocation outputLocation) {
        super(entity, name);
        
        this.particle = particle;
        this.outputLocation = outputLocation;
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
//        if (Blocks.getFacingBlock(startLoc, dir, range) != null && !Blocks.getFacingBlock(startLoc, dir, range).isPassable()){
//            SkyUtils.lightningStrike(this,Blocks.getFacingBlock(startLoc, dir, range).getLocation() );
//        }

        Location endLoc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).add(dir.clone().multiply(range));

        if (outputLocation == OutputLocation.MAINHAND) {
            startLoc.add(Vectors.getVectorToMainHand(entity));
            endLoc.add(Vectors.getVectorToMainHand(entity));
        } else if (outputLocation == OutputLocation.OFFHAND) {
            startLoc.add(Vectors.getVectorToOffHand(entity));
            endLoc.add(Vectors.getVectorToOffHand(entity));
        }

        if (locs.size() < 20) {
            for (int i = 0; i < 1; i++) {
                Location location = startLoc.clone();
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
                location.setDirection(newDir);
                locs.add(location);

//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        if ((System.currentTimeMillis() - sinceLastDirChange) > 100) {
            locs.forEach(location -> {
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(location, endLoc)).normalize();
                location.setDirection((location.getDirection().add(newDir.clone().multiply(1.2))).normalize());
            });

            sinceLastDirChange = System.currentTimeMillis();

        }

        for (double d = 0; d < range; d += hitbox) {

            AbilityDamage.damageOne(startLoc.clone().add(dir.clone().multiply(d)), this, entity, true, dir);
        }


        locs.forEach(location -> {
//            location.setDirection(dir.clone());

            playParticlesBetweenPoints(location, location.clone().add(location.getDirection().clone().multiply(2)));
            location.add(location.getDirection());
//            location.add(location.getDirection().normalize());
//            Particles.spawnParticle(particle, location, 1, 0, 0);

        });

        locs.removeIf(location -> location.distanceSquared(entity.getLocation().add(0,entity.getHeight()-0.5, 0)) > range * range);

    }

    public void playParticlesBetweenPoints(Location start, Location end) {
        Vector difference = Vectors.getDirectionBetweenLocations(start, end);
        double distance = difference.length();
        Vector normalised = difference.clone().normalize();

        for (double d = 0; d <= distance; d += size) {
            Location temploc = start.clone().add(normalised.clone().multiply(d));
            //Particles.spawnColoredParticle(temploc, 1, 0.05, 1, Color.fromRGB(1, 225, 255));
//            TDBs.playTDBs(temploc, DisplayBlock.LIGHTNING, 1, size, 0);
//            Particles.spawnParticle(particle, temploc, 1, 0, 0);
            new ArchetypeVisuals.LightningVisual().playVisual(temploc, size, size / 2, 0, 1, 0);


        }
        //Particles.spawnColoredParticle(temploc, 11, 1, 1, Color.fromRGB(1, 225, 255));

//            Vector random = Vector.getRandom().normalize().add(new Vector(-0.5,-0.5,-0.5)).normalize().add(dir.clone().multiply(0.2)).normalize().multiply(0.4);
//
//            Particles.spawnParticleOffset(Particle.END_ROD, temploc, 0, random.getX(), random.getY(), random.getZ(), 0.15);
//            Particles.spawnColoredParticle(temploc, 1, Math.log(d+1), size*3, Color.fromRGB(1, 225, 255));
//            Particles.spawnParticle(Particle.ELECTRIC_SPARK, temploc, 1,Math.log(d+1),0);
    }

    public Location randomMidwayVertex(Location start, Location end) {
        Vector midpoint = end.clone().subtract(start.clone()).toVector().multiply(0.5);

        Vector random = getRandomOffset();
        if (start.distanceSquared(end) > 1) {
            random.multiply(Math.log(start.distance(end)) * Math.log(start.distance(end)));
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

    

    public enum OutputLocation {
        MAINHAND,
        OFFHAND,
        MIDDLE
    }
}
