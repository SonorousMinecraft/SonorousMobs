package com.sereneoasis.ability.utilities.blocks;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Blocks;
import com.sereneoasis.util.Locations;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class RaiseBlockCircle extends CoreAbility {

    

    private double currentHeight = 0;
    private double height;
    private List<TempDisplayBlock> block;

    private boolean shouldFall;


    public RaiseBlockCircle(Entity entity, String name, double height, double currentRadius, boolean shouldFall) {
        super(entity, name);

        
        this.height = height;
        this.shouldFall = shouldFall;
        block = new ArrayList<>();
        Locations.getOutsideSphereLocs(entity.getLocation(), currentRadius, size + 0.001).stream()
                .filter(l -> Blocks.getArchetypeBlocks(sEntity).contains(l.getBlock().getType()))
                .filter(l -> Blocks.isTopBlock(l.getBlock()))
                .forEach(l -> {
                    //Bukkit.broadcastMessage(String.valueOf(Vectors.getDirectionBetweenLocations(b.getLocation(), entity.getLocation()).setY(0).length()));
                    TempDisplayBlock tb = new TempDisplayBlock(l.setDirection(l.getBlock().getLocation().getDirection()).clone(), l.getBlock().getType(), 60000, size);
                    block.add(tb);
                });


//        Blocks.getBlocksAroundPoint(entity.getLocation(), currentRadius).stream()
//                .filter(b -> b.getLocation().distance(entity.getLocation()) > currentRadius/2)
//                .filter((b -> Blocks.getArchetypeBlocks(sEntity).contains(b.getType())))
//                .filter(Blocks::isTopBlock)
//                .forEach(b -> {
//                    //Bukkit.broadcastMessage(String.valueOf(Vectors.getDirectionBetweenLocations(b.getLocation(), entity.getLocation()).setY(0).length()));
//                    TempDisplayBlock tb = new TempDisplayBlock(b.getLocation(), b.getType(), 60000, size-0.001);
//                    block.add(tb);
//                });
        if (!block.isEmpty()) {
            abilityStatus = AbilityStatus.SOURCING;
            start();
        } else {
            abilityStatus = AbilityStatus.NO_SOURCE;
        }
    }

    public RaiseBlockCircle(Entity entity, String name, Location loc, double height, double currentRadius, boolean shouldFall) {
        super(entity, name);

        
        this.height = height;
        this.shouldFall = shouldFall;
        block = new ArrayList<>();
        Locations.getOutsideSphereLocs(loc, currentRadius, size + 0.001).stream()
                .filter(l -> Blocks.getArchetypeBlocks(sEntity).contains(l.getBlock().getType()))
                .filter(l -> Blocks.isTopBlock(l.getBlock()))
                .forEach(l -> {
                    //Bukkit.broadcastMessage(String.valueOf(Vectors.getDirectionBetweenLocations(b.getLocation(), entity.getLocation()).setY(0).length()));
                    TempDisplayBlock tb = new TempDisplayBlock(l.setDirection(l.getBlock().getLocation().getDirection()).clone(), l.getBlock().getType(), 60000, size);
                    block.add(tb);
                });


//        Blocks.getBlocksAroundPoint(entity.getLocation(), currentRadius).stream()
//                .filter(b -> b.getLocation().distance(entity.getLocation()) > currentRadius/2)
//                .filter((b -> Blocks.getArchetypeBlocks(sEntity).contains(b.getType())))
//                .filter(Blocks::isTopBlock)
//                .forEach(b -> {
//                    //Bukkit.broadcastMessage(String.valueOf(Vectors.getDirectionBetweenLocations(b.getLocation(), entity.getLocation()).setY(0).length()));
//                    TempDisplayBlock tb = new TempDisplayBlock(b.getLocation(), b.getType(), 60000, size-0.001);
//                    block.add(tb);
//                });
        if (!block.isEmpty()) {
            abilityStatus = AbilityStatus.SOURCING;
            start();
        } else {
            abilityStatus = AbilityStatus.NO_SOURCE;
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.SOURCING) {
            if (currentHeight < height) {
                for (TempDisplayBlock tb : block) {
                    tb.moveTo(tb.getBlockDisplay().getLocation().clone().add(0, 0.2 * speed, 0));
                }
                currentHeight += 0.2 * speed;
            } else {
                abilityStatus = AbilityStatus.SOURCED;
            }
        } else if (abilityStatus == AbilityStatus.SOURCED && shouldFall) {
            if (currentHeight > 0) {
                for (TempDisplayBlock tb : block) {
                    tb.moveTo(tb.getBlockDisplay().getLocation().clone().subtract(0, 0.2 * speed, 0));
                }
                currentHeight -= 0.2 * speed;
            } else {
                shouldFall = false;
                abilityStatus = AbilityStatus.COMPLETE;
            }

        }


    }

    @Override
    public void remove() {
        super.remove();
        for (TempDisplayBlock tb : block) {
            tb.revert();
        }
    }


    

    
}
