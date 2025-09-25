package com.cleannrooster.hexblade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.enchantment.effect.entity.ReplaceDiskEnchantmentEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class HexbladeEnchantment {
    public HexbladeEnchantment(){

    }

    public static final RegistryKey<Enchantment> PILLARING = of("pillaring");
    public static final MapCodec<ReplaceDiskEnchantmentEffect> CODEC = ReplaceDiskEnchantmentEffect.CODEC;

    private static RegistryKey<Enchantment> of(String path) {
        Identifier id = Identifier.of(Hexblade.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }
    public static MapCodec<ReplaceDiskEnchantmentEffect> register(String id) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(Hexblade.MOD_ID, id), CODEC);
    }

}