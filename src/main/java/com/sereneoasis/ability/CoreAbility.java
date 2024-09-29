package com.sereneoasis.ability;

import com.sereneoasis.ability.data.AbilityData;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.ability.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Colors;
import com.sereneoasis.util.ReflectionUtils;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


/**
 * @author Sakrajin
 * Serves as a blueprint for abilities.
 * Handles config values, so they are automatically available in subclasses.
 * Used to handle all ability progression, removal and cooldowns.
 */
public abstract class CoreAbility implements Ability {

    private static final Set<CoreAbility> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    protected Entity entity;
    protected SereneEntity sEntity;
    protected Archetype archetype;
    protected long startTime, duration;
    protected double damage, hitbox, radius, range, speed, sourceRange, size;

    private String name;

    protected AbilityStatus abilityStatus;
    private static int idCounter = Integer.MIN_VALUE;


    private int id;

    private static final Map<Class<? extends CoreAbility>, Map<UUID, Map<Integer, CoreAbility>>> INSTANCES_BY_ENTITY = new ConcurrentHashMap<>();


    public CoreAbility(final Entity entity, String name) {

        this.entity = entity;
        this.name = name;
        this.sEntity = SereneEntity.getSereneAbilitiesEntity(entity);
        this.archetype = sEntity.getArchetype();
        initialiseConfigVariables(AbilityDataManager.getAbilityData(name));
    }

    /**
     * Returns a Collection of specific CoreAbility instances that were created
     * by the specified entity.
     *
     * @param entity the entity that created the instances
     * @param clazz  the class for the type of CoreAbilities
     * @return a Collection of real instances
     */
    public static <T extends CoreAbility> Collection<T> getAbilities(final Entity entity, final Class<T> clazz) {
        if (entity == null || clazz == null || INSTANCES_BY_ENTITY.get(clazz) == null || INSTANCES_BY_ENTITY.get(clazz).get(entity.getUniqueId()) == null) {
            return Collections.emptySet();
        }
        return (Collection<T>) INSTANCES_BY_ENTITY.get(clazz).get(entity.getUniqueId()).values();
    }

    /**
     * Returns a Collection of specific CoreAbility instances that were created
     * by the specified entity.
     *
     * @param entity the entity that created the instances
     * @return a Collection of real instances
     */
    public static Set<CoreAbility>getAbilities(final Entity entity) {
        Set<Class<?>> classes = ReflectionUtils.findAllClasses("com.sereneoasis.ability.utilities");
        Set<CoreAbility> abilities = new HashSet<>();
        
        classes.forEach((clazz) -> {
            if (entity == null || INSTANCES_BY_ENTITY.get(clazz) == null || INSTANCES_BY_ENTITY.get(clazz).get(entity.getUniqueId()) == null) {
                
            } else {
                abilities.addAll(INSTANCES_BY_ENTITY.get(clazz).get(entity.getUniqueId()).values());
            }
        });
        
        return abilities;
    }


    public static void progressAll() throws ReflectiveOperationException {

        for (CoreAbility abil : INSTANCES) {
            if (!abil.entity.isDead()) {
                abil.progress();
            } else {
                abil.remove();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    public SereneEntity getsEntity() {
        return sEntity;
    }

    public Archetype getArchetype() {
        return archetype;
    }



    public long getDuration() {
        return duration;
    }

    public double getDamage() {
        return damage;
    }

    public double getHitbox() {
        return hitbox;
    }

    public double getRadius() {
        return radius;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSourceRange() {
        return sourceRange;
    }
    public double getSize() {
        return size;
    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
    }


    private void initialiseConfigVariables(AbilityData abilityData) {

        this.startTime = System.currentTimeMillis();

//        this.duration = abilityData.getDuration();
//
//        this.damage = abilityData.getDamage();
//        this.hitbox = abilityData.getHitbox();
//        this.radius = abilityData.getRadius();
//        this.range = abilityData.getRange();
//        this.speed = abilityData.getSpeed();
//        this.size = abilityData.getSize();

        this.duration = 1000;

        this.damage = 1;
        this.hitbox = 1;
        this.radius = 1;
        this.range = 30;
        this.speed = 1;
        this.sourceRange = 20;
        this.size = 0.4;
    }

    public void start() {

        net.minecraft.world.entity.PathfinderMob nmsEntity = (PathfinderMob) ((CraftLivingEntity)entity).getHandle();
        if (damage == 0 ||  nmsEntity.getTarget() != null &&  nmsEntity.distanceToSqr(nmsEntity.getTarget()) < range * range) {


            INSTANCES.add(this);

            final Class<? extends CoreAbility> clazz = this.getClass();
            final UUID uuid = this.entity.getUniqueId();
            if (!INSTANCES_BY_ENTITY.containsKey(clazz)) {
                INSTANCES_BY_ENTITY.put(clazz, new ConcurrentHashMap<>());
            }
            if (!INSTANCES_BY_ENTITY.get(clazz).containsKey(uuid)) {
                INSTANCES_BY_ENTITY.get(clazz).put(uuid, new ConcurrentHashMap<>());
            }
            this.id = CoreAbility.idCounter;

            if (idCounter == Integer.MAX_VALUE) {
                idCounter = Integer.MIN_VALUE;
            } else {
                idCounter++;
            }

            INSTANCES_BY_ENTITY.get(clazz).get(uuid).put(this.id, this);
        }
    }

    @Override
    public void remove() {

        final Map<UUID, Map<Integer, CoreAbility>> classMap = INSTANCES_BY_ENTITY.get(this.getClass());
        if (classMap != null) {
            final Map<Integer, CoreAbility> entityMap = classMap.get(this.entity.getUniqueId());
            if (entityMap != null) {
                entityMap.remove(this.id);
                if (entityMap.isEmpty()) {
                    classMap.remove(this.entity.getUniqueId());
                }
            }

            if (classMap.isEmpty()) {
                INSTANCES_BY_ENTITY.remove(this.getClass());
            }
        }
        
        INSTANCES.remove(this);
    }



}