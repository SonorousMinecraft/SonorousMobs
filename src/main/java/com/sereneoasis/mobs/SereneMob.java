package com.sereneoasis.mobs;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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
            aiStep(entity);

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
    
    public static void aiStep(PathfinderMob entity) {
//        if (entity.noJumpDelay > 0) {
//            --entity.noJumpDelay;
//        }
//
//        if (entity.isControlledByLocalInstance()) {
//            entity.lerpSteps = 0;
//            entity.syncPacketPositionCodec(entity.getX(), entity.getY(), entity.getZ());
//        }
//
//        if (entity.lerpSteps > 0) {
////            entity.lerpPositionAndRotationStep(entity.lerpSteps, entity.lerpX, entity.lerpY, entity.lerpZ, entity.lerpYRot, entity.lerpXRot);
//            --entity.lerpSteps;
//        } else if (!entity.isEffectiveAi()) {
//            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.98));
//        }
//
//        if (entity.lerpHeadSteps > 0) {
//            entity.lerpHeadRotationStep(entity.lerpHeadSteps, entity.lerpYHeadRot);
//            --entity.lerpHeadSteps;
//        }

//        entity.lerpTo(entity.lerpTargetX(), entity.lerpTargetY(), entity.lerpTargetZ(), entity.lerpTargetYRot(), entity.lerpTargetXRot(), 5);


        Vec3 vec3d = entity.getDeltaMovement();
        double d0 = vec3d.x;
        double d1 = vec3d.y;
        double d2 = vec3d.z;
        if (Math.abs(vec3d.x) < 0.003) {
            d0 = 0.0;
        }

        if (Math.abs(vec3d.y) < 0.003) {
            d1 = 0.0;
        }

        if (Math.abs(vec3d.z) < 0.003) {
            d2 = 0.0;
        }



        entity.setDeltaMovement(d0, d1, d2);
        entity.level().getProfiler().push("ai");
        if (entity.isEffectiveAi()) {
            entity.level().getProfiler().push("newAi");
//            entity.serverAiStep();
            serverAiStep(entity);
            entity.level().getProfiler().pop();
        }

        entity.level().getProfiler().pop();
        
//        entity.level().getProfiler().push("jump");
//        if (entity.jumping) {
//            double d3;
//            if (entity.isInLava()) {
//                d3 = entity.getFluidHeight(FluidTags.LAVA);
//            } else {
//                d3 = entity.getFluidHeight(FluidTags.WATER);
//            }
//
//            boolean flag = entity.isInWater() && d3 > 0.0;
//            double d4 = entity.getFluidJumpThreshold();
//            if (!flag || entity.onGround() && !(d3 > d4)) {
//                if (entity.isInLava() && (!entity.onGround() || d3 > d4)) {
//                    entity.jumpInLiquid(FluidTags.LAVA);
//                } else if ((entity.onGround() || flag && d3 <= d4) && entity.noJumpDelay == 0) {
//                    if ((new EntityJumpEvent(entity.getBukkitLivingEntity())).callEvent()) {
//                        entity.jumpFromGround();
//                        entity.noJumpDelay = 10;
//                    } else {
//                        entity.setJumping(false);
//                    }
//                }
//            } else {
//                entity.jumpInLiquid(FluidTags.WATER);
//            }
//        } else {
//            entity.noJumpDelay = 0;
//        }
//
//        entity.level().getProfiler().pop();
        entity.level().getProfiler().push("travel");
        entity.xxa *= 0.98F;
        entity.zza *= 0.98F;
//        entity.updateFallFlying();
        AABB axisalignedbb = entity.getBoundingBox();
        Vec3 vec3d1 = new Vec3((double)entity.xxa, (double)entity.yya, (double)entity.zza);

        entity.travel(vec3d1);

        if (entity.hasEffect(MobEffects.SLOW_FALLING) || entity.hasEffect(MobEffects.LEVITATION)) {
            entity.resetFallDistance();
        }


        entity.level().getProfiler().pop();
        entity.level().getProfiler().push("freezing");
        if (!entity.level().isClientSide && !entity.isDeadOrDying() && !entity.freezeLocked) {
            int i = entity.getTicksFrozen();
            if (entity.isInPowderSnow && entity.canFreeze()) {
                entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze(), i + 1));
            } else {
                entity.setTicksFrozen(Math.max(0, i - 2));
            }
        }

        if (!entity.level().isClientSide && entity.tickCount % 40 == 0 && entity.isFullyFrozen() && entity.canFreeze()) {
            entity.hurt(entity.damageSources().freeze(), 1.0F);
        }

        entity.level().getProfiler().pop();
//        entity.level().getProfiler().push("push");
//
//        entity.pushEntities();
//        entity.level().getProfiler().pop();
        if (((ServerLevel)entity.level()).hasEntityMoveEvent  && (entity.xo != entity.getX() || entity.yo != entity.getY() || entity.zo != entity.getZ() || entity.yRotO != entity.getYRot() || entity.xRotO != entity.getXRot())) {
            Location from = new Location(entity.level().getWorld(), entity.xo, entity.yo, entity.zo, entity.yRotO, entity.xRotO);
            Location to = new Location(entity.level().getWorld(), entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
            EntityMoveEvent event = new EntityMoveEvent(entity.getBukkitLivingEntity(), from, to.clone());
//            if (!event.callEvent()) {
//                entity.absMoveTo(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch());
//            } else 
                
                if (!to.equals(event.getTo())) {
                entity.absMoveTo(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ(), event.getTo().getYaw(), event.getTo().getPitch());
            }
        }

        if (!entity.level().isClientSide && entity.isSensitiveToWater() && entity.isInWaterRainOrBubble()) {
            entity.hurt(entity.damageSources().drown(), 1.0F);
        }

    }

    public static void serverAiStep(PathfinderMob entity) {
        entity.setNoActionTime(entity.getNoActionTime()+1);
        if (!entity.aware) {
            if (entity.goalFloat != null) {
                if (entity.goalFloat.canUse()) {
                    entity.goalFloat.tick();
                }
                entity.getJumpControl().tick();
            }

        } else {
            entity.level().getProfiler().push("sensing");
            entity.getSensing().tick();
            entity.level().getProfiler().pop();
            int i = entity.level().getServer().getTickCount() + entity.getId();
            if (i % 2 != 0 && entity.tickCount > 1) {
                entity.level().getProfiler().push("targetSelector");
                entity.targetSelector.tickRunningGoals(false);
                entity.level().getProfiler().pop();
                entity.level().getProfiler().push("goalSelector");
                entity.goalSelector.tickRunningGoals(false);
                entity.level().getProfiler().pop();
            } else {
                entity.level().getProfiler().push("targetSelector");
                entity.targetSelector.tick();
                entity.level().getProfiler().pop();
                entity.level().getProfiler().push("goalSelector");
                entity.goalSelector.tick();
                entity.level().getProfiler().pop();
            }

            entity.level().getProfiler().push("navigation");
            entity.getNavigation().tick();
            entity.level().getProfiler().pop();
//            entity.level().getProfiler().push("mob tick");
//            entity.customServerAiStep();
//            entity.level().getProfiler().pop();
            entity.level().getProfiler().push("controls");
            entity.level().getProfiler().push("move");
            entity.getMoveControl().tick();
            entity.level().getProfiler().popPush("look");
            entity.getLookControl().tick();
//            entity.level().getProfiler().popPush("jump");
//            entity.getJumpControl().tick();
//            entity.level().getProfiler().pop();
            entity.level().getProfiler().pop();
//            entity.sendDebugPackets();
        }
    }

}
