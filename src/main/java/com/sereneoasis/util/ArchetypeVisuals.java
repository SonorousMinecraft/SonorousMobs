package com.sereneoasis.util;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.abilities.DisplayBlock;
import com.sereneoasis.ability.data.ArchetypeDataManager;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ArchetypeVisuals {

    public interface ArchetypeVisual {
        void playVisual(Location loc, double size, double radius, int tb, int amount, int colour);

        void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour);
    }

    public static class AirVisual implements ArchetypeVisual {

        @Override
        public void playVisual(Location loc, double size, double radius, int tb, int cloud, int white) {
            TDBs.playTDBs(loc, DisplayBlock.AIR, tb, size, radius);
            Particles.spawnParticle(Particle.GUST_DUST, loc, cloud, radius, 0);
            Particles.spawnColoredParticle(loc, white, radius, size * 3, Color.fromRGB(242, 243, 244));
        }

        @Override
        public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {

        }
    }


    public static class LightningVisual implements ArchetypeVisual {

        @Override
        public void playVisual(Location loc, double size, double radius, int tb, int cloud, int white) {
            TDBs.playTDBs(loc, DisplayBlock.LIGHTNING, tb, size, radius);
//            Particles.spawnParticle(Particle.ELECTRIC_SPARK, loc, cloud, radius, 0);
            Particles.spawnParticle(Particle.ELECTRIC_SPARK, loc, cloud, radius, 0);
            Particles.spawnParticle(Particle.GLOW, loc, cloud, radius, 0);

//            Particles.spawnParticle(Particle.FLASH, loc, cloud, 0, 0);
//            Particles.spawnColoredParticle(loc, white, radius, size*3, Color.fromRGB(2, 28, 164));
        }

        @Override
        public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {

        }
    }

    public static class SunVisual implements ArchetypeVisual {

        @Override
        public void playVisual(Location loc, double size, double radius, int tb, int amount, int red) {
            TDBs.playTDBs(loc, DisplayBlock.SUN, tb, size, radius);

            Particles.spawnParticle(Particle.WAX_ON, loc, amount, radius, 0);
            Particles.spawnParticle(Particle.FLAME, loc, amount, radius, 0);
            Particles.spawnColoredParticle(loc, red, radius, size, ArchetypeDataManager.getArchetypeData(Archetype.SUN).getRealColor());
//            Particles.spawnColoredParticle(loc, red, radius, size*3, Color.fromRGB(220, 20, 60));
        }

        @Override
        public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {
            for (Location helixLoc : Locations.getSeveralHelixes(loc, dir, Math.round(Math.round(radius)), 20, Math.round(Math.round(radius)), Math.round(Math.round(angle)), true, 3)) {
//                TDBs.playTDBs(helixLoc, DisplayBlock.SUN, tb, size, radius);

                Particles.spawnParticle(Particle.WAX_ON, helixLoc, amount, 0.3, 0);
                Particles.spawnParticle(Particle.FLAME, helixLoc, amount, 0.1, 0);
                Particles.spawnColoredParticle(helixLoc, amount, radius, size, ArchetypeDataManager.getArchetypeData(Archetype.SUN).getRealColor());

            }
        }
    }

    public static class ChaosVisual implements ArchetypeVisual {

        @Override
        public void playVisual(Location loc, double size, double radius, int tb, int wax_on, int red) {
//            TDBs.playTDBs(loc, DisplayBlock.CHAOS, tb, size, radius);
            Particles.spawnParticle(Particle.PORTAL, loc, wax_on, 0.2, 1);
            Particles.spawnColoredParticle(loc, red, 0.2, size, Color.fromRGB(46, 26, 71));
        }

        @Override
        public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {

        }
    }


    public static class SnowVisual implements ArchetypeVisual {

        @Override
        public void playVisual(Location loc, double size, double radius, int tb, int wax_on, int red) {
//            TDBs.playTDBs(loc, DisplayBlock.CHAOS, tb, size, radius);
//            Particles.spawnParticle(Particle.PORTAL, loc, wax_on, 0.2, 1);
//            Particles.spawnColoredParticle(loc, red, 0.2, size, Color.fromRGB(46, 26, 71));
        }

        @Override
        public void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour) {
            Particles.spawnParticle(Particle.FALLING_NECTAR, loc, 1, radius, 1);
            TDBs.playTDBs(loc, DisplayBlock.SNOW, tb, size, 0);
//            Locations.getCircleLocsAroundPoint(loc, radius, size/2).forEach(location -> {
//                Particles.spawnParticle(Particle.SNOWFLAKE, loc, 1, radius, 1);
//                TDBs.playTDBs(location, DisplayBlock.SNOW, tb, size, 0);
//
//            });
        }
    }
}
