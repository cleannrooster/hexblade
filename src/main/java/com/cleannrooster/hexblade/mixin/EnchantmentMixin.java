package com.cleannrooster.hexblade.mixin;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.HexbladeEnchantment;
import com.cleannrooster.hexblade.entity.MatchingFluidsBlockPredicateHexblade;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.enchantment.effect.entity.ReplaceDiskEnchantmentEffect;
import net.minecraft.enchantment.provider.SingleEnchantmentProvider;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.blockpredicate.OffsetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public class EnchantmentMixin {
    private static Optional<MatchingFluidsBlockPredicateHexblade> predicate = Optional.of(new MatchingFluidsBlockPredicateHexblade(new Vec3i(0,0,0), RegistryEntryList.of(Registries.FLUID.getEntry(Fluids.WATER))));

    @Inject(at = @At("HEAD"), method = "applyLocationBasedEffects", cancellable = true)
    private static void applyLocationBasedEffectsHexblade(ServerWorld world, LivingEntity user, CallbackInfo cir) {
        if(world.getRegistryKey().equals(Hexblade.DIMENSIONKEY)){
            {
                Vec3d pos = user.getPos();
                BlockPos blockPos = BlockPos.ofFloored(pos).add(new Vec3i(0, 0, 0));
                Random random = user.getRandom();
                int i = 4;
                int j = -4;
                Iterator var10 = BlockPos.iterate(blockPos.add(-i, 0, -i), blockPos.add(i, Math.min(j - 1, 0), i)).iterator();
                BlockState blockState = Hexblade.BASALT.getDefaultState();
                while (var10.hasNext()) {
                    BlockPos blockPos2 = (BlockPos) var10.next();
                    if (blockPos2.getSquaredDistanceFromCenter(pos.getX(), (double) blockPos2.getY() + 0.5, pos.getZ()) < (double) MathHelper.square(i) && (Boolean) predicate.map((predicate) -> {
                        return predicate.test(world, blockPos2);
                    }).orElse(true) && world.setBlockState(blockPos2, blockState)) {

                    }
                }
            }
            {
                Vec3d pos = user.getPos();
                BlockPos blockPos = BlockPos.ofFloored(pos).add(new Vec3i(0,-2,0));
                Random random = user.getRandom();
                int i = 6;
                int j = -3;
                Iterator var10 = BlockPos.iterate(blockPos.add(-i, 0, -i), blockPos.add(i, Math.min(j - 1, 0), i)).iterator();
                BlockState blockState =Hexblade.BASALT.getDefaultState();
                while(var10.hasNext()) {
                    BlockPos blockPos2 = (BlockPos)var10.next();
                    if (blockPos2.getSquaredDistanceFromCenter(pos.getX(), (double)blockPos2.getY() + 0.5, pos.getZ()) < (double) MathHelper.square(i) && (Boolean)predicate.map((predicate) -> {
                        return predicate.test(world, blockPos2);
                    }).orElse(true) && world.setBlockState(blockPos2, blockState)) {

                    }
                }
            }
        }

    }


}
