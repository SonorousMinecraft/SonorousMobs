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

public class ChaosVisual implements ArchetypeVisual {

    @Override
    public void playVisual(Location loc, double size, double radius, int tb, int wax_on, int red) {
        TDBs.playTDBs(loc, DisplayBlock.CHAOS, tb, size, radius);
        Particles.spawnParticle(Particle.PORTAL, loc, wax_on, 0.2, 1);
        Particles.spawnColoredParticle(loc, red, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.CHAOS).getRealColor());
    }

    @Override
    public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {
        TDBs.playTDBs(loc, DisplayBlock.CHAOS, tb, size, radius);

        Particles.spawnParticle(Particle.PORTAL, loc, amount, 0.2, 1);
        Particles.spawnColoredParticle(loc, colour, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.CHAOS).getRealColor());
    }

    @Override
    public void playDisplayBlock(Location loc, double size, double radius) {
        TDBs.playTDBs(loc, DisplayBlock.CHAOS, 1, size, radius);
    }
}