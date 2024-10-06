package com.sereneoasis.util;

import com.sereneoasis.ability.Archetype;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.SereneEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;

/**
 * @author Sakrajin
 * Handles where abilities damage entities
 */
public class DamageHandler {
    static ResourceKey<DamageType> EATING_SHIT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("eating_shit"));

    public static boolean damageEntity(Entity entity, Entity source, CoreAbility ability, double damage) {
        if (entity != null) {
            if (entity instanceof LivingEntity livingEntity) {
                // needd to add some damage cooldown
                livingEntity.damage(damage, source);
                if (SereneEntity.getSereneAbilitiesEntity(source).getArchetype().equals(Archetype.SUN)) {
                    livingEntity.setFireTicks(20);
                }
                return true;
//                net.minecraft.world.entity.entity.Entity nmsEntity = ((CraftEntity)source).getHandle();
//                net.minecraft.world.entity.LivingEntity nmsTarget = ((CraftLivingEntity)livingEntity).getHandle();
//                nmsTarget.setHealth((float) (nmsTarget.getHealth() - damage));


                //Event abilityDamageEntityEvent = new AbilityDamageEntityEvent(source, livingEntity, ability, damage);
                //Bukkit.getServer().getPluginManager().callEvent(abilityDamageEntityEvent);
            }
        }
        return false;
    }
}
