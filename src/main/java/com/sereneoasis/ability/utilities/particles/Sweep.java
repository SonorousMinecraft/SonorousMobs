package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Entities;
import com.sereneoasis.util.Locations;
import com.sereneoasis.util.Vectors;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.List;

public class Sweep extends CoreAbility {

    

    private Location origin, loc1, loc2;

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private Vector dir1, dir2;

    public Sweep(Entity entity, String name, ArchetypeVisuals.ArchetypeVisual archetypeVisual, Location loc1, Location loc2) {
        super(entity, name);

        this.archetypeVisual = archetypeVisual;
        
        this.loc1 = loc1.clone();
        this.loc2 = loc2.clone();
        this.origin = entity.getLocation().add(0,entity.getHeight()-0.5, 0).clone();

        this.dir1 = Vectors.getDirectionBetweenLocations(origin, loc1).normalize();
        this.dir2 = Vectors.getDirectionBetweenLocations(origin, loc2).normalize();
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            loc1.add(dir1.clone());
            loc2.add(dir2.clone());

            List<Location> locs = Locations.getArc(loc1, loc2, origin, 0.2);
            //List<Location> locs = List.of(new Location[]{loc1, loc2});
            for (Location loc : locs) {
                archetypeVisual.playVisual(loc, size, 0.1, 10, 1, 5);
            }

            DamageHandler.damageEntity(Entities.getEntityBetweenPoints(loc1, loc2), entity, this, damage);


            if (origin.distance(Locations.getMidpoint(loc1, loc2)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    public List<Location> getLocations() {
        return Locations.getArc(loc1, loc2, origin, 0.2);
    }

    
}
