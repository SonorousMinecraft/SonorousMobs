package com.sereneoasis.ability.abilities.sky;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.utilities.particles.Blade;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.Locations;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class SkyBlade extends CoreAbility {

    private static final String name = "SkyBlade";

    private Blade blade;

    private Location loc1, loc2;


    public SkyBlade(Entity entity) {
        super(entity, name);
         start();

        abilityStatus = AbilityStatus.SHOT;
        loc1 = Locations.getFacingLocation(entity.getLocation(), entity.getLocation().getDirection(), sourceRange).clone();

        loc2 = Locations.getFacingLocation(entity.getLocation().add(0,entity.getHeight(),0), entity.getLocation().getDirection(), sourceRange).clone();

        if (loc1 != null && loc2 != null) {
            blade = new Blade(entity, "SkyBlade", new ArchetypeVisuals.AirVisual(), loc1, loc2);
        } else {
            this.remove();
        }

    }

    @Override
    public void progress() {



        if (abilityStatus == AbilityStatus.SHOT) {

            if (blade.getAbilityStatus() == AbilityStatus.COMPLETE) {
                this.remove();
            }
        }

    }


    @Override
    public void remove() {
        super.remove();
        if (blade != null) {
            blade.remove();
        }
    }

}