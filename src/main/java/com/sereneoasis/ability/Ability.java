package com.sereneoasis.ability;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Entity;

/**
 * @author Sakrajin
 * Interface to define the function of all abilities.
 */
public interface Ability {

    public void progress() throws ReflectiveOperationException;

    public void remove();

    public Entity getEntity();

    public String getName();

}
