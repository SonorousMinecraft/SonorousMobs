package com.sereneoasis.ability.utilities.blocks.forcetype.projectile;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.data.ArchetypeDataManager;
import com.sereneoasis.util.*;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class BlockSweepGivenType extends CoreAbility {


    private Location origin, loc1, loc2;
    private Vector dir1, dir2, dir;
    private Set<Location> oldLocs = new HashSet<>();

    private Set<LivingEntity> damagedSet = new HashSet<>();

    private Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();


    public BlockSweepGivenType(Entity entity, String name) {
        super(entity, name);



        abilityStatus = AbilityStatus.NO_SOURCE;

        this.origin = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();


        abilityStatus = AbilityStatus.SOURCE_SELECTED;

        start();
        setHasClicked();


    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {

            Set<Location> newLocs = new HashSet<>(oldLocs);
            oldLocs.forEach(location -> {
                Location newLoc = location.clone().add(dir.clone().multiply(speed));
                newLocs.add(newLoc);

            });

            loc1.add(dir.clone().multiply(speed).add(dir1));
            newLocs.add(loc1);
            loc2.add(dir.clone().multiply(speed).add(dir2));
            newLocs.add(loc2);

            newLocs.stream()
                    .filter(location -> !oldLocs.contains(location))
                    .map(Location::getBlock)
                    .forEach(block -> {
                        TempDisplayBlock tdb = new TempDisplayBlock(block, Collections.getRandom(Blocks.getArchetypeBlocks(sEntity)), 1000, 1);
//                            tdb.getBlockDisplay().setGlowing(true);
                        tempDisplayBlocks.add(tdb);
//                            tdb.moveToAndMaintainFacing(tdb.getLoc().add(0, 10, 0));
                        damagedSet.addAll(AbilityDamage.damageSeveralExceptReturnHit(tdb.getLoc(), this, entity, damagedSet, true, entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection()));

//                        DamageHandler.damageEntity(Entities.getAffected(tdb.getLoc(), hitbox, entity), entity, this, damage);
                    });

            tempDisplayBlocks.stream().forEach(tempDisplayBlock -> tempDisplayBlock.moveToAndMaintainFacing(tempDisplayBlock.getLoc().add(0, Math.random() * Constants.BLOCK_RAISE_SPEED * speed, 0)));


            oldLocs = newLocs;

//            Locations.getArc(loc1, loc2, origin.clone(), 0.5).stream()
//                    .map(Location::getBlock)
//                    .forEach(block -> {
//                            TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), 2000, 1);
//                            tdb.getBlockDisplay().setGlowing(true);
//                            tdb.moveToAndMaintainFacing(tdb.getLoc().add(0, 1, 0));
//                            DamageHandler.damageEntity(Entities.getAffected(tdb.getLoc(), hitbox, entity), entity, this, damage);
//                    });


            if (origin.distance(Locations.getMidpoint(loc1, loc2)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                this.remove();

            }
        }
    }

    public Set<TempDisplayBlock> getTempDisplayBlocks() {
        return this.tempDisplayBlocks;
    }


    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SHOT;

//            this.loc1 = origin.clone().add(Vectors.getLeftSide(entity, radius/2));
            this.loc1 = origin.clone();
//            this.loc2 = origin.clone().add(Vectors.getRightSide(entity, radius/2));
            this.loc2 = origin.clone();

            this.dir1 = Vectors.getLeftSide(entity, 1);
            this.dir2 = Vectors.getRightSide(entity, 1);

            this.dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().setY(0).normalize();

            oldLocs.add(origin.clone());
        }
    }




}
