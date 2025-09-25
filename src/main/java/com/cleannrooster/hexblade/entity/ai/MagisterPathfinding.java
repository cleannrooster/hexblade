package com.cleannrooster.hexblade.entity.ai;

import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MagisterPathfinding {

    @Nullable
    public static Vec3d getPathfindingTarget(PathAwareEntity pathAwareEntity) {
        if (pathAwareEntity.isInsideWaterOrBubbleColumn()) {
            Vec3d vec3d = find(pathAwareEntity, 64, 64);
            return vec3d == null ? getBackupPathfindingTarget(pathAwareEntity) : vec3d;
        } else {
            return pathAwareEntity.getRandom().nextFloat() >= 0.001 ? find(pathAwareEntity, 64, 64) : getBackupPathfindingTarget(pathAwareEntity);
        }

    }
    @Nullable
    public static Vec3d getBackupPathfindingTarget(PathAwareEntity pathAwareEntity) {
        return FuzzyTargeting.find(pathAwareEntity, 10, 7);
    }
    @Nullable
    public static Vec3d find(PathAwareEntity entity, int horizontalRange, int verticalRange) {
        Vec3d vec3d = null;
        for(int i = 0; i < 30  ; vec3d = NoPenaltyTargeting.find(entity, horizontalRange, verticalRange)){
            if (vec3d != null &&
                    vec3d.distanceTo(entity.getPos()) > horizontalRange*0.75F
                    &&
                    vec3d.distanceTo(entity.getPos()) < horizontalRange*1.25F
                    && FuzzyTargeting.validate(entity, BlockPos.ofFloored(vec3d)) != null) {
                return vec3d;


            }
            i++;

        }

        return getBackupPathfindingTarget(entity);
    }

}
