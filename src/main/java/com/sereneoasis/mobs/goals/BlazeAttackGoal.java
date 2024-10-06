package com.sereneoasis.mobs.goals;

import com.sereneoasis.SereneMobs;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.abilities.AbilityFactory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.util.Vector;

import java.util.EnumSet;

public class BlazeAttackGoal extends Goal {
    private final PathfinderMob blaze;
    private int attackStep;
    private int attackTime;
    private int lastSeen;


    public BlazeAttackGoal(PathfinderMob blaze) {
        this.blaze = blaze;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.blaze.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.blaze.canAttack(livingEntity);
    }

    @Override
    public void start() {
        this.attackStep = 0;
    }

    @Override
    public void stop() {
//        this.blaze.setCharged(false);
        this.lastSeen = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        --this.attackTime;
        LivingEntity livingEntity = this.blaze.getTarget();
        if (livingEntity != null) {
            boolean hasLineOfSight = this.blaze.getSensing().hasLineOfSight(livingEntity);
            if (hasLineOfSight) {
                this.lastSeen = 0;
            } else {
                ++this.lastSeen;
            }

            double distanceToSqr = this.blaze.distanceToSqr(livingEntity);
            if (distanceToSqr < 4.0) {
                if (!hasLineOfSight) {
                    return;
                }

                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.blaze.doHurtTarget(livingEntity);
                }

                this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);

            } else if (distanceToSqr < this.getFollowDistance() * this.getFollowDistance() && hasLineOfSight) {
//                double xDifference = livingEntity.getX() - this.blaze.getX();
//                double yDifference = livingEntity.getY(0.5) - this.blaze.getY(0.5);
//                double zDifference = livingEntity.getZ() - this.blaze.getZ();
//
                if (this.attackTime <= 0) {
                    AbilityFactory abilityFactory = new AbilityFactory(blaze.getBukkitEntity());
                    this.attackTime = 20;
                }

                this.blaze.getLookControl().setLookAt(livingEntity, 10.0F, 10.0F);
            } else if (this.lastSeen < 5) {
                this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
            }

            super.tick();
        }
    }


    private double getFollowDistance() {
        return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
}
