package com.cleannrooster.hexblade.entity;

import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class WanderAroundGoalHexblade extends WanderAroundGoal {
    public WanderAroundGoalHexblade(PathAwareEntity mob, double speed) {
        super(mob, speed);
    }


}
