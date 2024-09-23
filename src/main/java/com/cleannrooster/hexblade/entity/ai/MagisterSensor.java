package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.entity.Magus;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class MagisterSensor extends Sensor<LivingEntity> {
     public MagisterSensor() {
    }

    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
    }

    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        List<LivingEntity> list = Lists.newArrayList();
        LivingTargetCache livingTargetCache = (LivingTargetCache)brain.getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());
        Optional var10000 = livingTargetCache.findFirst((livingEntityx) -> {
            return livingEntityx instanceof Monster && !(livingEntityx instanceof Magister) && !(livingEntityx instanceof Magus);
        });
        Objects.requireNonNull(MobEntity.class);
        Optional<MobEntity> optional = var10000.map(MobEntity.class::cast);
        List<LivingEntity> list2 = (List)brain.getOptionalRegisteredMemory(MemoryModuleType.MOBS).orElse(ImmutableList.of());
        Iterator var8 = list2.iterator();

        while(var8.hasNext()) {
            LivingEntity livingEntity = (LivingEntity)var8.next();
            if (livingEntity instanceof Monster && !(livingEntity instanceof Magister)) {
                list.add(livingEntity);
            }
        }

        brain.remember(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
    }
}
