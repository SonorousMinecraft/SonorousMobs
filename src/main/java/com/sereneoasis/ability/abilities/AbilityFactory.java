package com.sereneoasis.ability.abilities;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.SereneEntity;
import com.sereneoasis.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

public class AbilityFactory {

    Set<Class<?>> classes = ReflectionUtils.findAllClasses("com.sereneoasis.ability.utilities.blocks.requirestype.projectiles");

    public AbilityFactory(Entity entity){
        SereneEntity sereneEntity = SereneEntity.getSereneAbilitiesEntity(entity);
        Archetype archetype = sereneEntity.getArchetype();
        String archetypeName = archetype.name();

        Random r = new Random();

        classes.stream().skip(r.nextInt(classes.size())).findFirst().ifPresent(aClass -> {

            try {
                Bukkit.broadcastMessage("started ability " + aClass.getName());

                aClass.getDeclaredConstructor(Entity.class, String.class).newInstance(entity, archetypeName+aClass.getName());
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
