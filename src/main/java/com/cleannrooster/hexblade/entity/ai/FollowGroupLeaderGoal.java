package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.task.PanicTask;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.SchoolingFishEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FollowGroupLeaderGoal extends Goal {
    private static final int MIN_SEARCH_DELAY = 200;
    private final Magister fish;
    private int moveDelay;
    private int checkSurroundingDelay;

    public FollowGroupLeaderGoal(Magister fish) {
        this.fish = fish;
        this.checkSurroundingDelay = this.getSurroundingSearchDelay(fish);
    }

    protected int getSurroundingSearchDelay(Magister fish) {
        return toGoalTicks(200 + fish.getRandom().nextInt(200) % 20);
    }
    public boolean canStart() {
        if(this.fish.getFollowing() != null && this.fish.distanceTo(this.fish.getFollowing()) < 4){
            return false;
        }
        if (this.fish.hasLeader()) {
            return true;
        }
            if (this.fish.hasLeader()) {
                return true;
            } else if (this.checkSurroundingDelay > 0) {
                --this.checkSurroundingDelay;
                return false;

        } else {
            this.checkSurroundingDelay = this.getSurroundingSearchDelay(this.fish);
            Predicate<Magister> predicate = (fish) -> {
                return  !fish.hasLeader();
            };
            List<? extends Magister> list = this.fish.getWorld().getEntitiesByClass(this.fish.getClass(), this.fish.getBoundingBox().expand(16.0, 16.0, 16.0), predicate);
            Magister schoolingFishEntity = (Magister) DataFixUtils.orElse(list.stream().findAny(), this.fish);
            schoolingFishEntity.pullInOtherMagisters(list.stream().filter((fish) -> {
                return !fish.hasLeader();
            }));
            return this.fish.hasLeader();
        }
    }

    public boolean shouldContinue() {
        return this.fish.getFollowing() != null && this.fish.distanceTo(this.fish.getFollowing()) < 4;
    }

    public void start() {
        this.moveDelay = 0;
    }

    public void stop() {
        this.fish.getNavigation().stop();
    }

    @Override
    public boolean canStop() {
        return this.fish.getFollowing() != null && this.fish.distanceTo(this.fish.getFollowing()) < 4;
    }

    public void tick() {
        if (--this.moveDelay <= 0 && this.fish.getFollowing() != null && this.fish.distanceTo(this.fish.getFollowing()) > 4 && !this.fish.retreating) {

            this.moveDelay = this.getTickCount(10);
            this.moveTowardLeader();
        }
    }
    public void moveTowardLeader() {
        if (this.fish.hasLeader()) {

            Optional<Magister> magisterFind = this.fish.getGroup().stream().filter(magister -> {

                return magister.rank == this.fish.rank-1;
            }).findFirst();
            if(magisterFind.isPresent()) {
                this.fish.getNavigation().startMovingTo(magisterFind.get(), 0.8);
            }
            else{
                this.fish.getNavigation().startMovingTo( this.fish.getLeader(),0.8);

            }
        }

    }
}
