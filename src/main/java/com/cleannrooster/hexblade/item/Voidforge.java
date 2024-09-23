package com.cleannrooster.hexblade.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static com.extraspellattributes.ReabsorptionInit.*;

public class Voidforge extends Starforge  {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public Voidforge(ToolMaterial toolMaterial, Item.Settings settings) {
        super(toolMaterial, settings);
    }





    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {

        super.inventoryTick(itemStack, level, entity, i, bl);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

            return super.postHit(stack,target,attacker);
    }

    SpellSchool school = SpellSchools.ARCANE;

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("Triggers Elemental Novas on hit, with a 1 second base cooldown and a 0.8 coefficient."));
        tooltip.add(Text.translatable("Requires runes of the right type, or Spell Infinity."));
        tooltip.add(Text.translatable("A weapon born of nothingness,").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("can only create more nothingness.").formatted(Formatting.RED).formatted(Formatting.ITALIC));

        super.appendTooltip(stack, context, tooltip, type);
    }

}
