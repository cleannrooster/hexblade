package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.WorldScheduler;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static com.cleannrooster.hexblade.entity.Magus.FLYING;
import static com.cleannrooster.hexblade.entity.ai.MagusThrowGoal.rotate;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static net.spell_engine.internals.SpellHelper.launchPoint;

public class MagusDivebombGoal<E extends Magus> extends Goal{
    protected final E mob;
    private final double speed;
    private final boolean pauseWhenMobIdle;
    private Path path;
    private int time = 0;
    private int realtime = 0;

    private boolean triggered = false;
    private int nontriggeredtime = 0;

    public boolean end = false;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateCountdownTicks;
    private int cooldown;
    private double tooCloseDistance = 5;
    private final int attackIntervalTicks = 20;
    private long lastUpdateTime;
    private static final long MAX_ATTACK_TIME = 20L;

    public MagusDivebombGoal(E mob, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        if( mob.age < 100){
            return false;
        }
        long l = this.mob.getWorld().getTime();
        this.cooldown--;
        if (l - this.lastUpdateTime < 20L || this.cooldown > 0) {
            return false;
        } else {
            this.lastUpdateTime = l;
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else {
                this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getSquaredMaxAttackDistance(livingEntity) >= this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                }
            }
        }
    }

    public boolean shouldContinue() {
        if(nontriggeredtime > 20){
            return false;
        }
        return !end;
    }

    public void start() {
        this.mob.setAttacking(true);
        this.realtime = 0;
        this.updateCountdownTicks = 0;
        this.triggered = false;
        this.nontriggeredtime = 0;
        this.end = false;
        this.cooldown = 0;
        if(mob.getTarget() != null && !this.mob.positions.isEmpty()) {
            this.mob.getDataTracker().set(FLYING,true);
            this.mob.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.mob.positions.get(0));
                Vec3d vec31 = new Vec3d(this.mob.positions.get(0).getX() - this.mob.getX(), 0, this.mob.positions.get(0).getZ() - this.mob.getZ());
                Vec3d vec3 = new Vec3d(vec31.normalize().x *0.2 , 2, vec31.normalize().z * 0.2);
                this.mob.setPosition(this.mob.getPos().add(0, 1, 0));
                this.mob.setOnGround(false);
                this.mob.setVelocity(vec3);
        }
        mob.getDataTracker().set(Magus.TIER,1);

    }

    public void stop() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.mob.setTarget((LivingEntity)null);
        }
        this.mob.getDataTracker().set(FLYING,false);

        this.mob.setAttacking(false);
        this.mob.getNavigation().stop();
        this.time = 0;
        this.resetCooldown();
    }

    public boolean shouldRunEveryTick() {
        return true;
    }
    public void tick() {

        if(this.mob.isOnGround()){
            this.triggered = true;
            Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(SpellbladesAndSuch.MOD_ID, "dragon_slam"));
            if(!this.mob.getWorld().isClient()) {
                ParticleHelper.sendBatches(this.mob, spell.release.particles);
            }

            List<Entity> entities = this.mob.getWorld().getEntitiesByClass(Entity.class,this.mob.getBoundingBox().expand(4,2,4), entity -> entity != this.mob);
            for(Entity entity : entities){
                entity.damage(SpellDamageSource.mob(SpellSchools.FIRE,this.mob),(float)this.mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
            }
            this.end = true;
        }
        else{
            if(mob.getTarget() != null) {

                if(!this.mob.positions.isEmpty()) {
                    Vec3d vec31 = new Vec3d(mob.positions.get(0).getX() - this.mob.getX(), mob.positions.get(0).getY() - this.mob.getY(), mob.positions.get(0).getZ() - this.mob.getZ());
                    Vec3d vec3 = new Vec3d(vec31.normalize().x * 0.2, vec31.normalize().y * 0.2, vec31.normalize().z * 0.2);
                    this.mob.addVelocity(vec3);
                }
            }
        }
    }
    public void shootMissile(boolean finalthrow){
        if(this.mob.getTarget() != null) {
            if (!finalthrow) {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(SpellbladesAndSuch.MOD_ID, "amethyst_slash"));
                SpellHelper.shootProjectile(mob.getWorld(),mob,null,SpellRegistry.from(mob.getWorld()).getEntry(Identifier.of(SpellbladesAndSuch.MOD_ID, "amethyst_slash")).get(),new SpellHelper.ImpactContext().power(new SpellPower.Result(SpellSchools.ARCANE,mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.8,0,0)));
                mob.getDataTracker().set(Magus.TIER,0);
            } else {
                Spell spell = SpellRegistry.from(mob.getWorld()).get(Identifier.of(SpellbladesAndSuch.MOD_ID, "flame_slash"));
                SpellHelper.shootProjectile(mob.getWorld(),mob,null,SpellRegistry.from(mob.getWorld()).getEntry(Identifier.of(SpellbladesAndSuch.MOD_ID, "flame_slash")).get(),new SpellHelper.ImpactContext().power(new SpellPower.Result(SpellSchools.ARCANE,mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.8,0,0)));

                mob.getDataTracker().set(Magus.TIER,1);

            }
        }
    }
    public void spawnParticlesSlash(float yaw, float pitch){

        int iii = -200;
        for (int i = 0; i < 5; i++) {

            for (int ii = 0; ii < 80; ii++) {

                iii++;

                int finalIii = iii;
                int finalI = i;
                int finalIi = ii;
                ((WorldScheduler)this.mob.getWorld()).schedule(i+1,() ->{
                    if(this.mob.getWorld() instanceof ServerWorld serverWorld) {
                        double x = 0;
                        double x2 = 0;

                        double z = 0;
                        x =  ((4.5*this.mob.getWidth() + 2*this.mob.getWidth() * sin(20 *  ((double) finalIii /(double)(4*31.74)))) * cos(((double) finalIii /(double)(4*31.74))));
                        x2 =  -((4.5*this.mob.getWidth() + 2*this.mob.getWidth() * sin(20 *  ((double) finalIii /(double)(4*31.74)))) * cos(((double) finalIii /(double)(4*31.74))));

                        z =  ((4.5*this.mob.getWidth() + 2*this.mob.getWidth() * sin(20 * ((double) finalIii /(double)(4*31.74)))) * sin(((double) finalIii /(double)(4*31.74))));
                        float f7 = this.mob.getYaw()+yaw % 360;
                        float f = pitch;
                        Vec3d vec3d = rotate(x,0,z,Math.toRadians(-f7),Math.toRadians(f+90),0);
                        Vec3d vec3d2 = rotate(x2,0,z,Math.toRadians(-f7),Math.toRadians(f+90),0);
                        Vec3d vec3d3 = vec3d.add(this.mob.getEyePos().getX(),this.mob.getEyeY(),this.mob.getEyePos().getZ());
                        Vec3d vec3d4 = vec3d2.add(this.mob.getEyePos().getX(),this.mob.getEyeY(),this.mob.getEyePos().getZ());

                        double y = this.mob.getY()+this.mob.getHeight()/2;




                        for(ServerPlayerEntity player : PlayerLookup.tracking(this.mob)) {
                            if (finalIi % 2 == 1) {
                                //serverWorld.spawnParticles(player, Particles.snowflake.particleType,true, vec3d3.getX(), vec3d3.getY(), vec3d3.getZ(), 1, 0, 0, 0, 0);
                                serverWorld.spawnParticles(player , SpellEngineParticles.snowflake.particleType(),true, vec3d4.getX(), vec3d4.getY(), vec3d4.getZ(), 1, 0, 0, 0, 0);
                            }
                            //serverWorld.spawnParticles(player,Particles.frost_shard.particleType, true, vec3d3.getX(), vec3d3.getY(), vec3d3.getZ(), 1, 0, 0, 0, 0);
                            serverWorld.spawnParticles(player,SpellEngineParticles.frost_shard.particleType(), true, vec3d4.getX(), vec3d4.getY(), vec3d4.getZ(), 1, 0, 0, 0, 0);
                        }

                    }
                });

            }


        }
    }
    protected void attack(LivingEntity target, double squaredDistance) {
        double d = this.getSquaredMaxAttackDistance(target);
        if (squaredDistance <= d && this.cooldown <= 0) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(target);
        }

    }

    protected void resetCooldown() {
        this.cooldown = this.getTickCount(60);
    }

    protected boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    protected int getCooldown() {
        return this.cooldown;
    }

    protected int getMaxCooldown() {
        return this.getTickCount(20);
    }

    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return (double)(this.mob.getWidth() * 2.0F * this.mob.getWidth() * 2.0F + entity.getWidth());
    }
    private boolean isTargetVisible(E mob) {
        if(mob.getTarget() != null) {

            return ((LivingTargetCache) mob.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).get()).contains(this.getTarget(mob).get());
        }
        return false;
    }

    private boolean isTargetTooClose(E mob) {
        if(mob.getTarget() != null) {
            return mob.getTarget().isInRange(mob, (double)this.tooCloseDistance);
        }
        return false;
    }

    private Optional<LivingEntity> getTarget(E mob) {
        return mob.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
