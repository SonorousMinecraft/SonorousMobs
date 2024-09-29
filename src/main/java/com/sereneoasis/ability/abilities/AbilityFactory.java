package com.sereneoasis.ability.abilities;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.SereneEntity;
import com.sereneoasis.util.ArchetypeVisuals;
import com.sereneoasis.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;

public class AbilityFactory {

    public AbilityFactory(Entity entity){
        SereneEntity sereneEntity = SereneEntity.getSereneAbilitiesEntity(entity);
        Archetype archetype = sereneEntity.getArchetype();
        String archetypeName = archetype.name();

        ReflectionUtils.findAllClasses("com.sereneoasis.ability.utilities.particles").stream()
                .forEach(aClass -> {
                    try {
                        Object[] initArgs = Arrays.stream(aClass.getDeclaredConstructors()[0].getParameterTypes()).map( (constructorType) ->
                        {
                            if (constructorType.equals(String.class)) {
                                return archetypeName + aClass.getName();
                            } else if (constructorType.equals(Entity.class)){
                                return entity;
                            } else if (constructorType.equals(boolean.class)){
                                return true;
                            } else if (constructorType.equals(ArchetypeVisuals.ArchetypeVisual.class)){
                                return new ArchetypeVisuals.AirVisual();
                            } else if (constructorType.equals(Particle.class)) {
                                return Particle.CLOUD;
                            } else if (constructorType.equals(Vector.class)){
                                return entity.getLocation().getDirection();
                            } else if (constructorType.equals(Location.class)){
                                return entity.getLocation().add(0, Math.random()*entity.getHeight() * 3, 0);
                            } else {
                                Bukkit.getLogger().log(Level.SEVERE, "Parameter for an ability improperly matched " + constructorType.getName() + " ability " + aClass.getName());
                                return null;
                            }
                        }).toArray();
                        new AbilityFactory((Class<? extends CoreAbility>) aClass, initArgs);
                    } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public AbilityFactory(Class<? extends CoreAbility> ability, Object... initArgs) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Bukkit.broadcastMessage("started ability " + ability.getName());
        ability.getDeclaredConstructors()[0].newInstance(initArgs);
    }
    
    
    
}
