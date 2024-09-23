package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magus;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import org.jetbrains.annotations.Nullable;

public class MagusFollowGoal extends Goal {
    public static final int HORIZONTAL_CHECK_RANGE = 8;
    public static final int VERTICAL_CHECK_RANGE = 4;
    public static final int MIN_DISTANCE = 3;
    private final HostileEntity magus;
    @Nullable
    private AnimalEntity parent;
    private final double speed;
    private int delay;

    public MagusFollowGoal(HostileEntity animal, double speed) {
        this.magus = animal;
        this.speed = speed;
    }

    public boolean canStart() {
        return this.magus.getTarget() != null && this.magus.getTarget().isAlive();
    }

    public boolean shouldContinue() {
        return this.magus.getTarget() != null && this.magus.getTarget().isAlive();

    }

    public void start() {
        this.delay = 0;
    }

    public void stop() {
        this.parent = null;
    }

    public void tick() {
        if(this.magus.getTarget() != null) {
            this.magus.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.magus.getTarget().getEyePos());
            this.magus.getNavigation().startMovingTo(this.magus.getTarget(), this.speed);

        }
    }
}
