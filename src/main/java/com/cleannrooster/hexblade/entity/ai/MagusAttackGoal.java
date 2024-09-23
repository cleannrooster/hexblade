package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.entity.Magus;
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
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.WorldScheduler;
import net.spell_engine.particle.Particles;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellDamageSource;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.cleannrooster.hexblade.entity.ai.MagusThrowGoal.rotate;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MagusAttackGoal<E extends Magus> extends Goal{
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
    private double tooCloseDistance = 4;
    private final int attackIntervalTicks = 20;
    private long lastUpdateTime;
    private static final long MAX_ATTACK_TIME = 20L;

    public MagusAttackGoal(E mob, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
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
        if(mob.getTarget() != null) {
            this.mob.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.mob.getTarget().getEyePos());
            if( !this.isTargetTooClose(this.mob)) {
                Vec3d vec31 = new Vec3d(mob.getTarget().getX() - this.mob.getX(), 0, mob.getTarget().getZ() - this.mob.getZ());
                Vec3d vec3 = new Vec3d(vec31.normalize().x * 1, 0.5, vec31.normalize().z * 1);
                this.mob.setPosition(this.mob.getPos().add(0, 1, 0));
                this.mob.setOnGround(false);
                this.mob.setVelocity(vec3);
            }
            mob.getDataTracker().set(Magus.TIER,2);
        }
    }

    public void stop() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.mob.setTarget((LivingEntity)null);
        }

        this.mob.setAttacking(false);
        this.mob.getNavigation().stop();
        this.time = 0;
        this.resetCooldown();
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    public void tick() {

        this.mob.getLookControl().tick();

        if(this.time == 5){
            this.mob.triggerAnim("slashtwo","slashtwo");
            this.spawnParticlesSlash(-90,-90);
            this.attack(false);
        }
        if(this.time == 10){
            this.mob.triggerAnim("slashthree","slashthree");
            this.spawnParticlesSlash(-90,0);
            this.attack(true);

        }
        if(this.time == 13){
            if(this.mob.getRandom().nextFloat() > 0.25){
                this.end = true;
            }
            else{

            }
        }
        if(this.time == 15){
            this.mob.triggerAnim("slashone","slashone");
            this.spawnParticlesSlash(-90,45);
            this.attack(false);

        }
        if(this.time == 20){
            this.mob.triggerAnim("slashtwo","slashtwo");
            this.spawnParticlesSlash(-90,-90);
            this.attack(false);

        }
        if(this.time == 25){
            this.mob.triggerAnim("slashthree","slashthree");
            this.spawnParticlesSlash(-90,0);
            this.attack(true);

        }
        if(this.time == 30){
            this.mob.triggerAnim("ryuenjin","ryuenjin");
            this.spawnParticlesSlash(-90,180);
            this.attack(true);

        }
        if(this.time == 35){
            this.end = true;
        }
        if(this.mob.isOnGround()){
            if(this.isTargetTooClose(this.mob)) {
                this.triggered = true;
            }
        }
        if(triggered) {
            if(this.time == 0){
                this.mob.triggerAnim("slashone","slashone");
                this.spawnParticlesSlash(-90,45);
                this.attack(false);



            }
            this.time++;
        }
        else{
            nontriggeredtime++;
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
                                serverWorld.spawnParticles(player , Particles.snowflake.particleType,true, vec3d4.getX(), vec3d4.getY(), vec3d4.getZ(), 1, 0, 0, 0, 0);
                            }
                            //serverWorld.spawnParticles(player,Particles.frost_shard.particleType, true, vec3d3.getX(), vec3d3.getY(), vec3d3.getZ(), 1, 0, 0, 0, 0);
                            serverWorld.spawnParticles(player,Particles.frost_shard.particleType, true, vec3d4.getX(), vec3d4.getY(), vec3d4.getZ(), 1, 0, 0, 0, 0);
                        }

                    }
                });

            }


        }
    }
    protected void attack(boolean narrow) {
        SoundHelper.playSoundEvent(this.mob.getWorld(), this.mob, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
        Spell.Release.Target.Area area = new Spell.Release.Target.Area();
        area.angle_degrees = 180;
        if(narrow){
            area.angle_degrees = 90;

        }
        Predicate<Entity> selectionPredicate = (person) -> {
            return !(person instanceof Magister);
        };
        List<Entity> list = TargetHelper.targetsFromArea(this.mob, this.mob.getBoundingBox().getCenter(), 4F, area, selectionPredicate);
        for (Entity entity : list) {
            entity.timeUntilRegen = 0;
            if (entity.damage(SpellDamageSource.mob(mob.getMagicSchool(), this.mob), (float)((float) 1F* this.mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 3F))) {
                entity.timeUntilRegen = 0;
                entity.damage(this.mob.getDamageSources().mobAttack(this.mob), (float) ( 0.5F * this.mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 3F));
            }
        }

    }

    protected void resetCooldown() {
        this.cooldown = this.getTickCount(50);
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
