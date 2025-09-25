package com.cleannrooster.hexblade.block;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.HexbladePortal;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class HexbladeBlockItem extends BlockItem {
    public HexbladeBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack itemStack, PlayerEntity player, LivingEntity livingEntity, Hand hand) {
        if(livingEntity instanceof HexbladePortal && Hexblade.config.glassocean){
            livingEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT,1,1);
            if(player.getWorld().isClient()) {
                return ActionResult.success(player.getWorld().isClient());
            }
            else{
                livingEntity.discard();
                RegistryKey<World> resourceKey = player.getWorld().getRegistryKey().equals(Hexblade.DIMENSIONKEY) ? World.OVERWORLD : Hexblade.DIMENSIONKEY;

                ServerWorld serverWorld = player.getEntityWorld().getServer().getWorld(resourceKey);
                if (serverWorld == null) {
                    return super.useOnEntity(itemStack, player, livingEntity, hand);
                }
                if (resourceKey == Hexblade.DIMENSIONKEY) {
                    TeleportTarget target = new TeleportTarget(serverWorld,
                            new net.minecraft.util.math.Vec3d(0, 65, 0),
                            net.minecraft.util.math.Vec3d.ZERO,
                            0.0F,
                            0.0F,
                            TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET
                    );
                    player.teleportTo(target);

                }
                if (player instanceof ServerPlayerEntity serverPlayer && resourceKey == World.OVERWORLD) {
                    TeleportTarget target;
                    if (serverPlayer.getSpawnPointPosition() != null) {
                        target = new TeleportTarget(serverWorld,
                                net.minecraft.util.math.Vec3d.ofCenter(serverPlayer.getSpawnPointPosition()),
                                net.minecraft.util.math.Vec3d.ZERO,
                                0.0F,
                                0.0F,
                                TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET
                        );
                    } else {
                        target = new TeleportTarget(serverWorld,
                                net.minecraft.util.math.Vec3d.ofCenter(serverWorld.getSpawnPos()),
                                net.minecraft.util.math.Vec3d.ZERO,
                                0.0F,
                                0.0F,
                                TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET
                        );
                    }
                    player.teleportTo(target);

                }
            }

        }
        return super.useOnEntity(itemStack, player, livingEntity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        HexbladePortal portal = new HexbladePortal(Hexblade.HEXBLADEPORTAL,world);
        portal.prevYaw = playerEntity.getYaw();
        portal.setYaw(playerEntity.getYaw());
        portal.headYaw = playerEntity.getYaw();
        portal.prevHeadYaw = playerEntity.getYaw();
        portal.prevBodyYaw = playerEntity.getYaw();

        portal.prevHeadYaw = playerEntity.headYaw;
        portal.setYaw(playerEntity.getYaw());
        portal.setPosition(playerEntity.getPos().add(playerEntity.getRotationVector().multiply(4)));
        portal.spawn = false;
        if(!world.isClient() && !playerEntity.getItemCooldownManager().isCoolingDown(this)){
            world.spawnEntity(portal);
            playerEntity.getItemCooldownManager().set(this,160);
            return super.use(world, playerEntity, hand);
        }
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        playerEntity.getItemCooldownManager().set(this,160);

        return TypedActionResult.success(itemStack,playerEntity.getWorld().isClient());

    }

}
