package com.sereneoasis.ability.utilities.blocks.requirestype;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sereneoasis.util.Constants;
import com.sereneoasis.util.Vectors;
import com.sereneoasis.ability.temp.TempBlock;

import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the entity
 */
public class BlockCreateSphere extends CoreAbility {

    private Location centerLoc;

    private String name;

    private double increment;


//    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();


    private Material type;


    public BlockCreateSphere(Entity entity, String name) {
        super(entity, name);



        this.centerLoc = entity.getLocation().add(0, entity.getHeight() - 0.5, 0);
        this.increment = 1;
        this.type = Material.GLASS;
        start();
    }


    public BlockCreateSphere(Entity entity, String name, Location startLoc, double increment, Material type) {
        super(entity, name);

        

        this.centerLoc = startLoc.clone();
        this.increment = increment;
        this.type = type;
        start();
    }

    public BlockCreateSphere(Entity entity, String name, Location startLoc, double radius, double increment, Material type) {
        super(entity, name);

        

        this.centerLoc = startLoc.clone();
        this.increment = increment;
        this.radius = radius;
        this.type = type;
        start();
    }

    @Override
    public void progress() {
        radius -= increment;

        Set<Block> sourceBlocks = EnhancedBlocks.getOutsideSphereBlocks(this, centerLoc);

        for (Block b : sourceBlocks) {
            if (b != null && !b.isPassable()) {


                TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 1);
                Vector offset = Vectors.getDirectionBetweenLocations(centerLoc, b.getLocation()).add(new Vector(0, radius, 0)).normalize();
                displayBlocks.put(tdb, offset);


//                if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {

                TempBlock tb = new TempBlock(b, type, 60000);


//                sourceTempBlocks.add(tb);
            }
        }

        displayBlocks.forEach((tempDisplayBlock, vector) -> {
            tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().clone().add(vector.clone().normalize()));
            vector.subtract(new Vector(0, Constants.GRAVITY, 0));
        });


        if (radius - increment <= 0) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        displayBlocks.forEach((tempDisplayBlock, vector) -> tempDisplayBlock.revert());


    }

    
}
