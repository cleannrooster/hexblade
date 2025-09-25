package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class WanderAroundPointOfInterestMagisterGoal  extends WanderAroundGoal{
    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
        private static final int HORIZONTAL_RANGE = 10;
        private static final int VERTICAL_RANGE = 7;

        public WanderAroundPointOfInterestMagisterGoal (PathAwareEntity entity, double speed, boolean canDespawn) {
            super(entity, speed, 120, canDespawn);
        }



    @Override
    public void tick() {
            super.tick();
    }

    @Nullable
        protected Vec3d getWanderTarget() {
            return MagisterPathfinding.getPathfindingTarget(this.mob);
        }

}
