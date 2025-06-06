package com.sereneoasis.util.enhancedmethods;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Locations;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnhancedBlocks {

    public static Set<Block> getFacingSphereLiquidBlocks(CoreAbility coreability) {
        Location loc = Blocks.getFacingBlockOrLiquidLoc(coreability.getEntity(), coreability.getSourceRange());
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getBlocksAroundPoint(loc.getBlock().getLocation(), coreability.getRadius()).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreability.getsEntity()).contains(block.getType()))
                .collect(Collectors.toSet());
    }

    public static Set<Block> getFacingSphereBlocks(CoreAbility coreability) {
        Location loc = Blocks.getFacingBlockLoc(coreability.getEntity(), coreability.getSourceRange());
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getBlocksAroundPoint(loc.getBlock().getLocation(), coreability.getRadius()).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreability.getsEntity()).contains(block.getType()))
                .collect(Collectors.toSet());
    }

    public static boolean isStandingOnSource(CoreAbility coreability) {
        return !Blocks.getBlocksAroundPoint(coreability.getEntity().getLocation().subtract(0, 1, 0), 2).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreability.getsEntity()).contains(block.getType()))
                .collect(Collectors.toSet()).isEmpty();

    }

    public static Set<Block> getFacingSphereBlocks(CoreAbility coreability, Location loc) {
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getBlocksAroundPoint(loc, coreability.getRadius()).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreability.getsEntity()).contains(block.getType()))
                .collect(Collectors.toSet());
    }

    public static Set<Block> getOutsideSphereBlocks(CoreAbility coreability, Location loc) {
        if (loc == null) {
            return new HashSet<>();
        }
        Set<Block> facingSphereBlocks = getFacingSphereBlocks(coreability, loc);
        facingSphereBlocks.removeIf(block -> Blocks.getBlocksAroundPoint(loc, coreability.getRadius() - 1).contains(block));
        return facingSphereBlocks;

    }

    public static Set<Block> getTopCylinderBlocks(CoreAbility coreAbility, double height) {
        Set<Block> blocks = Blocks.getBlocksAroundPoint(coreAbility.getEntity().getLocation().subtract(0, 1, 0), coreAbility.getRadius()).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreAbility.getsEntity()).contains(block.getType()) && Blocks.isTopBlock(block))
                .collect(Collectors.toSet());


        Set<Block> beneath = new HashSet<>();
        for (int h = 1; h < height; h++) {
            int finalH = h;
            blocks.forEach(block -> {
                Block newBlock = block.getRelative(0, -finalH, 0);
                if (Blocks.getArchetypeBlocks(coreAbility.getsEntity()).contains(newBlock.getType())) {
                    beneath.add(newBlock);
                }
            });
        }
        blocks.addAll(beneath);
        return blocks;
    }

    public static Set<Block> getTopHCircleBlocks(CoreAbility coreAbility) {
        Set<Block> blocks = Locations.getOutsideSphereLocs(coreAbility.getEntity().getLocation(), coreAbility.getSourceRange(), 1).stream()
                .map(Location::getBlock)
                .filter(block -> Blocks.getArchetypeBlocks(coreAbility.getsEntity()).contains(block.getType()) && Blocks.isTopBlock(block))
                .collect(Collectors.toSet());
        return blocks;
    }

    public static Set<Block> getTopCircleBlocks(CoreAbility coreAbility) {
        return Blocks.getBlocksAroundPoint(coreAbility.getEntity().getLocation(), coreAbility.getRadius()).stream().filter(b -> Blocks.getArchetypeBlocks(coreAbility.getsEntity()).contains(b.getType()) && Blocks.isTopBlock(b) && !b.isPassable()).collect(Collectors.toSet());
    }

    public static Set<Block> getTopCircleBlocksFloor(CoreAbility coreAbility) {

        Location floorLoc = Blocks.getBelowBlock(coreAbility.getEntity().getLocation().getBlock(), coreAbility.getRadius()).getLocation();

        return Blocks.getBlocksAroundPoint(floorLoc, coreAbility.getRadius()).stream().filter(b -> Blocks.getArchetypeBlocks(coreAbility.getsEntity()).contains(b.getType()) && Blocks.isTopBlock(b) && !b.isPassable()).collect(Collectors.toSet());
    }

    public static Set<Block> getTopHCircleBlocks(CoreAbility coreAbility, Location loc) {
        Set<Block> blocks = Locations.getOutsideSphereLocs(loc, coreAbility.getSourceRange(), 1).stream()
                .map(Location::getBlock)
                .filter(block -> Blocks.getArchetypeBlocks(coreAbility.getsEntity()).contains(block.getType()) && Blocks.isTopBlock(block))
                .collect(Collectors.toSet());

        return blocks;
    }


}
