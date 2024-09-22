package com.sereneoasis.ability.utilities.blocks.forcetype;


import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Locations;
import com.sereneoasis.util.Vectors;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Sakrajin
 * Allows a entity to source a block and have it travel towards them
 */
public class SourceBlockToPlayerGivenType extends CoreAbility {


    private Location loc;

    private String user;

    private double distanceToStop;

    private DisplayBlock type;

    private TempDisplayBlock glowingSource;

    public SourceBlockToPlayerGivenType(Entity entity, String user, DisplayBlock type, double distanceToStop) {
        super(entity, user);

        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlockOrLiquid(entity, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sEntity).contains(source.getType())) {
            this.user = user;
            this.type = type;
            this.distanceToStop = distanceToStop;
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            glowingSource = Blocks.selectSourceAnimationManual(Blocks.getFacingBlockOrLiquidLoc(entity, sourceRange).clone().subtract(0, size / 2, 0), sEntity.getColor(), size);
            loc = source.getLocation();
            start();
        }
    }

    public AbilityStatus getSourceStatus() {
        return abilityStatus;
    }

    public Location getLocation() {
        return loc;
    }

    @Override
    public void progress() {

        //new TempBlock(loc.getBlock(), type.createBlockData(), 500);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);


        if (abilityStatus == AbilityStatus.SOURCING) {

            Location targetLoc = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone().add(entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().multiply(radius));

            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();

            loc.add(dir.clone().multiply(speed));

            List<Location> locs = Locations.getShotLocations(loc, Math.round(Math.round(speed / (size / 3))), dir, speed);

            for (Location point : locs) {
                new TempDisplayBlock(point, type, 500, size);
            }

            if (loc.distanceSquared(targetLoc) <= distanceToStop * distanceToStop) {
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
        if (abilityStatus != AbilityStatus.SOURCE_SELECTED) {
            glowingSource.revert();
        }
    }

    

    @Override
    public String getName() {
        return user;
    }
}
