package com.cleannrooster.hexblade.item;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.hexblade.invasions.piglinsummon;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrismaticEffigy extends Item {
    public PrismaticEffigy(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand interactionHand) {

        if(level instanceof ServerWorld level1 ) {
            if((level1.getRegistryKey() != Hexblade.DIMENSIONKEY && !Hexblade.config.magusWeaker)){
                player.sendMessage(Text.translatable("Magus cannot be summoned outside the Glass Ocean."));
                return super.use(level, player, interactionHand);
            }
            if ( level1.getEntitiesByType(TypeFilter.instanceOf(Magus.class), archmagus -> archmagus.distanceTo(player) < 200).isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    BlockPos vec3 = piglinsummon.getSafePositionAroundPlayer2(level, player.getSteppingPos(), 10);
                    if (vec3 != null &&level.isSkyVisible(vec3.up()) &&  !level.isClient()) {
                        Magus magus = new Magus(Hexblade.ARCHMAGUS, level);
                        magus.setPosition(vec3.getX(), vec3.getY(), vec3.getZ());
                        if (!player.isCreative()) {
                            player.getStackInHand(interactionHand).decrement(1);
                            if (player.getStackInHand(interactionHand).isEmpty()) {
                                player.getInventory().removeOne(player.getStackInHand(interactionHand));
                            }
                            magus.spawnedfromitem = true;
                        }
                        if(level1.getRegistryKey() == Hexblade.DIMENSIONKEY) {
                            player.sendMessage(Text.translatable("Magus' full power is unleashed in the Glass Ocean!"));
                        }
                        else if(Hexblade.config.magusWeaker){
                            player.sendMessage(Text.translatable("This dimension seems to diminish Magus' power."));
                        }
                        if(magus.getWorld().isSkyVisible(magus.getBlockPos())){
                            magus.setPosition(magus.getPos().add(0,12,0));
                        }
                        magus.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES,player.getPos());
                        level.spawnEntity(magus);

                        return TypedActionResult.consume(player.getStackInHand(interactionHand));

                    }
                }
                player.sendMessage(Text.translatable("Magus has no room at your location"));
            } else {
                player.sendMessage(Text.translatable("Magus is already present within 200 blocks."));
            }
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("Use to summon Magus, if available."));
        tooltip.add(Text.translatable("Magus is stronger and drops more stuff in the Glass Ocean."));

        super.appendTooltip(stack, context, tooltip, type);
    }


}
