package com.cleannrooster.hexblade.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.ai.*;
import com.cleannrooster.hexblade.entity.ai.FollowGroupLeaderGoal;
import com.cleannrooster.hexblade.invasions.attackevent;
import com.cleannrooster.hexblade.invasions.piglinsummon;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.Spellblade;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.*;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.math.functions.limit.Min;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.WalkTowardsLookTargetTask;
import net.minecraft.entity.ai.brain.task.WalkTowardsPosTask;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_engine.utils.WorldScheduler;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.cleannrooster.hexblade.Hexblade.*;
import static com.cleannrooster.hexblade.effect.Effects.HEXED;
import static com.cleannrooster.hexblade.effect.Effects.MAGISTERFRIEND;

public class Magister extends HostileEntity implements InventoryOwner, GeoEntity, Merchant {
    public PlayerEntity nemesis;
    public int cooldown = 0;
    public boolean retreating = false;

    public boolean isthinking = false;
    public boolean isScout = false;
    public int rank = 0;
    private boolean hasntthrownitems = true;
    private boolean firstattack = false;
    private boolean secondattack = false;
    private boolean isstopped = false;
    public boolean isCaster = false;
    public boolean isTemporary = false;
    private PlayerEntity tradingplayer;
    float damagetakensincelastthink = 0;
    public int experiencePoints = 25;
    boolean isLeader;
    private Magister leader;
    private UUID groupUUID;
    private int leaderID;


    public Magister(EntityType<? extends Magister> p_34652_, World p_34653_) {
        super(p_34652_, p_34653_);
        this.lookControl = new MinibossLookControl(this);
    }

    @Override
    public double getTick(Object entity) {
        return GeoEntity.super.getTick(entity);
    }

    public void setTemporary(boolean temporary) {
        isTemporary = temporary;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    private final SimpleInventory inventory = new SimpleInventory(8);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    public boolean returninghome = false;

    public int homecount = 0;
    public int homecount2 = 0;
    public PlayerEntity hero = null;
    public boolean canGiveGifts = false;
    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);

    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.unknown.walk");
    public static final RawAnimation WALK2H = RawAnimation.begin().thenLoop("animation.unknown.walk_2h");


    public static final RawAnimation ATTACK = RawAnimation.begin().then("animation.mob.swing1", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation ATTACK2 = RawAnimation.begin().then("animation.mob.swing2", Animation.LoopType.PLAY_ONCE);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION,MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT);
    protected static final ImmutableList<SensorType<? extends Sensor<? super Magister>>> SENSOR_TYPES = ImmutableList.of(MagisterAI.MAGISTER_SENSOR_SENSOR_TYPE,SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);

    @Override
    public void setEquipmentDropChance(EquipmentSlot equipmentSlot, float f) {
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimAroundGoal(this,0.7F,120));

        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2,new RetreatGoal(this,1.2F));

        this.goalSelector.add(3, new AttackGoalMagister<>(this));
        this.goalSelector.add(4,new RangedAttackMagister<>(this,1.0F,2,32));
        this.goalSelector.add(5, new FollowGroupLeaderGoal(this));


        this.goalSelector.add(9, new WanderAroundFarMagisterGoal(this, 0.7));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true, entity ->  !entity.hasStatusEffect(MAGISTERFRIEND.registryEntry) && this.getLastAttacker() != null && this.getLastAttacker().equals(entity) ));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true,entity ->   !entity.hasStatusEffect(MAGISTERFRIEND.registryEntry)));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, LivingEntity.class, true, entity ->  !(entity instanceof Magus || entity instanceof Magister || entity instanceof CreeperEntity)&& this.getLastAttacker() != null && this.getLastAttacker().equals(entity)));


    }
    public static class MinibossLookControl extends LookControl {
        protected final MobEntity entity;
        protected float maxYawChange;
        protected float maxPitchChange;
        protected int lookAtTimer;
        protected double x;
        protected double y;
        protected double z;
        public MinibossLookControl(MobEntity entity) {
            super(entity);
            this.entity = entity;
        }

        public void lookAt(Vec3d direction) {
            this.lookAt(direction.x, direction.y, direction.z);
        }

        public void lookAt(Entity entity) {
            this.lookAt(entity.getX(), getLookingHeightFor(entity), entity.getZ());
        }

        public void lookAt(Entity entity, float maxYawChange, float maxPitchChange) {
            this.lookAt(entity.getX(), getLookingHeightFor(entity), entity.getZ(), maxYawChange, maxPitchChange);
        }

        public void lookAt(double x, double y, double z) {
            this.lookAt(x, y, z, (float)this.entity.getMaxLookYawChange(), (float)this.entity.getMaxLookPitchChange());
        }

        public void lookAt(double x, double y, double z, float maxYawChange, float maxPitchChange) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.maxYawChange = 30;
            this.maxPitchChange = 30;
            this.lookAtTimer = 8;
        }

        public void tick() {
            if (this.lookAtTimer > 0) {

                this.getTargetYaw().ifPresent((yaw) -> {
                    this.entity.setHeadYaw(this.changeAngle(this.entity.headYaw, yaw, this.maxYawChange));
                    this.entity.prevHeadYaw = this.entity.headYaw;
                });
                this.getTargetPitch().ifPresent((pitch) -> {
                    this.entity.setPitch(this.changeAngle(this.entity.getPitch(), pitch, this.maxPitchChange));
                    this.entity.prevPitch = this.entity.getPitch();

                });  } else {
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, 10.0F);
            }

            this.clampHeadYaw();
        }
        protected void clampHeadYaw() {
            if (!this.entity.getNavigation().isIdle()) {
                this.entity.headYaw = MathHelper.clampAngle(this.entity.headYaw, this.entity.bodyYaw, (float)this.entity.getMaxHeadRotation());
            }

        }

        protected boolean shouldStayHorizontal() {
            return false;
        }

        public boolean isLookingAtSpecificPosition() {
            return this.lookAtTimer > 0;
        }

        public double getLookX() {
            return this.x;
        }

        public double getLookY() {
            return this.y;
        }

        public double getLookZ() {
            return this.z;
        }

        protected Optional<Float> getTargetPitch() {
            double d = this.x - this.entity.getX();
            double e = this.y - this.entity.getEyeY();
            double f = this.z - this.entity.getZ();
            double g = Math.sqrt(d * d + f * f);
            return !(Math.abs(e) > 9.999999747378752E-6) && !(Math.abs(g) > 9.999999747378752E-6) ? Optional.empty() : Optional.of((float)(-(MathHelper.atan2(e, g) * 57.2957763671875)));
        }

        protected Optional<Float> getTargetYaw() {
            double d = this.x - this.entity.getX();
            double e = this.z - this.entity.getZ();
            return !(Math.abs(e) > 9.999999747378752E-6) && !(Math.abs(d) > 9.999999747378752E-6) ? Optional.empty() : Optional.of((float)(MathHelper.atan2(e, d) * 57.2957763671875) - 90.0F);
        }

        protected float changeAngle(float from, float to, float max) {
            float f = MathHelper.subtractAngles(from, to);
            float g = MathHelper.clamp(f, -max, max);
            return from + g;
        }

        private static double getLookingHeightFor(Entity entity) {
            return entity instanceof LivingEntity ? entity.getEyeY() : (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0;
        }
    }

    @Nullable
    public Magister getFollowing(){
        Optional<Magister> following = this.getGroup().stream().filter(mob -> { return mob.rank == this.rank-1;}).findFirst();

        return this.rank == 1 ? this.getLeader(): following.orElse(null);
    }


    @Override
    protected float getDropChance(EquipmentSlot equipmentSlot) {
        return 0;
    }

    public boolean isCaster() {
        return this.getDataTracker().get(CASTER);
    }
    public boolean isLeader() {
        return this.getDataTracker().get(LEADER);
    }
    public Magister  getLeader(){
        Magister leader = null;

        if(this.getWorld().getEntityById(this.getDataTracker().get(LEADERID)) instanceof Magister magister){
            leader = magister;
        }
        return leader;
    }

    @Override
    public ItemStack getOffHandStack() {
        if(this.isLeader()){
            return new ItemStack(HEXBLADEITEM);
        }
        return super.getOffHandStack();
    }

    public boolean hasLeader(){
        return this.getLeader() != null && this.getLeader().isAlive() && !this.isLeader();
    }
    public boolean isScout() {
        return this.isScout;
    }

    public boolean canGiveGifts() {
        return this.canGiveGifts;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Magister piglin) {
        Brain<?> brain = piglin.getBrain();
        Optional<LivingEntity> optional = LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && optional.get().distanceTo(piglin) < 32) {
            return optional;
        } else {
            Optional optional2;


            optional2 = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            return optional2;

        }
    }
    public void pullInOtherMagisters(Stream<? extends Magister> fish) {
        fish.forEach((fishx) -> {
            fishx.joinGroupOf(this);
        });
    }

    public Magister joinGroupOf(Magister groupLeader) {
        this.leader = groupLeader;
        return groupLeader;
    }
    static boolean isNearestValidAttackTarget(Magister piglin, LivingEntity livingEntity) {
        return findNearestValidAttackTarget(piglin).filter((livingEntity2) -> {
            return livingEntity2 == livingEntity;
        }).isPresent();
    }

    public void writeCustomDataToNbt(NbtCompound compoundTag) {

        if (this.getDataTracker().get(LEADER)) {
            compoundTag.putBoolean("Leader", this.getDataTracker().get(LEADER));
        } else {
            compoundTag.putBoolean("Leader", false);

        }

        compoundTag.putInt("leaderID", this.getDataTracker().get(LEADERID));
        this.getDataTracker().get(GROUPUUID).ifPresent(uuid1 ->
                compoundTag.putUuid("Uuid", uuid1));

        if (this.isCaster) {
            compoundTag.putBoolean("Caster", true);
        } else {
            compoundTag.putBoolean("Caster", false);

        }
        if (this.isTemporary) {
            compoundTag.putBoolean("Temporary", true);
        } else {
            compoundTag.putBoolean("Temporary", false);

        }
        if (this.isScout) {
            compoundTag.putBoolean("Scout", true);
        } else {
            compoundTag.putBoolean("Scout", false);

        }
        super.writeCustomDataToNbt(compoundTag);

    }

    @Override
    public boolean canTarget(LivingEntity target) {
        if(target instanceof Magister magister){
            return false;
        }
        return super.canTarget(target);
    }



    public static final TrackedData<Boolean> LEADER;
    public static final TrackedData<Boolean> CASTER;

    public static final TrackedData<Integer> LEADERID;

    public static final TrackedData<Optional<UUID>> GROUPUUID;

    static {
        LEADER = DataTracker.registerData(Magister.class, TrackedDataHandlerRegistry.BOOLEAN);
        CASTER = DataTracker.registerData(Magister.class, TrackedDataHandlerRegistry.BOOLEAN);

        LEADERID = DataTracker.registerData(Magister.class, TrackedDataHandlerRegistry.INTEGER);
        GROUPUUID = DataTracker.registerData(Magister.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    }
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LEADER,false);
        builder.add(CASTER,false);

        builder.add(LEADERID,0);
        builder.add(GROUPUUID,Optional.empty());

    }
    public void readCustomDataFromNbt(NbtCompound compoundTag) {
        this.isTemporary = compoundTag.getBoolean("Temporary");
        if (compoundTag.contains("Uuid")) {
            this.dataTracker.set(GROUPUUID, Optional.of(compoundTag.getUuid("Uuid")));
        }
        if (compoundTag.contains("leaderID")) {
            this.dataTracker.set(LEADERID, compoundTag.getInt("leaderID"));
        }
        if (compoundTag.contains("Leader")) {
            this.dataTracker.set(LEADER, compoundTag.getBoolean("Leader"));
        }
        this.isCaster = compoundTag.getBoolean("Caster");
        this.isScout = compoundTag.getBoolean("Scout");
        super.readCustomDataFromNbt(compoundTag);

    }

    @Override
    public int getMaxLookYawChange() {
        return 9999999;
    }

    protected boolean isImmuneToZombification() {
        return true;
    }

    public boolean attacking = false;
    public boolean justattacked = false;
    public int attackTicker = 0;
    public int attackTime = 0;
    protected EntityNavigation createNavigation(World world) {
        return new HexbladeNavigation(this, world);
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }



    @Override
    public void tickMovement() {
        tickHandSwing();
        super.tickMovement();
    }



    @Override
    public void tick() {
        if(!this.getWorld().isClient()) {
            cooldown++;
        }

        if(this.nemesis != null){
            this.setTarget(nemesis);
        }
        if(!this.isScout() && this.getMainHandStack().isEmpty() && !this.getWorld().isClient()){
            switch (this.getRandom().nextInt(3)) {
                case 0:
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(com.cleannrooster.spellblades.items.Items.arcane_blade.item()));
                    break;
                case 1:
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(com.cleannrooster.spellblades.items.Items.fire_blade.item()));
                    break;

                case 2:
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(com.cleannrooster.spellblades.items.Items.frost_blade.item()));
                    break;


            }

        }
        super.tick();
        if(this.getMainHandStack().isEmpty() && this.age < 200){
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,5,4));
        }





        if (age > 4000 && !this.getWorld().isClient() && this.isTemporary() ) {
            this.playSoundIfNotSilent(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
            this.discard();
        }





    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.ENTITY_PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
    }

    protected void playAngrySound() {
    }
    public SpellSchool getMagicSchool(){
        if(this.getMainHandStack().getItem() instanceof Spellblade spellblade){
            return spellblade.getSchool();
        }
        return SpellSchools.ARCANE;
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean bl) {
        if(damageSource.getAttacker() instanceof PlayerEntity player && player.hasStatusEffect(HEXED.registryEntry)){
            player.removeStatusEffect(HEXED.registryEntry);
        }
        if(damageSource.getAttacker() instanceof PlayerEntity player && player.hasStatusEffect(MAGISTERFRIEND.registryEntry)){
            player.removeStatusEffect(MAGISTERFRIEND.registryEntry);
        }
        super.dropLoot(damageSource, bl);
    }

    @Override
    public int getXpToDrop() {
        return experiencePoints;
    }

    @Override
    public boolean canSpawn(WorldAccess levelAccessor, SpawnReason mobSpawnType) {
        return super.canSpawn(levelAccessor, mobSpawnType);
    }

    @Override
    public boolean isInvulnerable() {
        return this.isLeader() && !this.getGroup().isEmpty();
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        if(this.getMainHandStack().isEmpty() && this.age < 200){
            return false;
        }
        if(damageSource.getSource() instanceof PlayerEntity player && this.isScout() && this.getHealth()/this.getMaxHealth() <= 0.5 && this.getMainHandStack().isEmpty()){
            this.tryEquip(new ItemStack(com.cleannrooster.spellblades.items.Items.arcane_blade.item()));

        }
        if(damageSource.getSource() instanceof PlayerEntity livingEntity && damageSource.getAttacker() instanceof PlayerEntity && livingEntity.hasStatusEffect(HEXED.registryEntry) && this.getHealth()/this.getMaxHealth() <= 0.5){
            if(livingEntity.getStatusEffect(HEXED.registryEntry) != null ){

                if(livingEntity instanceof PlayerEntity player && !player.getWorld().isClient()){

                        attackeventArrayList.add(new attackevent(player.getWorld(),player));
                        livingEntity.removeStatusEffect(HEXED.registryEntry);
                }
            }
        }
        if(damageSource.getSource() instanceof PlayerEntity player && damageSource.getAttacker() instanceof PlayerEntity && player.hasStatusEffect(MAGISTERFRIEND.registryEntry) && this.getHealth()/this.getMaxHealth() <= 0.5){
            player.removeStatusEffect(MAGISTERFRIEND.registryEntry);
        }
        return super.damage(damageSource, f);

    }


    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,0.5);
    }
    protected void tickHandSwing() {
        int i = 18;
        if (this.handSwinging) {
            ++this.handSwingTicks;

            if (this.handSwingTicks >= i) {
                this.handSwingTicks = 0;
                this.handSwinging = false;
            }
        } else {
            this.handSwingTicks = 0;
        }

        this.handSwingProgress = (float)this.handSwingTicks / (float)i;
    }
    @Override
    public void swingHand(Hand interactionHand, boolean bl) {
        if (!this.handSwinging || this.handSwingTicks >= 18 || this.handSwingTicks < 0) {

            this.handSwingTicks = -1;
            this.handSwinging = true;
            this.preferredHand = interactionHand;
            if (this.getWorld() instanceof ServerWorld) {
                EntityAnimationS2CPacket clientboundAnimatePacket = new EntityAnimationS2CPacket(this, interactionHand == Hand.MAIN_HAND ? 0 : 3);
                ServerChunkManager serverChunkCache = ((ServerWorld)this.getWorld()).getChunkManager();
                if (bl) {
                    serverChunkCache.sendToNearbyPlayers(this, clientboundAnimatePacket);
                } else {
                    serverChunkCache.sendToOtherNearbyPlayers(this, clientboundAnimatePacket);
                }
            }
        }

    }
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlay("animation.mob.idle");
    public static final RawAnimation IDLE2H = RawAnimation.begin().thenPlay("animation.unknown.idle_2h");

    public boolean   swingBool;

    @Override
    public boolean tryAttack(Entity entity) {
        if(entity instanceof LivingEntity living){
            RegistryEntry<Spell> spell = null;
            if(this.getMagicSchool().equals(SpellSchools.FIRE)){
                spell = SpellRegistry.from(living.getWorld()).getEntry(Identifier.of(MOD_ID,"flame_slash")).get();
            }
            if(this.getMagicSchool().equals(SpellSchools.ARCANE)){
                spell = SpellRegistry.from(living.getWorld()).getEntry(Identifier.of(MOD_ID,"amethyst_slash")).get();
            }
            if(this.getMagicSchool().equals(SpellSchools.FROST)){
                spell = SpellRegistry.from(living.getWorld()).getEntry(Identifier.of(MOD_ID,"frost_slash")).get();
            }
            if(spell != null) {
                SpellHelper.performImpacts(living.getWorld(), this, living, living, spell,spell.value().impacts, new SpellHelper.ImpactContext());
            }
        }
        if(swingBool){
            ((Magister)this).triggerAnim("swing1","swing1");
            swingBool = false;
            return super.tryAttack(entity);

        }
        else{
            ((Magister)this).triggerAnim("swing2","swing2");
            swingBool = true;
            return super.tryAttack(entity);

        }

    }

    protected static boolean canHarvest(Magister piglin){
        return piglin.getMainHandStack().getItem() instanceof HoeItem;
    }



    protected void loot(ItemEntity p_35467_) {


    }

    @Override
    public boolean isTeammate(Entity other) {
        if(other instanceof Magister || other instanceof Magus){
            return true;
        }
        return super.isTeammate(other);
    }

    protected void mobTick() {
        MagisterAI.updateActivity(this);

        {

            if (this.isLeader()&&((this.getGroup().size() < 4 && this.age > 100) || this.age > 1000)) {
                piglinsummon.summonNetherPortal(this.getWorld(),this,true).ifPresent((hexbladePortal -> {
                    if(this.cooldown > 100) {

                        hexbladePortal.leaderID=this.getId();
                        this.getWorld().spawnEntity(hexbladePortal);
                        this.cooldown = 0;
                    }
                }));

            }
        }
        super.mobTick();
    }
    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }




    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand interactionHand) {
        if (this.getOffers().isEmpty() || !this.getMainHandStack().isEmpty()) {
            return ActionResult.FAIL;
        } else {
                this.setCustomer(player);
                this.sendOffers(player, Text.translatable("Protection comes at a price"), 1);

            return ActionResult.SUCCESS;
        }
    }

    private PlayState predicate2(AnimationState<Magister> state) {
        if(state.isMoving()){
            if(this.isCaster()){
                return state.setAndContinue(WALK2H);


            }
            return state.setAndContinue(WALK);


        }
        return state.setAndContinue(IDLE);
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        animationData.add(new AnimationController<Magister>(this,"walk",
                0,this::predicate2)
        );
        animationData.add(
                new AnimationController<>(this, "swing1", event -> PlayState.CONTINUE)
                        .triggerableAnim("swing1", ATTACK));
        animationData.add(
                new AnimationController<>(this, "swing2", event -> PlayState.CONTINUE)
                        .triggerableAnim("swing2", ATTACK2));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }


    @Override
    public Text getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity player) {
        this.tradingplayer = player;
    }

    @Nullable
    @Override
    public PlayerEntity getCustomer() {
        return this.tradingplayer;
    }

    @Override
    public TradeOfferList getOffers() {
        TradeOfferList offers = new TradeOfferList();
        if(this.getMainHandStack().isEmpty()) {
            ItemStack offering = new ItemStack(Hexblade.OFFERING);
            offers.add(new TradeOffer(
                    new TradedItem(SpellbladesAndSuch.RUNEBLAZE, 2),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new TradedItem(SpellbladesAndSuch.RUNEGLEAM, 2),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new TradedItem(SpellbladesAndSuch.RUNEFROST, 2),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new TradedItem(com.cleannrooster.spellblades.items.Items.arcane_blade.item(), 1),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new TradedItem(com.cleannrooster.spellblades.items.Items.fire_blade.item(), 1),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new TradedItem(com.cleannrooster.spellblades.items.Items.frost_blade.item(), 1),
                    offering, 10, 8, 1F));

        }
        return offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList merchantOffers) {

    }

    @Override
    public void trade(TradeOffer merchantOffer) {

    }

    @Override
    public void onSellingItem(ItemStack itemStack) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int i) {

    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return null;
    }

    public void sendOffers(PlayerEntity player, Text component, int i) {
        OptionalInt optionalInt = player.openHandledScreen(new SimpleNamedScreenHandlerFactory((ix, inventory, playerx) -> {
            return new MerchantScreenHandler(ix, inventory, this);
        }, component));
        if (optionalInt.isPresent() && this.getMainHandStack().isEmpty()) {
            TradeOfferList merchantOffers = this.getOffers();
            if (!merchantOffers.isEmpty()) {
                player.sendTradeOffers(optionalInt.getAsInt(), merchantOffers, i, this.getExperience(), this.isLeveledMerchant(), this.canRefreshTrades());
            }
        }

    }

    @Override
    public boolean isClient() {
        return false;
    }



    public boolean isCloseEnoughToLeader() {
        return this.getLeader() != null && this.distanceTo(this.getLeader()) < 8;
    }

    public boolean hasOtherFishInGroup() {
       return !this.getGroup().stream().filter(magister -> magister != null && magister.isAlive()).toList().isEmpty();
    }
    public UUID getGroupUUID(){
        UUID id = UUID.randomUUID();
        if(this.getDataTracker().get(GROUPUUID).isPresent()){
            return this.getDataTracker().get(GROUPUUID).get();

        }
        return id;
    }

    public void setGroupUUID(UUID groupUUID) {
        this.groupUUID = groupUUID;
    }
    public void setGroup(UUID groupUUID){
        this.getDataTracker().set(GROUPUUID,Optional.of(groupUUID));
    }

    public List<Magister> getGroup() {
        List<Magister> group = new ArrayList<>();
        if(this.getServer() != null) {
            this.getServer().getWorlds().forEach(serverWorld -> serverWorld.iterateEntities().forEach(entity -> {
                if (entity instanceof Magister magister && !magister.isLeader() &&  magister.getGroupUUID() == this.getGroupUUID() && magister.isAlive()) {
                    group.add(magister);
                }
            }));
        }
        return group;
    }

    public void setLeader(int leaderUUid) {
        this.getDataTracker().set(LEADERID,leaderUUid);
    }
}

