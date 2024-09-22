package com.sereneoasis.ability.utilities.blocks;

import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RaiseBlockPillarDamage extends RaiseBlockPillar {

    private boolean hitOnce;

    private Set<LivingEntity> damagedSet = new HashSet<>();

    public RaiseBlockPillarDamage(Entity entity, String name, double height, boolean hitOnce) {
        super(entity, name, height);
        this.hitOnce = hitOnce;
    }

    public RaiseBlockPillarDamage(Entity entity, String name, double height, Block targetBlock, boolean hitOnce) {
        super(entity, name, height, targetBlock);
        this.hitOnce = hitOnce;
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        super.progress();

        if (abilityStatus != AbilityStatus.COMPLETE && !isFalling) {
            if (currentHeight - size < height) {
                if (hitOnce) {
                    boolean isFinished = AbilityDamage.damageSeveral(origin.clone().add(0, currentHeight, 0), this, entity, true, new Vector(0, 0.1, 0));
                    if (isFinished) {
                        this.abilityStatus = AbilityStatus.COMPLETE;
                    }
                } else {
                    damagedSet.addAll(AbilityDamage.damageSeveralExceptReturnHit(loc, this, entity, damagedSet, true, entity.getLocation().add(0,entity.getHeight()-0.5, 0).getDirection()));
                }
            }
        }

    }
}
