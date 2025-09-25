package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WanderAroundFarMagisterGoal extends WanderAroundGoal {
    public static final float CHANCE = 0.001F;
    protected final float probability;

    public WanderAroundFarMagisterGoal(PathAwareEntity pathAwareEntity, double d) {
        this(pathAwareEntity, d, 0.001F);
    }

    public WanderAroundFarMagisterGoal(PathAwareEntity mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Override
    public boolean canStart() {
        if(this.mob instanceof Magister magister){
            Optional<Magister> following = magister.getGroup().stream().filter(mob -> { return mob.rank == magister.rank-1;}).findFirst();
            if(following.isPresent()){
                if(this.mob.distanceTo(following.get())<4){
                    return false;
                }
            }
        }
        return super.canStart();
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        return MagisterPathfinding.getPathfindingTarget(this.mob);
    }
}
