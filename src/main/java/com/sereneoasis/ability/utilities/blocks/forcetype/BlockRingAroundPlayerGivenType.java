package com.sereneoasis.ability.utilities.blocks.forcetype;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.util.Locations;
import com.sereneoasis.util.Vectors;
import com.sereneoasis.ability.temp.TempDisplayBlock;import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sakrajin
 * Creates a ring of blocks around a entity
 */
public class BlockRingAroundPlayerGivenType extends CoreAbility {

    private Location loc;

    private String user;

    private double ringSize;

    private DisplayBlock type;

    private int orientation;

    private Vector dir;

    private int rotation;

    private int rotatePerTick;

    private boolean clockwise;

    private List<TempDisplayBlock> blocks = new ArrayList<>();

    private boolean readyToShoot = false;

    public BlockRingAroundPlayerGivenType(Entity entity, String user, Location startLoc, DisplayBlock type, double ringSize, int orientation, int rotatePerTick, boolean clockwise) {
        super(entity, user);

        this.user = user;
        this.type = type;
        this.ringSize = ringSize;
        this.orientation = orientation;
        this.rotatePerTick = rotatePerTick;
        this.clockwise = clockwise;
        loc = startLoc;
        this.dir = Vectors.getDirectionBetweenLocations(startLoc, entity.getLocation().add(0,entity.getHeight()-0.5, 0)).setY(0).normalize();
        rotation = Math.round(entity.getLocation().add(0,entity.getHeight()-0.5, 0).getYaw());

        for (int i = 0; i < rotatePerTick; i++) {
            blocks.add(new TempDisplayBlock(startLoc, type, 60000, size));
        }

        start();
    }

    @Override
    public void progress() {

        dir = entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection().setY(0).normalize();
        int arcDegrees = (int) ((rotatePerTick * size / 3 * 360) / (2 * Math.PI * ringSize));
        List<Location> locs = Locations.getArcFromTrig(entity.getLocation().add(0,entity.getHeight()-0.5, 0), ringSize, rotatePerTick, dir, orientation,
                rotation, rotation + arcDegrees, clockwise);

        loc = locs.get(locs.size() - 1);


        for (int i = 0; i < rotatePerTick; i++) {
            blocks.get(i).moveTo(locs.get(i));
        }

        if (((rotation + rotatePerTick) % 360) < (rotation % 360)) {
            readyToShoot = true;
        } else {
            readyToShoot = false;
        }
        rotation += rotatePerTick;
//        List<Location> currentLocs = Locations.getCircle(entity.getLocation().add(0,entity.getHeight()-0.5, 0), radius, 360)
//                .subList(Math.floorMod(Math.abs(rotation) + Math.abs(rotatePerTick), 360), Math.floorMod(Math.abs(rotation), 360));
//        for (Location point : currentLocs)
//        {
//            new TempDisplayBlock(point, type.createBlockData(), 100, 0.05);
//        }


        //new TempDisplayBlock(loc, type.createBlockData(), 500, 1);
        //new TempBlock(loc.getBlock(), type.createBlockData(), 500, false);
    }

    public boolean isReadyToShoot() {
        return readyToShoot;
    }

    public double getRingSize() {
        return ringSize;
    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(TempDisplayBlock::revert);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Location getLocation() {
        return loc;
    }


    @Override
    public String getName() {
        return user;
    }
}
