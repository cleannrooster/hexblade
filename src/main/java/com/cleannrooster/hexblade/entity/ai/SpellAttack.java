package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_engine.utils.SoundHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import static net.spell_engine.internals.SpellHelper.launchPoint;

public class SpellAttack<E extends MobEntity, T extends LivingEntity> extends MultiTickTask<E> {
    private static final int TIMEOUT = 1200;
    private int attackDelay = 20;
    private CrossbowState crossbowState;
    public SpellAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT), 1200);
        this.crossbowState = CrossbowState.UNCHARGED;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, E entity) {
        return  entity instanceof Magister reaver && reaver.isCaster();
    }

    protected boolean checkExtraStartConditions(ServerWorld serverLevel, E mob) {

        return  mob instanceof Magister reaver && reaver.isCaster();
    }

    protected boolean shouldKeepRunning(ServerWorld serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel, mob);
    }

    protected void keepRunning(ServerWorld serverLevel, E mob, long l) {
        LivingEntity livingEntity = getAttackTarget(mob);
        this.lookAtTarget(mob, livingEntity);
        this.crossbowAttack(mob, livingEntity);
    }

    protected void finishRunning(ServerWorld serverLevel, E mob, long l) {
        if (mob.isUsingItem()) {
            mob.clearActiveItem();
        }


    }

    private void crossbowAttack(E mob, LivingEntity target) {
        if(attackDelay <= 0) {
            if(mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.ARCANE) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "amethyst_slash"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.ARCANE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.8, 0, 1), SpellTarget.FocusMode.DIRECT,0);
                SpellHelper.shootProjectile(mob.getWorld(),(PlayerEntity)(LivingEntity)reaver,null,SpellRegistry.from(mob.getWorld()).getEntry(spell),context);
                attackDelay = 20;
            }
            if(mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FROST) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "frost_slash"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FROST, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.6, 0, 1), SpellTarget.FocusMode.DIRECT,0);
                SpellHelper.shootProjectile(mob.getWorld(),(PlayerEntity)(LivingEntity)reaver,null,SpellRegistry.from(mob.getWorld()).getEntry(spell),context);
                attackDelay = 10;
            }
            if(mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FIRE) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "flame_slash"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FIRE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*1, 0, 1), SpellTarget.FocusMode.DIRECT,0);
                SpellHelper.shootProjectile(mob.getWorld(),(PlayerEntity)(LivingEntity)reaver,null,SpellRegistry.from(mob.getWorld()).getEntry(spell),context);

                attackDelay = 40;
            }
        }
            attackDelay--;
    }

    private void lookAtTarget(MobEntity mob, LivingEntity livingEntity) {
        mob.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(livingEntity, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity livingEntity) {
        return (LivingEntity)livingEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    static enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;

        private CrossbowState() {
        }
    }
}

