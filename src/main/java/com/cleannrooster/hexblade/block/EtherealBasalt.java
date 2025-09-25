package com.cleannrooster.hexblade.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EtherealBasalt extends PillarBlock {
    public static final MapCodec<EtherealBasalt> CODEC = createCodec(EtherealBasalt::new);

    public MapCodec<? extends EtherealBasalt> getCodec() {
        return CODEC;
    }

    public EtherealBasalt(Settings settings) {
        super(settings);

    }
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (!EnchantmentHelper.hasAnyEnchantmentsIn(tool, EnchantmentTags.PREVENTS_ICE_MELTING)) {
            if (world.getDimension().ultrawarm()) {
                world.removeBlock(pos, false);
                return;
            }

            BlockState blockState = world.getBlockState(pos.down());
            if (blockState.blocksMovement() || blockState.isLiquid()) {
                world.setBlockState(pos, getMeltedState());
            }
        }

    }

    @Override
    protected int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return super.getOpacity(state, world, pos);
    }

    protected boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            int i = 0;
            if (!world.getBlockState(pos.up()).isOf(state.getBlock())) {
                i++;
            }
            if (!world.getBlockState(pos.down()).isOf(state.getBlock())) {
                i++;

            }
            if (!world.getBlockState(pos.north()).isOf(state.getBlock())) {
                i++;

            }
            if (!world.getBlockState(pos.south()).isOf(state.getBlock())) {
                i++;

            }
            if (!world.getBlockState(pos.west()).isOf(state.getBlock())) {
                i++;

            }
            if (!world.getBlockState(pos.east()).isOf(state.getBlock())) {
                i++;

            }
            if (i >= 2) {

                this.melt(state, world, pos);
            }

    }
    public static BlockState getMeltedState() {
        return Blocks.WATER.getDefaultState();
    }

    protected void melt(BlockState state, World world, BlockPos pos) {
        if (world.getDimension().ultrawarm()) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, getMeltedState());
            world.updateNeighbor(pos, getMeltedState().getBlock(), pos);
        }
    }
}
