package com.sereneoasis.mobs;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.SereneEntity;
import com.sereneoasis.mobs.goals.BendingGoal;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;


public class SereneMonster {

    private PathfinderMob mob;

    public PathfinderMob getMob() {
        return mob;
    }

    private GoalSelector goalSelector;
    private GoalSelector targetSelector;




    public SereneMonster(Location location, Archetype archetype) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Level world = ((CraftWorld) location.getWorld()).getHandle();
        EntityType<Panda> type = EntityType.PANDA;
        Class<? extends PathfinderMob> entityClass = (Class<? extends PathfinderMob>) type.create(world).getClass();


        DynamicType.Unloaded<? extends PathfinderMob> unloadedType = new ByteBuddy()
                .subclass(entityClass)
                .method(ElementMatchers.named("l"))
                .intercept(MethodDelegation.to(SereneMob.class))
                .make();

        this.mob = (PathfinderMob) unloadedType.load(getClass()
                        .getClassLoader())
                .getLoaded().getDeclaredConstructor(EntityType.class, Level.class).newInstance(type, world);
        SereneMob.ENTITIES.add(mob);

        world.addFreshEntity(mob);

        new SereneEntity(mob.getBukkitEntity(), archetype);

        this.mob.moveTo(new Vec3(location.toVector().toVector3f()));
        this.goalSelector = mob.goalSelector;

        this.targetSelector = mob.targetSelector;
        Arrays.stream(Attribute.values()).forEach(attribute -> {
            if (mob.craftAttributes.getAttribute(attribute) == null){
                mob.craftAttributes.registerAttribute(attribute);
            }
        });


        mob.craftAttributes.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
        mob.craftAttributes.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(0);

        ArrayList<Entity.DefaultDrop> drops = new ArrayList<>();
        addDefaultDrop(drops, Material.ICE);
        mob.drops = drops;

        registerZombieGoals();
    }

    private void addDefaultDrop(ArrayList<Entity.DefaultDrop> drops, Material material){
        drops.add(new Entity.DefaultDrop(ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(material)), null));
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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(mob, Entity.class, true));
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
//        this.targetSelector.addGoal(2, new Spider.SpiderTargetGoal(mob, Entity.class));
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
//        this.targetSelector.addGoal(1, new EnderMan.EndermanLookForEntityGoal(mob, this::isAngryAt));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(mob, new Class[0]));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(mob, Endermite.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal(mob, false));
    }

    protected void registerZombieGoals() {

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(mob, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(mob));
//        this.goalSelector.addGoal(2, new ZombieAttackGoal(mob, 1.0, false));
        this.goalSelector.addGoal(2, new BendingGoal(mob));
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

























