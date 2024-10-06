package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.*;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.List;

public class Blade extends CoreAbility {

    private final String name;

    private Location origin, loc1, loc2;

    private Vector dir;

    public Blade(Entity entity, String name, Location loc1, Location loc2) {
        super(entity, name);


        this.name = name;
        this.loc1 = loc1.clone();
        this.loc2 = loc2.clone();
        this.origin = Locations.getMidpoint(loc1, loc2).clone();

        this.dir = entity.getLocation().getDirection().clone().normalize();
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            loc1.add(dir.clone());
            loc2.add(dir.clone());

            //Particles.playParticlesBetweenPoints(particle, loc1, loc2, 0.1, 5, 0.5, 0);

            for (Location loc : Locations.getPointsAlongLine(loc1, loc2, size)) {
                archetype.getArchetypeVisual().playVisual(loc, size, 0.1, 1, 1, 5);

            }

            if (Entities.getEntityBetweenPoints(loc1, loc2) != null) {
                DamageHandler.damageEntity(Entities.getEntityBetweenPoints(loc1, loc2), entity, this, damage);
                abilityStatus = AbilityStatus.COMPLETE;
            }

            if (origin.distance(Locations.getMidpoint(loc1, loc2)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    public List<Location> getLocs() {
        return Locations.getPointsAlongLine(loc1, loc2, size);
    }

    @Override
    public String getName() {
        return name;
    }
}
