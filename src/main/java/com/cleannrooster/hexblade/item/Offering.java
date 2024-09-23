package com.cleannrooster.hexblade.item;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.HexbladePortal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Offering extends Item {
    public Offering(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getBlockPos() != null){
            if(context.getPlayer() != null && !context.getPlayer().isCreative()){
                context.getPlayer().getItemCooldownManager().set(this,20*60);
            }
            context.getPlayer().addStatusEffect(new StatusEffectInstance(Hexblade.MAGISTERFRIEND,20*60*5,0));
            HexbladePortal portal = new HexbladePortal(Hexblade.HEXBLADEPORTAL,context.getWorld());
            portal.setPos(context.getBlockPos().getX()+0.5,context.getBlockPos().getY()+1,context.getBlockPos().getZ()+0.5);
            float yaw = 360*context.getWorld().getRandom().nextFloat();
            portal.setYaw(yaw);
            portal.setBodyYaw(yaw);
            portal.setHeadYaw(yaw);
            portal.prevBodyYaw = yaw;
            portal.prevHeadYaw = yaw;
            portal.prevYaw = yaw;
            if(!context.getWorld().isClient()){
                context.getWorld().spawnEntity(portal);
            }
            ItemStack stack = context.getStack();
            stack.decrement(1);
            if (stack.isEmpty()) {
                context.getPlayer().getInventory().removeOne(stack);

            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {
        if(entity instanceof PlayerEntity player){
            if(player.hasStatusEffect(Hexblade.HEXED)){
                player.removeStatusEffect(Hexblade.HEXED);
                player.sendMessage(Text.translatable("Your patronage has saved you. For now."),true);
                itemStack.decrement(1);
            }
        }
        super.inventoryTick(itemStack, level, entity, i, bl);
    }
    @Override
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand interactionHand) {
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("Keep in inventory to ward away Hex, consuming the item when hexed."));
        tooltip.add(Text.translatable("Use on a block to summon a friendly Hexblade portal. Consumed when used."));

        super.appendTooltip(stack, context, tooltip, type);
    }


}
