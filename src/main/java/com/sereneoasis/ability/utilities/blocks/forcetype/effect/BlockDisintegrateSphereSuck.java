package com.sereneoasis.ability.utilities.blocks.forcetype.effect;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.utilities.blocks.forcetype.sourcing.SourceBlockToLoc;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Particles;
import com.sereneoasis.ability.temp.TempBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the entity
 */
public class BlockDisintegrateSphereSuck extends CoreAbility {

    private Location centerLoc, targetLoc;

    private String name;

    private double currentRadius, increment;


    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private Set<SourceBlockToLoc> sourceBlocksToLoc = new HashSet<>();
    private Set<Block> previousSourceBlocks = new HashSet<>();
    private Random random = new Random();

    public BlockDisintegrateSphereSuck(Entity entity, String name, Location startLoc, Location targetLoc, double currentRadius, double increment) {
        super(entity, name);

        

        this.centerLoc = startLoc.clone();
        this.targetLoc = targetLoc;
        this.currentRadius = currentRadius;
        this.increment = increment;
        start();
    }


    public BlockDisintegrateSphereSuck(Entity entity, String name, Location startLoc, Location targetLoc, double currentRadius, double endRadius, double increment) {
        super(entity, name);

        

        this.centerLoc = startLoc.clone();
        this.targetLoc = targetLoc;
        this.currentRadius = currentRadius;
        this.radius = endRadius;
        this.increment = increment;
        start();
    }

    public Set<SourceBlockToLoc> getSourceBlocksToLoc() {
        return sourceBlocksToLoc;
    }

    @Override
    public void progress() {
        currentRadius += increment;
        Particles.spawnParticle(Particle.SONIC_BOOM, centerLoc, 20, currentRadius / 2, 0);

        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(centerLoc, currentRadius);

        sourceBlocks.removeIf(block -> previousSourceBlocks.contains(block));

        for (Block b : sourceBlocks) {
            if (b != null && !b.isPassable()) {
                if (random.nextDouble() < 0.1) {
                    SourceBlockToLoc sourceBlockToLoc = new SourceBlockToLoc(entity, name, 4, 1, b, targetLoc);
                    sourceBlockToLoc.setAbilityStatus(AbilityStatus.SOURCING);
                    sourceBlocksToLoc.add(sourceBlockToLoc);
                }

                TempBlock tb = new TempBlock(b, Material.AIR, 60000);
                sourceTempBlocks.add(tb);


            }
        }
        this.previousSourceBlocks.addAll(sourceBlocks);

        if (currentRadius > radius) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();

        sourceBlocksToLoc.forEach(SourceBlockToLoc::remove);

    }

    
}
