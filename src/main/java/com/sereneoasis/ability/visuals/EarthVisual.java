package com.sereneoasis.ability.visuals;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.ability.data.ArchetypeDataManager;
import com.sereneoasis.util.Particles;
import com.sereneoasis.util.TDBs;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class EarthVisual implements ArchetypeVisual {

    @Override
    public void playVisual(Location loc, double size, double radius, int tb, int cloud, int white) {
        TDBs.playTDBs(loc, DisplayBlock.EARTH, tb, size, radius);
        Particles.spawnParticle(Particle.TOTEM, loc, cloud, radius, 0);
        Particles.spawnColoredParticle(loc, white, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.EARTH).getRealColor());

    }

    @Override
    public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {
        Particles.spawnParticle(Particle.TOTEM, loc, amount, radius, 0);
        Particles.spawnColoredParticle(loc, colour, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.EARTH).getRealColor());
    }
}
