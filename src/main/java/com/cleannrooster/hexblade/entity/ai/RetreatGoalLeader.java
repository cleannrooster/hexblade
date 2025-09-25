package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.HexbladePortal;
import com.cleannrooster.hexblade.entity.Magister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class RetreatGoalLeader extends Goal {
    private final Magister mob;
    private double x;
    private double y;
    private double z;
    private final double speed;

    public RetreatGoalLeader(Magister mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean canStart() {
         if( this.mob.isLeader()&& ((this.mob.age > 200 && this.mob.getGroup().size() < 8 )|| this.mob.age > 60*20 )){
            Vec3d vec3d = NoPenaltyTargeting.findTo(this.mob, 16, 7, Vec3d.ofBottomCenter(this.mob.getPositionTarget()), 1.5707963705062866);
            if (vec3d == null) {
                return false;
            } else {
                this.x = vec3d.x;
                this.y = vec3d.y;
                this.z = vec3d.z;
                return true;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public void tick() {

        super.tick();
    }

    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    public void start() {

    }
}
