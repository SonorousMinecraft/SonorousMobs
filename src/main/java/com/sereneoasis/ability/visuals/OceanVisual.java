package com.sereneoasis.ability.visuals;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.ability.data.ArchetypeDataManager;
import com.sereneoasis.util.Particles;
import com.sereneoasis.util.TDBs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class OceanVisual implements ArchetypeVisual {

    @Override
    public void playVisual(Location loc, double size, double radius, int tb, int cloud, int white) {
        TDBs.playTDBs(loc, DisplayBlock.WATER, tb, size, radius);
        Particles.spawnParticle(Particle.WATER_BUBBLE, loc, cloud, radius, 0);
        Particles.spawnColoredParticle(loc, white, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.OCEAN).getRealColor());


    }

    @Override
    public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {
        Particles.spawnParticle(Particle.WATER_BUBBLE, loc, amount, radius, 0);
        Particles.spawnColoredParticle(loc, colour, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.OCEAN).getRealColor());
    }
}
