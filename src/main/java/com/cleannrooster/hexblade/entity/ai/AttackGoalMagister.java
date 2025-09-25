package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class AttackGoalMagister<E extends Magister> extends AttackGoal {
    private final E mob;
    private LivingEntity target;
    private int cooldown;
    public AttackGoalMagister(E mob) {
        super(mob);
        this.mob = mob;
    }
    public void tick() {
        if(this.mob.retreating){
            return;
        }
        if(this.mob.getTarget() != null) {
            this.mob.getLookControl().lookAt(this.mob.getTarget(), 30.0F, 30.0F);
            double d = (double) (this.mob.getWidth() * 2.0F * this.mob.getWidth() * 2.0F);
            double e = this.mob.squaredDistanceTo(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ());
            double f = 1;
            this.mob.getNavigation().startMovingTo(this.mob.getTarget(), f);
            if(this.mob instanceof Magister magister && magister.isCaster()){
                if(this.mob.getTarget() != null && magister.distanceTo(this.mob.getTarget()) < 16 ){
                    this.mob.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, magister.getTarget().getPos());
                    if(this.mob.getTarget() != null) {
                        this.mob.getMoveControl().strafeTo((magister.distanceTo(this.mob.getTarget()) < 8) ? -1: 1, this.mob.getTarget().getPos().subtract(this.mob.getPos()).crossProduct(new Vec3d(0, 1, 0)).dotProduct(this.mob.getRotationVector()) > 0 ? -1 : 1);
                    }
                    this.mob.getNavigation().stop();
                }
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
            if (!(e > d)) {
                if (this.cooldown <= 0) {
                    this.cooldown = 20;
                    this.mob.tryAttack(this.mob.getTarget());
                }
            }
        }
    }

}
