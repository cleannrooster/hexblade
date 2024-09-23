package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.SoundHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import static net.spell_engine.internals.SpellHelper.impactTargetingMode;
import static net.spell_engine.internals.SpellHelper.launchPoint;

public class RangedAttackMagister<E extends Magister> extends Goal {
    protected final E mob;
    private final double speed;
    private double tooclose = 4;
    private double toofar = 24;
    private int attackDelay = 0;
    public RangedAttackMagister(E mob, double speed, double tooclose, double toofar) {
        this.mob = mob;
        this.speed = speed;
        this.tooclose = tooclose;
        this.toofar = toofar;
    }
    @Override
    public boolean canStart() {
        return mob.isCaster;
    }

    @Override
    public boolean shouldContinue() {

        return super.shouldContinue() || (mob.getTarget() != null && mob.getTarget().distanceTo(mob) > tooclose && mob.getTarget().distanceTo(mob) < toofar);
    }

    @Override
    public void tick() {

        if(attackDelay <= 0){
            LivingEntity target = this.mob.getTarget();
            if(target  != null) {
                if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.ARCANE) {
                    Spell spell = SpellRegistry.getSpell(Identifier.of(SpellbladesAndSuch.MOD_ID, "arcane_missile"));
                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.ARCANE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.8, 0, 1), impactTargetingMode(spell));
                    Vec3d launchPoint = launchPoint(mob);
                    SpellProjectile projectile = new SpellProjectile(mob.getWorld(), mob, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, Identifier.of(SpellbladesAndSuch.MOD_ID, "arcane_missile"), target, context, new Spell.ProjectileData().perks);
                    Spell.Release.Target.ShootProjectile projectileData = spell.release.target.projectile;
                    projectileData.projectile.homing_angle = 15;
                    float velocity = projectileData.launch_properties.velocity;
                    float divergence = projectileData.projectile.divergence;
                    SoundHelper.playSoundEvent(mob.getWorld(), mob, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
                    Vec3d look = target.getBoundingBox().getCenter().subtract(launchPoint).normalize();
                    projectile.setVelocity(0, 1, 0, velocity, divergence);

                    projectile.range = spell.range;
                    projectile.getPitch(mob.getPitch());
                    projectile.setYaw(mob.getYaw());
                    mob.getWorld().spawnEntity(projectile);
                    attackDelay = 20;
                }
                if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FROST) {
                    Spell spell = SpellRegistry.getSpell(Identifier.of(SpellbladesAndSuch.MOD_ID, "frostbolt"));
                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FROST, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.6, 0, 1), impactTargetingMode(spell));
                    Vec3d launchPoint = launchPoint(mob);
                    SpellProjectile projectile = new SpellProjectile(mob.getWorld(), mob, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, Identifier.of(SpellbladesAndSuch.MOD_ID, "frostbolt"), target, context, new Spell.ProjectileData().perks);
                    Spell.Release.Target.ShootProjectile projectileData = spell.release.target.projectile;
                    projectileData.projectile.homing_angle = 15;
                    float velocity = projectileData.launch_properties.velocity;
                    float divergence = projectileData.projectile.divergence;
                    SoundHelper.playSoundEvent(mob.getWorld(), mob, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1.2F);
                    Vec3d look = target.getBoundingBox().getCenter().subtract(launchPoint).normalize();
                    projectile.setVelocity(0, 1, 0, velocity, divergence);

                    projectile.range = spell.range;
                    projectile.getPitch(mob.getPitch());
                    projectile.setYaw(mob.getYaw());
                    mob.getWorld().spawnEntity(projectile);
                    attackDelay = 10;
                }
                if (mob instanceof Magister reaver && reaver.getMagicSchool() == SpellSchools.FIRE) {
                    Spell spell = SpellRegistry.getSpell(Identifier.of(SpellbladesAndSuch.MOD_ID, "fireball"));
                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(SpellSchools.FIRE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 1, 0, 1), impactTargetingMode(spell));
                    Vec3d launchPoint = launchPoint(mob);
                    SpellProjectile projectile = new SpellProjectile(mob.getWorld(), mob, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, Identifier.of(SpellbladesAndSuch.MOD_ID, "fireball"), target, context, new Spell.ProjectileData().perks);
                    Spell.Release.Target.ShootProjectile projectileData = spell.release.target.projectile;
                    projectileData.projectile.homing_angle = 15;
                    float velocity = projectileData.launch_properties.velocity;
                    float divergence = projectileData.projectile.divergence;
                    SoundHelper.playSoundEvent(mob.getWorld(), mob, SoundEvents.ENTITY_BLAZE_SHOOT, 1, 1);
                    Vec3d look = target.getBoundingBox().getCenter().subtract(launchPoint).normalize();
                    projectile.setVelocity(0, 1, 0, velocity, divergence);

                    projectile.range = spell.range;
                    projectile.getPitch(mob.getPitch());
                    projectile.setYaw(mob.getYaw());
                    mob.getWorld().spawnEntity(projectile);
                    attackDelay = 40;
                }
            }
        }
        attackDelay--;
        super.tick();
    }
}
