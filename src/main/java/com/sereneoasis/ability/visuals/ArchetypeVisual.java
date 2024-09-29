package com.sereneoasis.ability.visuals;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface ArchetypeVisual {

    void playVisual(Location loc, double size, double radius, int tb, int amount, int colour);

    void playShotVisual(Location loc, Vector dir, double angle, double size, double radius, int tb, int amount, int colour);
}
