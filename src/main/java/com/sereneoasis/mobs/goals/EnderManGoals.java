package com.sereneoasis.mobs.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R3.event.CraftEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class EnderManGoals {

    private static class EndermanFreezeWhenLookedAt extends Goal {
        private final EnderMan enderman;
        @Nullable
        private LivingEntity target;

        public EndermanFreezeWhenLookedAt(EnderMan enderman) {
            this.enderman = enderman;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.enderman.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double d0 = this.target.distanceToSqr(this.enderman);
                return d0 > 256.0;
//                return d0 > 256.0 ? false : this.enderman.isLookingAtMe((Player)this.target);
            }
        }

        public void start() {
            this.enderman.getNavigation().stop();
        }

        public void tick() {
            this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }

    private static class EndermanLeaveBlockGoal extends Goal {
        private final EnderMan enderman;

        public EndermanLeaveBlockGoal(EnderMan enderman) {
            this.enderman = enderman;
        }

        public boolean canUse() {
            return this.enderman.getCarriedBlock() == null ? false : (!this.enderman.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? false : this.enderman.getRandom().nextInt(reducedTickDelay(2000)) == 0);
        }

        public void tick() {
            RandomSource randomsource = this.enderman.getRandom();
            Level world = this.enderman.level();
            int i = Mth.floor(this.enderman.getX() - 1.0 + randomsource.nextDouble() * 2.0);
            int j = Mth.floor(this.enderman.getY() + randomsource.nextDouble() * 2.0);
            int k = Mth.floor(this.enderman.getZ() - 1.0 + randomsource.nextDouble() * 2.0);
            BlockPos blockposition = new BlockPos(i, j, k);
            BlockState iblockdata = world.getBlockStateIfLoaded(blockposition);
            if (iblockdata != null) {
                BlockPos blockposition1 = blockposition.below();
                BlockState iblockdata1 = world.getBlockState(blockposition1);
                BlockState iblockdata2 = this.enderman.getCarriedBlock();
                if (iblockdata2 != null) {
                    iblockdata2 = Block.updateFromNeighbourShapes(iblockdata2, this.enderman.level(), blockposition);
                    if (this.canPlaceBlock(world, blockposition, iblockdata2, iblockdata, iblockdata1, blockposition1) && CraftEventFactory.callEntityChangeBlockEvent(this.enderman, blockposition, iblockdata2)) {
                        world.setBlock(blockposition, iblockdata2, 3);
                        world.gameEvent(GameEvent.BLOCK_PLACE, blockposition, GameEvent.Context.of(this.enderman, iblockdata2));
                        this.enderman.setCarriedBlock((BlockState) null);
                    }
                }

            }
        }

        private boolean canPlaceBlock(Level world, BlockPos posAbove, BlockState carriedState, BlockState stateAbove, BlockState state, BlockPos pos) {
            return stateAbove.isAir() && !state.isAir() && !state.is(Blocks.BEDROCK) && state.isCollisionShapeFullBlock(world, pos) && carriedState.canSurvive(world, posAbove) && world.getEntities(this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(posAbove))).isEmpty();
        }
    }

    private static class EndermanTakeBlockGoal extends Goal {
        private final EnderMan enderman;

        public EndermanTakeBlockGoal(EnderMan enderman) {
            this.enderman = enderman;
        }

        public boolean canUse() {
            return this.enderman.getCarriedBlock() != null ? false : (!this.enderman.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? false : this.enderman.getRandom().nextInt(reducedTickDelay(20)) == 0);
        }

        public void tick() {
            RandomSource randomsource = this.enderman.getRandom();
            Level world = this.enderman.level();
            int i = Mth.floor(this.enderman.getX() - 2.0 + randomsource.nextDouble() * 4.0);
            int j = Mth.floor(this.enderman.getY() + randomsource.nextDouble() * 3.0);
            int k = Mth.floor(this.enderman.getZ() - 2.0 + randomsource.nextDouble() * 4.0);
            BlockPos blockposition = new BlockPos(i, j, k);
            BlockState iblockdata = world.getBlockStateIfLoaded(blockposition);
            if (iblockdata != null) {
                Vec3 vec3d = new Vec3((double) this.enderman.getBlockX() + 0.5, (double) j + 0.5, (double) this.enderman.getBlockZ() + 0.5);
                Vec3 vec3d1 = new Vec3((double) i + 0.5, (double) j + 0.5, (double) k + 0.5);
                BlockHitResult movingobjectpositionblock = world.clip(new ClipContext(vec3d, vec3d1, net.minecraft.world.level.ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.enderman));
                boolean flag = movingobjectpositionblock.getBlockPos().equals(blockposition);
                if (iblockdata.is(BlockTags.ENDERMAN_HOLDABLE) && flag && CraftEventFactory.callEntityChangeBlockEvent(this.enderman, blockposition, iblockdata.getFluidState().createLegacyBlock())) {
                    world.removeBlock(blockposition, false);
                    world.gameEvent(GameEvent.BLOCK_DESTROY, blockposition, GameEvent.Context.of(this.enderman, iblockdata));
                    this.enderman.setCarriedBlock(iblockdata.getBlock().defaultBlockState());
                }

            }
        }
    }

    private static class EndermanLookForPlayerGoal extends NearestAttackableTargetGoal<Player> {
        private final EnderMan enderman;
        @Nullable
        private Player pendingTarget;
        private int aggroTime;
        private int teleportTime;
        private final TargetingConditions startAggroTargetConditions;
        private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();
        private final Predicate<LivingEntity> isAngerInducing;

        public EndermanLookForPlayerGoal(EnderMan enderman, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(enderman, Player.class, 10, false, false, targetPredicate);
            this.enderman = enderman;
            this.isAngerInducing = (entityliving) -> {
//                return (enderman.isLookingAtMe((Player)entityliving) || enderman.isAngryAt(entityliving)) && !enderman.hasIndirectPassenger(entityliving);
                return (enderman.isAngryAt(entityliving)) && !enderman.hasIndirectPassenger(entityliving);
            };
            this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(this.isAngerInducing);
        }

        public boolean canUse() {
            this.pendingTarget = this.enderman.level().getNearestPlayer(this.startAggroTargetConditions, this.enderman);
            return this.pendingTarget != null;
        }

        public void start() {
            this.aggroTime = this.adjustedTickDelay(5);
            this.teleportTime = 0;
            this.enderman.setBeingStaredAt();
        }

        public void stop() {
            this.pendingTarget = null;
            super.stop();
        }

        public boolean canContinueToUse() {
            if (this.pendingTarget != null) {
                if (!this.isAngerInducing.test(this.pendingTarget)) {
                    return false;
                } else {
                    this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
                    return true;
                }
            } else {
                if (this.target != null) {
                    if (this.enderman.hasIndirectPassenger(this.target)) {
                        return false;
                    }

                    if (this.continueAggroTargetConditions.test(this.enderman, this.target)) {
                        return true;
                    }
                }

                return super.canContinueToUse();
            }
        }
    }
}
