package com.sereneoasis.util.enhancedmethods;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.ability.temp.TempBlock;
import com.sereneoasis.ability.temp.TempDisplayBlock;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Vectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnhancedDisplayBlocks {

    public static void selectSphereDBs(CoreAbility coreAbility, Location loc, long revertTime, Vector moveVector) {
        EnhancedBlocks.getFacingSphereBlocks(coreAbility, loc).forEach(block -> {

            TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), revertTime, 1);
            tdb.moveToAndMaintainFacing(tdb.getLoc().add(moveVector));


        });
    }

    public static void selectCylinderDBs(CoreAbility coreAbility, double height, long revertTime, Vector moveVector) {
        EnhancedBlocks.getTopCylinderBlocks(coreAbility, height).forEach(block -> {

            TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), revertTime, 1);
            tdb.getBlockDisplay().setGlowing(true);
            tdb.moveToAndMaintainFacing(tdb.getLoc().add(moveVector));

        });
    }

    public static void orientOrganisedDBs(HashMap<TempDisplayBlock, Vector> displayBlocks, Vector previousDir, Vector newDir, Entity entity, double displayBlockDistance) {
        Location center = entity.getLocation().add(0,entity.getHeight()-0.5,0).add(entity.getLocation().add(0,entity.getHeight()-0.5,0).getDirection().multiply(displayBlockDistance));

        for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
            double pitchDiff = Vectors.getPitchDiff(previousDir, newDir, entity);
            double yawDiff = Vectors.getYawDiff(previousDir, newDir, entity);
            entry.getValue().rotateAroundY(-yawDiff);
            entry.getValue().rotateAroundAxis(Vectors.getRightSideNormalisedVector(entity), -pitchDiff);
            entry.getKey().moveToAndMaintainFacing(center.clone().add(entry.getValue()));
            Vector facingDir = Vectors.getRightSideNormalisedVector(entity);
            entry.getKey().rotate(Vectors.getYaw(newDir, entity), Vectors.getPitch(newDir, entity));
        }
    }

    public static void orientOrganisedDBsGivenCenter(HashMap<TempDisplayBlock, Vector> displayBlocks, Vector previousDir, Vector newDir, Entity entity, Location center) {
        for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
            double pitchDiff = Vectors.getPitchDiff(previousDir, newDir, entity);
            double yawDiff = Vectors.getYawDiff(previousDir, newDir, entity);
            entry.getValue().rotateAroundY(-yawDiff);
            entry.getValue().rotateAroundAxis(Vectors.getRightSideNormalisedVector(entity), -pitchDiff);
            entry.getKey().moveToAndMaintainFacing(center.clone().add(entry.getValue()));
            Vector facingDir = Vectors.getRightSideNormalisedVector(entity);
            entry.getKey().rotate(Vectors.getYaw(newDir, entity), Vectors.getPitch(newDir, entity));
        }
    }

    public static void handleMoveOrganisedDBsAndHit(HashMap<TempDisplayBlock, Vector> displayBlocks, Entity entity, CoreAbility coreAbility) {
        for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
            TempDisplayBlock tempDisplayBlock = entry.getKey();
            Location currentLoc = tempDisplayBlock.getBlockDisplay().getLocation();
            //entry.getValue().add(entity.getLocation().add(0,entity.getHeight()-0.5,0).getDirection().add(Vector.getRandom().multiply(0.1)).multiply(0.1)).normalize();
            Entity target = Entities.getAffected(currentLoc, coreAbility.getHitbox(), entity);
            if (target instanceof LivingEntity livingEntity) {
                DamageHandler.damageEntity(livingEntity, entity, coreAbility, coreAbility.getDamage());
                coreAbility.remove();
            }
            if (coreAbility.getEntity().getLocation().distance(currentLoc) > coreAbility.getRange()) {
                coreAbility.remove();
            }
            entry.getKey().moveToAndMaintainFacing(currentLoc.add(entry.getValue().clone().multiply(coreAbility.getSpeed())));
        }
    }

    public static Set<TempDisplayBlock> createTopCircleTempBlocks(CoreAbility coreAbility, Material material) {
        Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();
        EnhancedBlocksArchetypeLess.getTopCircleBlocks(coreAbility).forEach(b -> {
            TempBlock tb = new TempBlock(b, material, coreAbility.getDuration());
            TempDisplayBlock tdb = new TempDisplayBlock(b.getLocation(), material, coreAbility.getDuration(), coreAbility.getSize());
            tempDisplayBlocks.add(tdb);
        });

        return tempDisplayBlocks;
    }

    public static Set<TempDisplayBlock> createTopCircleTempBlocks(CoreAbility coreAbility, DisplayBlock displayBlock) {
        Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();
        EnhancedBlocksArchetypeLess.getTopCircleBlocks(coreAbility).forEach(b -> {
            TempBlock tb = new TempBlock(b, displayBlock, coreAbility.getDuration());
            TempDisplayBlock tdb = new TempDisplayBlock(b.getLocation(), b.getType(), coreAbility.getDuration(), coreAbility.getSize());
            tempDisplayBlocks.add(tdb);
        });

        return tempDisplayBlocks;
    }

}
