package com.cleannrooster.hexblade.item;

import com.cleannrooster.hexblade.Hexblade;
import com.extraspellattributes.ReabsorptionInit;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.List;
import java.util.function.UnaryOperator;

public class Omni extends TrinketItem {
    public Omni(Settings settings) {
        super(settings);

    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if( (stack.getComponents() == null || stack.getComponents().get(DataComponentTypes.CUSTOM_DATA) == null)){
            NbtCompound nbt = new NbtCompound();


           nbt.putInt("1modifier", entity.getWorld().getRandom().nextBetween(Hexblade.config.omni_lower, Hexblade.config.omni_upper));
            stack.apply(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(nbt),UnaryOperator.identity());

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        tooltip.add(Text.translatable("That winter, scorched refugees emerged from the shrine,").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("speaking only in strange tongues. They prayed to a new").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("symbol of power, not out of love, but out of fear.").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("attribute.name.hexblade.omni.desc").formatted(Formatting.DARK_RED));

        super.appendTooltip(stack, context, tooltip, type);
    }


    @Override
    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier slotIdentifier) {
        var modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
        // +10% movement speed
        if(stack.get(DataComponentTypes.CUSTOM_DATA) != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt() != null) {
            modifiers.put(Hexblade.OMNI, new EntityAttributeModifier(Identifier.of("hexblade:omni"), Math.max(Hexblade.config.omni_lower, Math.min(Hexblade.config.omni_upper, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("1modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_VALUE));

        }
            modifiers.put(SpellSchools.GENERIC.attributeEntry, new EntityAttributeModifier( Identifier.of("hexblade:spellminusomni"), -0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return modifiers;
    }


}
