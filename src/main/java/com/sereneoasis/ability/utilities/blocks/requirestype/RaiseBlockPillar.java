package com.sereneoasis.ability.utilities.blocks.requirestype;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.*;
import com.sereneoasis.ability.temp.TempBlock;

import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.*;

public class RaiseBlockPillar extends CoreAbility {

    protected Location origin, loc;

    protected double currentHeight;
    protected double height;
    protected List<TempDisplayBlock> blocks = new ArrayList<>();

    protected Set<Block> solidifiedBlocks = new HashSet<>();
    protected List<TempBlock> solidBlocks = new ArrayList<>();

    protected boolean isFalling = false;

    private Material type;

    public RaiseBlockPillar(Entity entity, String name) {
        super(entity, name);

        
        this.height = 5;
        currentHeight = height + 1;
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlock(entity, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sEntity).contains(source.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = source.getLocation();
//            Blocks.selectSourceAnimationBlock(source, Color.GREEN);
            this.loc = origin.clone();
            this.type = source.getType();
            while (Blocks.getArchetypeBlocks(sEntity).contains(loc.getBlock().getType()) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc.getBlock(), loc.getBlock().getType(), 60000, 1);
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            Collections.reverse(blocks);

            this.height = height - currentHeight;
            currentHeight = 0;
            start();
        }
    }

    public RaiseBlockPillar(Entity entity, String name, double height, Block targetBlock) {
        super(entity, name);

        
        this.height = height;
        currentHeight = height + 1;
        abilityStatus = AbilityStatus.NO_SOURCE;

        boolean needsBeneath = false;
        if (targetBlock != null && Blocks.getArchetypeBlocks(sEntity).contains(targetBlock.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = targetBlock.getLocation();
            // Blocks.selectSourceAnimationBlock(targetBlock, Color.GREEN);
            this.loc = origin.clone();
            this.type = targetBlock.getType();

            while ( (!needsBeneath || Blocks.getArchetypeBlocks(sEntity).contains(loc.getBlock().getType())) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc.getBlock(), type, 60000, 1);
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            Collections.reverse(blocks);
            this.height = height - currentHeight;
            currentHeight = 0;
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus != AbilityStatus.COMPLETE && !isFalling) {
            if (currentHeight + size + Constants.BLOCK_RAISE_SPEED*speed > height) {
                abilityStatus = AbilityStatus.COMPLETE;
            } else {
                for (TempDisplayBlock tdb : blocks) {
                    tdb.moveTo(tdb.getBlockDisplay().getLocation().add(0, Constants.BLOCK_RAISE_SPEED * speed, 0));
                }

                Block topBlock = origin.clone().add(0, currentHeight, 0).getBlock();
                if (!solidifiedBlocks.contains(topBlock)) {
                    if (currentHeight >= 1) {
                        if (topBlock.isPassable()) {
                            solidBlocks.add(new TempBlock(topBlock, type, 60000));
                        }
                        solidifiedBlocks.add(topBlock);
                    }
                }

                currentHeight += Constants.BLOCK_RAISE_SPEED * speed;

                Entities.getEntitiesAroundPoint(origin.clone().add(0, currentHeight, 0), hitbox).forEach(entity -> entity.setVelocity(entity.getVelocity().add(new Vector(0, Constants.BLOCK_RAISE_SPEED * speed, 0))));
            }
        }

        if (isFalling && currentHeight > 0 && abilityStatus != AbilityStatus.DROPPED) {
            for (TempDisplayBlock tdb : blocks) {
                tdb.moveTo(tdb.getBlockDisplay().getLocation().subtract(0, Constants.BLOCK_RAISE_SPEED * speed * 2, 0));
            }
            currentHeight -= Constants.BLOCK_RAISE_SPEED * speed;
        }
        if (isFalling && currentHeight <= 0) {
            abilityStatus = AbilityStatus.DROPPED;
        }
    }

    public void drop() {
        if (!isFalling) {
//            blocks.forEach(TempDisplayBlock::revert);
            isFalling = true;
            solidBlocks.forEach(TempBlock::revert);

        }
    }


    @Override
    public void remove() {
        super.remove();
    }

    
}
