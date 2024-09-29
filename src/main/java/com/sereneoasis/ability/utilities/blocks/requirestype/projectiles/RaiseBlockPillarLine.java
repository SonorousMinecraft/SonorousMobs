package com.sereneoasis.ability.utilities.blocks.requirestype.projectiles;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.ability.utilities.blocks.requirestype.RaiseBlockPillar;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.AbilityDamage;
import com.sereneoasis.util.Scheduler;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class RaiseBlockPillarLine extends BlockLine {

    private Set<RaiseBlockPillar> pillars = new HashSet<>();

    private String name;

    public RaiseBlockPillarLine(Entity entity, String name) {
        super(entity, name);
        this.name = name;
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.SHOT) {
            getNextLoc();
            if (loc != null) {
                RaiseBlockPillar pillar = new RaiseBlockPillar(entity, name, 10, loc.clone().getBlock());
                pillars.add(pillar);
                boolean isFinished = AbilityDamage.damageOne(loc.clone().add(0, size / 2, 0), this, entity, true, dir);

                if (isFinished) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
                if (loc.distanceSquared(origin) > range * range) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
            } else {
                abilityStatus = AbilityStatus.COMPLETE;
            }

            if (abilityStatus == AbilityStatus.COMPLETE) {
                Scheduler.performTaskLater(100, this::remove);
            }
        }

    }

    @Override
    public void remove() {
        super.remove();
//        pillars.forEach(CoreAbility::remove);
    }
}
