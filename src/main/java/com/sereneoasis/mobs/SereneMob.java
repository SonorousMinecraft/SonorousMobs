package com.sereneoasis.mobs;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SereneMob {

    public static final List<PathfinderMob> ENTITIES = new ArrayList<>();
    public static final List<PathfinderMob> ENTITIES_TO_REMOVE = new ArrayList<>();
    private static int currentIndex = 0;
    
    @RuntimeType
    public static void tick() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        PathfinderMob entity = ENTITIES.get(currentIndex);
        entity.baseTick();

        if (!entity.level().isClientSide) {
            int i = entity.getArrowCount();
            if (i > 0) {
                if (entity.removeArrowTime <= 0) {
                    entity.removeArrowTime = 20 * (30 - i);
                }

                --entity.removeArrowTime;
                if (entity.removeArrowTime <= 0) {
                    entity.setArrowCount(i - 1);
                }
            }

            int j = entity.getStingerCount();
            if (j > 0) {
                if (entity.removeStingerTime <= 0) {
                    entity.removeStingerTime = 20 * (30 - j);
                }

                --entity.removeStingerTime;
                if (entity.removeStingerTime <= 0) {
                    entity.setStingerCount(j - 1);
                }
            }

            entity.detectEquipmentUpdatesPublic();
            if (entity.tickCount % 20 == 0) {
                entity.getCombatTracker().recheckStatus();
            }
        }

        if (!entity.isRemoved()) {
            entity.aiStep();

        } else {
            ENTITIES_TO_REMOVE.add(entity);
        }

        double d0 = entity.getX() - entity.xo;
        double d1 = entity.getZ() - entity.zo;
        float f = (float)(d0 * d0 + d1 * d1);

        entity.level().getProfiler().push("headTurn");

        entity.level().getProfiler().pop();
        entity.level().getProfiler().push("rangeChecks");
        entity.yRotO += (float)Math.round((entity.getYRot() - entity.yRotO) / 360.0F) * 360.0F;
        entity.yBodyRotO += (float)Math.round((entity.yBodyRot - entity.yBodyRotO) / 360.0F) * 360.0F;
        entity.xRotO += (float)Math.round((entity.getXRot() - entity.xRotO) / 360.0F) * 360.0F;
        entity.yHeadRotO += (float)Math.round((entity.yHeadRot - entity.yHeadRotO) / 360.0F) * 360.0F;
        entity.level().getProfiler().pop();


//        if (entity.isFallFlying()) {
//            ++entity.fallFlyTicks;
//        } else {
//            entity.fallFlyTicks = 0;
//        }

        if (entity.isSleeping()) {
            entity.setXRot(0.0F);
        }

        Iterator iterator = entity.getAttributes().getDirtyAttributes().iterator();

        while(iterator.hasNext()) {
            AttributeInstance attributemodifiable = (AttributeInstance)iterator.next();
            Attribute attribute = attributemodifiable.getAttribute();

            float f2;
            if (attribute == Attributes.MAX_HEALTH) {
                f2 = entity.getMaxHealth();
                if (entity.getHealth() > f2) {
                    entity.setHealth(f2);
                }
            } else if (attribute == Attributes.MAX_ABSORPTION) {
                f2 = entity.getMaxAbsorption();
                if (entity.getAbsorptionAmount() > f2) {
                    entity.setAbsorptionAmount(f2);
                }
            }
        }
//
        currentIndex++;
        if (currentIndex == ENTITIES.size()){
            ENTITIES.removeIf(ENTITIES_TO_REMOVE::contains);
            ENTITIES_TO_REMOVE.clear();;
            currentIndex = 0;
        }
    }



}
