package com.cleannrooster.hexblade.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.ai.MagisterPathfinding;
import com.cleannrooster.spellblades.items.Items;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_power.mixin.attributes.CrossEntityAttributeInstance;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cleannrooster.hexblade.entity.Magister.CASTER;
import static com.cleannrooster.hexblade.entity.Magister.LEADER;

public class HexbladePortal extends HostileEntity implements GeoEntity {
    public PlayerEntity owner;
    public boolean spawn = true;
    public boolean firstPiglin = true;
    public boolean ishome = false;
    public int hometicks = 0;
    public boolean goinghome = false;
    public BlockPos origin = BlockPos.ORIGIN;
    public int experiencePoints = 160;
    public int leaderID;
    private UUID groupID;

    public HexbladePortal(EntityType<HexbladePortal> entityType, World world) {
        super(Hexblade.HEXBLADEPORTAL, world);
    }
    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);
    public static final RawAnimation FLYINGANIM = RawAnimation.begin().thenPlay("animation.model.dilate");
    public static final RawAnimation IDLEANIM = RawAnimation.begin().thenPlayAndHold("animation.model.idle");

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean canTakeDamage() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }



    public static final TrackedData<Integer> SPAWNED;

    @Override
    public boolean isCollidable() {
        return false;
    }
    static {
        SPAWNED = DataTracker.registerData(HexbladePortal.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SPAWNED,0);
    }

    public void setIshome(boolean ishome) {
        this.ishome = ishome;
    }

    public void readCustomDataFromNbt(NbtCompound compoundTag) {

        if (compoundTag.contains("Spawned")) {
            this.dataTracker.set(SPAWNED, compoundTag.getInt("Spawned"));
        }
        this.ishome = compoundTag.getBoolean("isHome");

    }
    public void writeCustomDataToNbt(NbtCompound compoundTag) {
        if(compoundTag.getBoolean("isHome")) {
            compoundTag.putBoolean("isHome", true);
        }
        else{
            compoundTag.putBoolean("isHome", false);

        }

        compoundTag.putInt("Spawned", (Integer) this.dataTracker.get(SPAWNED));
    }


        @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldDropXp() {
        return this.spawn;
    }

    @Override
    public int getXpToDrop() {
        return experiencePoints;
    }

    @Override
    public void equipStack(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }
    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,1);
    }    @Override
    public void tick() {
        if(firstUpdate){

            SoundEvent soundEvent = SoundEvents.ENTITY_BLAZE_SHOOT;
            this.playSound(soundEvent, 1F, 0.5F);
        }
        if(!this.getWorld().isClient() && this.age % 5 == 0) {
            for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(2))) {
                if (entity instanceof Magister magister && this.ishome) {
                    if (magister.isLeader() && (magister.getGroup().isEmpty() || magister.getGroup().stream().noneMatch(Magister::isAlive))) {
                        if (this.getWorld().isClient())
                            magister.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        magister.discard();
                    } else if (!magister.isLeader() && ((magister.getGroup().size() < 8) || magister.age > 1000)) {
                        magister.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        magister.discard();

                    }
                }
            }
        }
        for(int i = 0; i < 8; ++i) {
            double d = (double)this.getX() + random.nextDouble();
            double e = (double)this.getY() + random.nextDouble();
            double f = (double)this.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;

            f = (double)this.getZ() + 0.5D + 0.25D * (double)k;
            j = (double)(random.nextFloat() * 2.0F * (float)k);

            getWorld().addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }
        for(int i = 0; i < 8; ++i) {
            double d = (double)this.getX() + random.nextDouble();
            double e = (double)this.getY() + random.nextDouble();
            double f = (double)this.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;

            d = (double)this.getX() + 0.5D + 0.25D * (double)k;
            g = (double)(random.nextFloat() * 2.0F * (float)k);

            getWorld().addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }

        this.setNoGravity(true);
        this.noClip = true;

        if (this.spawn && this.getDataTracker().get(SPAWNED) < 8 && this.age < 200 && this.age % 10 == 5 && !this.ishome) {
            Magister piglin = new Magister(Hexblade.REAVER, this.getWorld());
            piglin.setTemporary(true);
            piglin.setPosition(this.getPos().add(0,0.5,0));

            if (firstPiglin) {

                piglin.getDataTracker().set(LEADER,true);
                piglin.tryEquip(new ItemStack(Hexblade.HEXBLADEITEM));
                Vec3d vec = MagisterPathfinding.getPathfindingTarget(piglin);
                if(vec != null) {
                    piglin.getNavigation().startMovingTo(vec.getX(), vec.getY(), vec.getZ(), 0.6F);
                }
                this.leaderID = piglin.getId();
                this.groupID = UUID.randomUUID();
            }
            if(!firstPiglin){
                piglin.tryEquip(new ItemStack(List.of(Items.arcane_blade.item(),Items.frost_blade.item(),Items.fire_blade.item()).get(getRandom().nextInt(3))));
                piglin.setLeader(this.leaderID);
                if (this.random.nextInt(5) < 2) {
                    piglin.getDataTracker().set(CASTER, true);

                    piglin.isCaster = true;
                }
            }

            piglin.setGroup(groupID);

            piglin.rank = this.getDataTracker().get(SPAWNED);
            this.getDataTracker().set(SPAWNED,this.getDataTracker().get(SPAWNED)+1);

            this.getWorld().spawnEntity(piglin);

            firstPiglin = false;
        }
        if (age > 300 && !this.ishome) {
            this.getWorld().playSound((PlayerEntity)null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            if( !this.getWorld().isClient()) {
                this.discard();
            }
        }
        if(this.ishome){
            if(this.getWorld().getEntityById(this.leaderID) instanceof Magister leader){
            for(Magister magister :leader.getGroup()){
                magister.retreating = true;
                magister.getNavigation().startMovingTo(this,1.2F);
            }
            leader.retreating = true;
                leader.getNavigation().startMovingTo(this,1.2F);

                if(!leader.isAlive() ||  (this.age > 600 || (!leader.isAlive() && (leader.getGroup().isEmpty() || leader.getGroup().stream().noneMatch(Magister::isAlive))))) {
                    if (!this.getWorld().isClient()) {
                        this.getWorld().playSound((PlayerEntity)null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                        this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

                        this.discard();
                    }
                }
            }
            else{
                if( !this.getWorld().isClient()) {
                    this.getWorld().playSound((PlayerEntity)null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

                    this.discard();
                }
                }
        }




        super.tick();
    }

    @Override
    public boolean collidesWith(Entity other) {

        return super.collidesWith(other);
    }


    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }



    @Override
    public void takeKnockback(double d, double e, double f) {
    }

    private <E extends GeoAnimatable> PlayState predicate2(AnimationState<E> event) {

        if(firstUpdate) {
            return event.setAndContinue(FLYINGANIM);
        }

        return PlayState.CONTINUE;

    }
    private <E extends GeoAnimatable> PlayState predicate3(AnimationState<E> event) {

        return event.setAndContinue(IDLEANIM);


    }
        @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
            animationData.add(new AnimationController<>(this, "fly",
                    0, this::predicate2)
            );
            animationData.add(new AnimationController<>(this, "idle",
                    0, this::predicate3)
            );
        }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

}
