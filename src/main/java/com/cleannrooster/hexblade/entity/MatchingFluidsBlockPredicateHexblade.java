package com.cleannrooster.hexblade.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.blockpredicate.OffsetPredicate;

public class MatchingFluidsBlockPredicateHexblade extends OffsetPredicate {
    private final RegistryEntryList<Fluid> fluids;
    public static final MapCodec<MatchingFluidsBlockPredicateHexblade> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return registerOffsetField(instance).and(RegistryCodecs.entryList(RegistryKeys.FLUID).fieldOf("fluids").forGetter((predicate) -> {
            return predicate.fluids;
        })).apply(instance, MatchingFluidsBlockPredicateHexblade::new);
    });

    public MatchingFluidsBlockPredicateHexblade(Vec3i offset, RegistryEntryList<Fluid> fluids) {
        super(offset);
        this.fluids = fluids;
    }

    protected boolean test(BlockState state) {
        return state.getFluidState().isIn(this.fluids);
    }

    public BlockPredicateType<?> getType() {
        return BlockPredicateType.MATCHING_FLUIDS;
    }
}