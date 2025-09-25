package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
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

public class RangedAttackMagister<E extends Magister> extends Goal {
    protected final E mob;
    private final double speed;
    private double tooclose = 4;
    private double toofar = 24;
    private int attackDelay = 0;
    private boolean direction = true;
    public RangedAttackMagister(E mob, double speed, double tooclose, double toofar) {
        this.mob = mob;
        this.speed = speed;
        this.tooclose = tooclose;
        this.toofar = toofar;
    }
    @Override
    public boolean canStart() {
        return mob.isCaster && (mob.getTarget() != null && mob.getTarget().distanceTo(mob) > tooclose && mob.getTarget().distanceTo(mob) < toofar);
    }

    @Override
    public boolean shouldContinue() {

        return  (mob.getTarget() != null && mob.getTarget().distanceTo(mob) > tooclose && mob.getTarget().distanceTo(mob) < toofar);
    }

    @Override
    public void tick() {

        super.tick();
        if(this.mob.raycast(this.mob.distanceTo(this.mob.getTarget()),0,false) instanceof EntityHitResult entityHitResult){
            if(entityHitResult.getEntity() != null && entityHitResult.getEntity() instanceof Magister){
                this.mob.getMoveControl().strafeTo(1.5F,this.mob.age % 40 > 20  ? -1 : 1);
            }
            else     if (attackDelay <= 0) {
                if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.ARCANE) {
                    Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "amethyst_slash"));
                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.ARCANE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.8, 0, 1), SpellTarget.FocusMode.DIRECT, 0);
                    SpellHelper.shootProjectile(mob.getWorld(), reaver, mob.getTarget(), SpellRegistry.from(mob.getWorld()).getEntry(spell), context);
                    attackDelay = 20;
                }
                if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FROST) {
                    Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "frost_slash"));
                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FROST, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.6, 0, 1), SpellTarget.FocusMode.DIRECT, 0);
                    SpellHelper.shootProjectile(mob.getWorld(), reaver, mob.getTarget(), SpellRegistry.from(mob.getWorld()).getEntry(spell), context);
                    attackDelay = 10;
                }
                if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FIRE) {
                    Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "flame_slash"));
                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FIRE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 1, 0, 1), SpellTarget.FocusMode.DIRECT, 0);
                    SpellHelper.shootProjectile(mob.getWorld(), reaver, mob.getTarget(), SpellRegistry.from(mob.getWorld()).getEntry(spell), context);

                    attackDelay = 40;
                }
            }


        }
        else if (attackDelay <= 0) {
            if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.ARCANE) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "amethyst_slash"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.ARCANE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.8, 0, 1), SpellTarget.FocusMode.DIRECT, 0);
                SpellHelper.shootProjectile(mob.getWorld(), reaver, mob.getTarget(), SpellRegistry.from(mob.getWorld()).getEntry(spell), context);
                attackDelay = 20;
            }
            if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FROST) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "frost_slash"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FROST, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.6, 0, 1), SpellTarget.FocusMode.DIRECT, 0);
                SpellHelper.shootProjectile(mob.getWorld(), reaver, mob.getTarget(), SpellRegistry.from(mob.getWorld()).getEntry(spell), context);
                attackDelay = 10;
            }
            if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FIRE) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(Hexblade.MOD_ID, "flame_slash"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FIRE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 1, 0, 1), SpellTarget.FocusMode.DIRECT, 0);
                SpellHelper.shootProjectile(mob.getWorld(), reaver, mob.getTarget(), SpellRegistry.from(mob.getWorld()).getEntry(spell), context);

                attackDelay = 40;
            }
            if(this.mob.swingBool){
                ((Magister)this.mob).triggerAnim("swing1","swing1");
                this.mob.swingBool = false;

            }
            else{
                ((Magister)this.mob).triggerAnim("swing2","swing2");
                this.mob.swingBool = true;

            }

        }


        attackDelay--;

    }
}
