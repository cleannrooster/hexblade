package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.HexbladePortal;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.hexblade.invasions.piglinsummon;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.EnumSet;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal.Control;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RetreatGoal extends Goal {
    private final Magister mob;
    private double x;
    private double y;
    private double z;
    private final double speed;
    private int retreatingtime = 0;

    public RetreatGoal(Magister mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean canStart() {
      if(((this.mob.getGroup().size() < 4 && this.mob.age > 100)  || (this.mob.age > 1000)) && this.mob.getLeader() != null && this.mob.getLeader().isAlive()  ){
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
        else if (this.mob.isLeader() && (this.mob.getGroup().isEmpty() || this.mob.getGroup().stream().noneMatch(Magister::isAlive))){
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
        else {
          return false;
        }

    }

    @Override
    public void stop() {
        retreatingtime = 0;
        super.stop();
    }

    @Override
    public void tick() {
        if ((((this.mob.getGroup().size() < 4 && this.mob.age > 100) ||(this.mob.age > 1000)) &&(this.mob.getLeader() != null && this.mob.getLeader().isAlive()))
    ||(this.mob.isLeader() && (this.mob.getGroup().isEmpty() || this.mob.getGroup().stream().noneMatch(Magister::isAlive)))) {

                if(retreatingtime > 100) {

                    if (!this.mob.isLeader() && this.mob.getWorld().getClosestEntity(HexbladePortal.class, TargetPredicate.DEFAULT, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().expand(2)) != null) {
                        this.mob.getWorld().playSound((PlayerEntity) null, this.mob.prevX, this.mob.prevY, this.mob.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.mob.getSoundCategory(), 1.0F, 1.0F);
                        this.mob.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

                        this.mob.discard();
                    }
                    if (this.mob.getGroup().isEmpty() || this.mob.getGroup().stream().noneMatch(LivingEntity::isAlive)) {
                        if (this.mob.getWorld().getClosestEntity(HexbladePortal.class, TargetPredicate.DEFAULT, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().expand(2)) != null) {
                            this.mob.getWorld().playSound((PlayerEntity) null, this.mob.prevX, this.mob.prevY, this.mob.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.mob.getSoundCategory(), 1.0F, 1.0F);
                            this.mob.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                            this.mob.discard();
                        }
                        ;
                    }
                }
                this.retreatingtime++;


        }

        super.tick();
    }


    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    public void start() {
        if ((((this.mob.getGroup().size() < 4 && this.mob.age > 100) ||(this.mob.age > 1000)) &&(this.mob.getLeader() != null && this.mob.getLeader().isAlive()))
        ||(this.mob.isLeader() && (this.mob.getGroup().isEmpty() || this.mob.getGroup().stream().noneMatch(Magister::isAlive)))) {
            this.mob.getNavigation().startMovingTo(this.x, this.y, this.z, this.speed);

        }

    }
}
