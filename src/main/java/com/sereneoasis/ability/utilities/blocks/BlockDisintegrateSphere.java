package com.sereneoasis.ability.utilities.blocks;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.temp.TempBlock;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Particles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the entity
 */
public class BlockDisintegrateSphere extends CoreAbility {

    private Location loc;

    private String name;

    private double currentRadius, increment;

    private boolean noParticles = false;


    public BlockDisintegrateSphere(Entity entity, String name, Location startLoc, double currentRadius, double increment) {
        super(entity, name);

        

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.increment = increment;
        start();
    }

    public BlockDisintegrateSphere(Entity entity, String name, Location startLoc, double currentRadius, double endRadius, double increment) {
        super(entity, name);

        

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.radius = endRadius;
        this.increment = increment;
        start();
    }

    public BlockDisintegrateSphere(Entity entity, String name, Location startLoc, double currentRadius, double endRadius, double increment, boolean noParticles) {
        super(entity, name);

        

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.radius = endRadius;
        this.increment = increment;
        this.noParticles = noParticles;
        start();
    }


    @Override
    public void progress() {
        currentRadius += increment;
        if (!noParticles) {
            Particles.spawnParticle(Particle.SONIC_BOOM, loc, 20, currentRadius, 0);
        }

        Blocks.getBlocksAroundPoint(loc, currentRadius).stream().forEach(block -> {
            TempBlock tb = new TempBlock(block, Material.AIR, duration);

        });

        if (currentRadius > radius) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();

    }

    
}
