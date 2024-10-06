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

public class SkyVisual implements ArchetypeVisual {

    @Override
    public void playVisual(Location loc, double size, double radius, int tb, int cloud, int white) {
        TDBs.playTDBs(loc, DisplayBlock.AIR, tb, size, radius);
        Particles.spawnParticle(Particle.GUST_DUST, loc, cloud, radius, 0);
        Particles.spawnColoredParticle(loc, white, radius, size * 3, ArchetypeDataManager.getArchetypeData(Archetype.SKY).getRealColor());

        TDBs.playTDBs(loc, DisplayBlock.LIGHTNING, tb, size, radius);
        Particles.spawnParticle(Particle.ELECTRIC_SPARK, loc, cloud, radius, 0);
        Particles.spawnParticle(Particle.GLOW, loc, cloud, radius, 0);

    }

    @Override
    public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {
        Particles.spawnParticle(Particle.GUST_DUST, loc, amount, radius, 0);
        TDBs.playTDBs(loc, DisplayBlock.LIGHTNING, tb, size, radius);


    }

    @Override
    public void playDisplayBlock(Location loc, double size, double radius) {
        TDBs.playTDBs(loc, DisplayBlock.AIR, 1, size, radius);
        TDBs.playTDBs(loc, DisplayBlock.LIGHTNING, 1, size, radius);
    }
}