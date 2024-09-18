package com.sereneoasis.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SereneMonster {

    private PathfinderMob mob;
    private GoalSelector goalSelector;
    private GoalSelector targetSelector;

    public SereneMonster(EntityType<? extends PathfinderMob> type , Location location) {
        Level world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPos pos = BlockPos.containing(new Vec3(location.toVector().toVector3f()));
        this.mob = type.spawn(world.getMinecraftWorld(), pos, MobSpawnType.NATURAL);

        this.goalSelector = mob.goalSelector;
        this.targetSelector = mob.targetSelector;
        registerCreeperGoals();
    }

    public void registerCreeperGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(mob));
//        this.goalSelector.addGoal(2, new SwellGoal((Creeper) mob));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(mob, Ocelot.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(mob, Cat.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(mob, 1.0, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(mob, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(mob, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(mob));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(mob, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(mob, new Class[0]));
    }

    protected void registerSpiderGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(mob));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(mob, 0.4F));
//        this.goalSelector.addGoal(4, new Spider.SpiderAttackGoal(mob));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(mob, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(mob, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(mob));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(mob, new Class[0]));
//        this.targetSelector.addGoal(2, new Spider.SpiderTargetGoal(mob, Player.class));
//        this.targetSelector.addGoal(3, new Spider.SpiderTargetGoal(mob, IronGolem.class));
    }


    protected void registerEndermanGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(mob));
//        this.goalSelector.addGoal(1, new EnderMan.EndermanFreezeWhenLookedAt(mob));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(mob, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(mob, 1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(mob, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(mob));
//        this.goalSelector.addGoal(10, new EnderMan.EndermanLeaveBlockGoal(mob));
//        this.goalSelector.addGoal(11, new EnderMan.EndermanTakeBlockGoal(mob));
//        this.targetSelector.addGoal(1, new EnderMan.EndermanLookForPlayerGoal(mob, this::isAngryAt));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(mob, new Class[0]));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(mob, Endermite.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal(mob, false));
    }

    protected void registerZombieGoals() {

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(mob, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(mob));
//        this.goalSelector.addGoal(2, new ZombieAttackGoal(mob, 1.0, false));
//        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(mob, 1.0, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(mob, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(mob, new Class[0])).setAlertOthers(new Class[]{ZombifiedPiglin.class}));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(mob, Player.class, true));


        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(mob, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(mob, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    protected void registerSkeletonGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(mob));
        this.goalSelector.addGoal(3, new FleeSunGoal(mob, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(mob, Wolf.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(mob, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(mob, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(mob));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(mob, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(mob, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(mob, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(mob, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }


}

























