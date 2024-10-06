//package com.sereneoasis.ability.utilities.blocks.requirestype;
//
//
//import com.sereneoasis.ability.CoreAbility;
//import com.sereneoasis.util.AbilityStatus;
//import com.sereneoasis.util.Blocks;
//import com.sereneoasis.util.Locations;
//import com.sereneoasis.util.Vectors;
//import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Entity;
//import org.bukkit.util.Vector;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Sakrajin
// * Allows a entity to source a block and have it travel towards them
// */
//public class SourceBlockToPlayer extends CoreAbility {
//
//
//    private Location loc;
//
//    private String user;
//
//    private double distanceToStop;
//
//    private Material type;
//
//    private TempDisplayBlock glowingSource;
//
//    private int amount;
//
//    private List<TempDisplayBlock> blocks = new ArrayList<>();
//
//    private boolean shouldSneak = true;
//
//
//    public SourceBlockToPlayer(Entity entity, String user, double distanceToStop, int amount) {
//        super(entity, user);
//
//        abilityStatus = AbilityStatus.NO_SOURCE;
//        Block source = Blocks.getFacingBlock(entity, sourceRange);
//        if (source != null && Blocks.getArchetypeBlocks(sEntity).contains(source.getType())) {
//            this.user = user;
//            this.type = source.getType();
//            this.amount = amount;
//            this.distanceToStop = distanceToStop;
//            abilityStatus = AbilityStatus.SOURCE_SELECTED;
//            Location origin = Blocks.getFacingBlockLoc(entity, sourceRange);
//
//            glowingSource = Blocks.selectSourceAnimationManual(origin.clone().subtract(0, size, 0), sEntity.getColor(), size);
//            loc = origin.clone();
//
//            for (int i = 0; i < amount; i++) {
//                TempDisplayBlock tdb = new TempDisplayBlock(glowingSource.getLoc(), type, 60000, size);
//                blocks.add(tdb);
//            }
//
//            start();
//        }
//    }
//
//    public SourceBlockToPlayer(Entity entity, String user, double distanceToStop, int amount, Block source) {
//        super(entity, user);
//
//        abilityStatus = AbilityStatus.NO_SOURCE;
//        if (source != null) {
//            this.user = user;
//            this.type = source.getType();
//            this.amount = amount;
//            this.distanceToStop = distanceToStop;
//            abilityStatus = AbilityStatus.SOURCE_SELECTED;
//
//            Location origin = source.getLocation().add(size / 2, size / 2, size / 2);
//
//            glowingSource = Blocks.selectSourceAnimationManual(origin.clone(), sEntity.getColor(), size);
//            loc = origin.clone();
////            Bukkit.broadcastMessage(glowingSource.getBlockDisplay().getBlock().getMaterial().toString());
//            for (int i = 0; i < amount; i++) {
//                TempDisplayBlock tdb = new TempDisplayBlock(glowingSource.getLoc(), type, 60000, size);
//                blocks.add(tdb);
//            }
//
//            shouldSneak = false;
//            start();
//        }
//    }
//
//
//    public AbilityStatus getSourceStatus() {
//        return abilityStatus;
//    }
//
//    public Location getLocation() {
//        return loc;
//    }
//
//    @Override
//    public void progress() {
//
//        //new TempBlock(loc.getBlock(), type.createBlockData(), 500);
//        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
//        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);
//
//
//        if (abilityStatus == AbilityStatus.SOURCING) {
//            if (shouldSneak ) {
//                this.remove();
//            }
//
//            Vector dir = Vectors.getDirectionBetweenLocations(loc, entity.getLocation().add(0,entity.getHeight()-0.5, 0)).normalize();
//
//            loc.add(dir.clone().multiply(speed));
//
//            List<Location> locs = Locations.getShotLocations(loc, amount, dir, speed);
//
//            for (int i = 0; i < amount; i++) {
//                blocks.get(i).moveTo(locs.get(i).clone().add(Math.random(), Math.random(), Math.random()));
//            }
//
//            if (loc.distance(entity.getLocation()) <= distanceToStop) {
//                abilityStatus = AbilityStatus.SOURCED;
//            }
//        }
//    }
//
//    @Override
//    public void remove() {
//        super.remove();
//        blocks.forEach(TempDisplayBlock::revert);
//
//    }
//
//    public void setAbilityStatus(AbilityStatus abilityStatus) {
//        this.abilityStatus = abilityStatus;
//        if (abilityStatus != AbilityStatus.SOURCE_SELECTED) {
//            glowingSource.revert();
//        }
//    }
//
//    public Material getType() {
//        return type;
//    }
//
//
//
//    @Override
//    public String getName() {
//        return user;
//    }
//}
