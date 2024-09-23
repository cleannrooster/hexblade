package com.cleannrooster.hexblade.entity.ai;

import com.cleannrooster.hexblade.Hexblade;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HexbladeNavigation extends MobNavigation {

    public HexbladeNavigation(MobEntity mobEntity, World world) {
        super(mobEntity, world);
    }
    public boolean isValidPosition(BlockPos pos) {
        BlockPos blockPos = pos.down();
        if(this.world.getServer() != null && this.world.equals(this.world.getServer().getWorld(Hexblade.DIMENSIONKEY))){
            return this.world.getBlockState(blockPos).isOpaqueFullCube(this.world, blockPos) || this.world.getBlockState(blockPos).getFluidState() != null;

        }
        return this.world.getBlockState(blockPos).isOpaqueFullCube(this.world, blockPos);
    }

}
