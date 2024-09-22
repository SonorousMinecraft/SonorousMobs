package com.sereneoasis.ability.utilities.particles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * @author Sakrajin
 * Allows a entity to charge a sphere that grows in size as time passes.
 */
public class ChargeSphere extends CoreAbility {

    private String name;


    private long startTime;

    private double startRadius, increment;
    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private Location loc;

    public ChargeSphere(Entity entity, String name, double startRadius, ArchetypeVisuals.ArchetypeVisual archetypeVisual) {
        super(entity, name);

        
        this.abilityStatus = AbilityStatus.CHARGING;

        this.startRadius = startRadius;
        this.startTime = System.currentTimeMillis();
        this.increment = ((radius - startRadius) / chargeTime) * 50;
        this.archetypeVisual = archetypeVisual;
        loc = Locations.getFacingLocation(entity.getLocation().add(0,entity.getHeight()-0.5, 0), entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection(), startRadius + 2);
        start();
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + chargeTime) {
            this.abilityStatus = AbilityStatus.CHARGED;
        }

        loc = Locations.getFacingLocation(entity.getLocation().add(0,entity.getHeight()-0.5, 0), entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection(), startRadius + 2);

        for (Location l : Locations.getSphere(loc,
                startRadius, 6)) {
            if (l != null) {
                archetypeVisual.playVisual(l, size, 0.5, 1, 1, 1);
            }
        }

        if (abilityStatus == AbilityStatus.CHARGING) {
            startRadius += increment;
        }
    }

    public double getCurrentRadius() {
        return startRadius;
    }

    public Location getLoc() {
        return loc.clone();
    }

    

    
}
