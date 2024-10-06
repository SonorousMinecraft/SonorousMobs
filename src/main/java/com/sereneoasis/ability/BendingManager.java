package com.sereneoasis.ability;


import com.sereneoasis.ability.temp.TempBlock;
import com.sereneoasis.ability.temp.TempDisplayBlock;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.util.BoundingBox;
import oshi.util.tuples.Pair;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class BendingManager implements Runnable {

    private static BendingManager instance;
    private static Stream<Pair<CoreAbility, Stream<BoundingBox>>> redirectBoundingBoxes;
    private long time;

    public BendingManager() {
        instance = this;

    }

    public static BendingManager getInstance() {
        return instance;
    }

    @Override
    public void run() {

        try {
            CoreAbility.progressAll();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
//        this.handleCooldowns();

        TempBlock.checkBlocks();
//        while (!TempBlock.getRevertQueue().isEmpty()) {
//            final TempBlock tempBlock = TempBlock.getRevertQueue().peek(); //Check if the top TempBlock is ready for reverting
//            if (tempBlock.getRevertTime() < System.currentTimeMillis()) {
//                tempBlock.automaticRevert();
//            } else {
//                break;
//            }
//        }
        while (!TempDisplayBlock.getRevertQueue().isEmpty()) {
            final TempDisplayBlock tempDisplayBlock = TempDisplayBlock.getRevertQueue().peek(); //Check if the top TempBlock is ready for reverting
            if (tempDisplayBlock.getRevertTime() < System.currentTimeMillis()) {
                tempDisplayBlock.automaticRevert();
            } else {
                break;
            }
        }

//        TempDisplayBlock.getTempDisplayBlockSet().forEach(tempDisplayBlock -> {
//            if (!tempDisplayBlock.getBlockDisplay().isGlowing() && tempDisplayBlock.getLoc().getBlock().getType().isSolid()){
//                tempDisplayBlock.setInvisible();
//            } else {
//                tempDisplayBlock.setVisible();
//            }
//        });
    }


}
