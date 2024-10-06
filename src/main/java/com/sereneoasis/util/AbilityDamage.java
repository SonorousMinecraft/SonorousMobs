package com.sereneoasis.util;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class AbilityDamage {

    public static boolean damageOne(Location loc, CoreAbility coreAbility, Entity entity, boolean kb, @Nullable Vector dir) {
        Entity target = Entities.getAffected(loc, coreAbility.getHitbox(), entity);

        if (target != null) {
            DamageHandler.damageEntity(target, entity, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
            return true;
        }
        return false;
    }

    public static boolean damageSeveral(Location loc, CoreAbility coreAbility, Entity entity, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), entity);
        livingEntities.forEach(target -> {
            DamageHandler.damageEntity(target, entity, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return !livingEntities.isEmpty();
    }

    public static List<LivingEntity> damageSeveralReturnHit(Location loc, CoreAbility coreAbility, Entity entity, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), entity);
        livingEntities.forEach(target -> {
            DamageHandler.damageEntity(target, entity, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return livingEntities;
    }

    public static List<LivingEntity> damageSeveralExceptReturnHit(Location loc, CoreAbility coreAbility, Entity entity, Set<LivingEntity> except, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), entity);

        livingEntities.stream().filter(livingEntity -> except == null || !except.contains(livingEntity)).forEach(target -> {
            DamageHandler.damageEntity(target, entity, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return livingEntities;
    }

    public static boolean damageSeveralExcept(Location loc, CoreAbility coreAbility, Entity entity, Set<LivingEntity> except, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), entity);
        if (except != null) {
            livingEntities.removeIf(except::contains);
        }
        livingEntities.forEach(target -> {
            DamageHandler.damageEntity(target, entity, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return !livingEntities.isEmpty();
    }
}
