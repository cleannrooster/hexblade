package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.mob.MobEntity;

public class AttackGoalMagister<E extends Magister> extends AttackGoal {
    private final E mob;
    public AttackGoalMagister(E mob) {
        super(mob);
        this.mob = mob;
    }


}
