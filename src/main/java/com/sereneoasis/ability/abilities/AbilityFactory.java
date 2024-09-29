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

    Set<Class<?>> classes = ReflectionUtils.findAllClasses("com.sereneoasis.ability.utilities.particles");

    Class<? extends CoreAbility> potentialAbilityClass;
    Object[] initArgs;

    public AbilityFactory(Entity entity){
        SereneEntity sereneEntity = SereneEntity.getSereneAbilitiesEntity(entity);
        Archetype archetype = sereneEntity.getArchetype();
        String archetypeName = archetype.name();

        Random r = new Random();

        classes.stream().skip(r.nextInt(classes.size())).findFirst().ifPresent(aClass -> {
                        Object[] initArgs = Arrays.stream(aClass.getDeclaredConstructors()[0].getParameterTypes()).map( (constructorType) ->
                        {
                            if (constructorType.equals(String.class)) {
                                return archetypeName + aClass.getName();
                            } else if (constructorType.equals(Entity.class)){
                                return entity;
                            } else if (constructorType.equals(boolean.class)){
                                return true;
                            } else if (constructorType.equals(Vector.class)){
                                return entity.getLocation().getDirection();
                            } else if (constructorType.equals(Location.class)){
                                return entity.getLocation().add(0, Math.random()*entity.getHeight() * 3, 0);
                            } else {
                                Bukkit.getLogger().log(Level.SEVERE, "Parameter for an ability improperly matched " + constructorType.getName() + " ability " + aClass.getName());
                                return null;
                            }
                        }).toArray();
                        potentialAbilityClass = (Class<? extends CoreAbility>) aClass;
                        this.initArgs = initArgs;
            try {
                this.startAbility();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void startAbility() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Bukkit.broadcastMessage("started ability " + potentialAbilityClass.getName());

        potentialAbilityClass.getDeclaredConstructors()[0].newInstance(initArgs);

    }
}
