package com.cleannrooster.hexblade.effect;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.invasions.attackevent;
import com.cleannrooster.hexblade.invasions.piglinsummon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.cleannrooster.hexblade.Hexblade.*;
import static com.cleannrooster.hexblade.effect.Effects.HEXED;
import static com.cleannrooster.hexblade.effect.Effects.MAGISTERFRIEND;


public class Hex extends StatusEffect {
    public Hex(StatusEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }


    @Override
    public boolean applyUpdateEffect(LivingEntity livingEntity, int i) {
        if(livingEntity instanceof PlayerEntity player && player.getWorld() instanceof ServerWorld level){
            List<Magister> magisterList = player.getWorld().getEntitiesByClass(Magister.class,player.getBoundingBox().expand(50,50,50), Magister::isScout);
            Magister reaver1 = player.getWorld().getClosestEntity(magisterList,TargetPredicate.DEFAULT,player,player.getX(),player.getY(),player.getZ());
            Magister reaver = new Magister(Hexblade.REAVER, player.getWorld());
            reaver.isScout = true;
            reaver.setTemporary(true);
            reaver.nemesis = player;
            BlockPos pos = piglinsummon.getSafePositionAroundPlayer(player.getWorld(), player.getSteppingPos(), 50);

            if (pos != null) {
                boolean bool = StreamSupport.stream(level.iterateEntities().spliterator(),true).toList().stream().noneMatch(entity -> entity instanceof Magister reaver2 && reaver2.isScout() && reaver2.nemesis == player);
                if(bool) {
                    reaver.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    reaver.setTarget(livingEntity);
                    player.getWorld().spawnEntity(reaver);
                }

            }
            if(reaver1 != null && !reaver1.returninghome && !player.hasStatusEffect(StatusEffects.INVISIBILITY)){
                reaver.setTarget(player);
            }

        }
        if(livingEntity.getStatusEffect(HEXED.registryEntry) != null && livingEntity.getStatusEffect(HEXED.registryEntry).getDuration() == 1){

            if(livingEntity instanceof PlayerEntity player && !player.getWorld().isClient()){
                Optional<BlockPos> pos = BlockPos.findClosest(player.getBlockPos(),64,128,
                        blockPos -> player.getWorld().getBlockState(blockPos).getBlock().equals(HEXBLADE));
                if(pos.isPresent()){
                    return false;
                }
                if(player.getInventory().containsAny(itemStack -> itemStack.isOf(HEXBLADEITEM))) {
                    return false;
                }
                if(!player.getInventory().containsAny(itemStack -> itemStack.isOf(Hexblade.OFFERING))){
                    attackeventArrayList.add(new attackevent(player.getWorld(),player));
                }
                else{
                    player.sendMessage(Text.translatable("Your patronage has saved you. For now."));
                    player.addStatusEffect(new StatusEffectInstance(MAGISTERFRIEND.registryEntry,20*60*5,0));

                    if(player instanceof ServerPlayerEntity player1) {
                        player1.getStatHandler().setStat(player1, Stats.CUSTOM.getOrCreateStat(SINCELASTHEX), 0);
                    }
                    if(player.getStackInHand(Hand.MAIN_HAND).isOf(Hexblade.OFFERING)) {
                        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                        stack.decrement(1);
                        if (stack.isEmpty()) {
                            player.getInventory().removeOne(stack);

                        }
                    }
                    else if(player.getStackInHand(Hand.OFF_HAND).isOf(Hexblade.OFFERING)) {
                        ItemStack stack = player.getStackInHand(Hand.OFF_HAND);
                        stack.decrement(1);


                    }
                    else {
                        for (int ii = 0; ii < player.getInventory().size(); ++ii) {
                            ItemStack stack = player.getInventory().getStack(ii);
                            if (stack.isOf(Hexblade.OFFERING)) {
                                stack.decrement(1);
                                if (stack.isEmpty()) {
                                    player.getInventory().removeOne(stack);
                                }
                                break;
                            }
                        }

                    }
                }
            }
        }
        return super.applyUpdateEffect(livingEntity, i);

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity player){
            player.sendMessage(Text.translatable("Hexblade Magister").formatted(Formatting.LIGHT_PURPLE).append(": \"Greetings, rogue mage. Pay us your dues or pay with your life. You have until the Hex expires.\""));
        }
        super.onApplied(entity, amplifier);
    }



}
