package com.cleannrooster.hexblade.item;

import com.cleannrooster.hexblade.Hexblade;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

import static com.extraspellattributes.ReabsorptionInit.*;

public class Ashes  extends TrinketItem {
    public Ashes(Settings settings) {
        super(settings);

    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if( (stack.getComponents() == null || stack.getComponents().get(DataComponentTypes.CUSTOM_DATA) == null)){
            NbtCompound nbt = new NbtCompound();
            switch (entity.getWorld().getRandom().nextInt(3)){
                case 0 -> nbt.putString("1","fire");
                case 1 -> nbt.putString("1", "frost");
                case 2 -> nbt.putString("1", "arcane");

            }
            switch (entity.getWorld().getRandom().nextInt(3)){
                case 0 -> nbt.putString("2", "fire");
                case 1 -> nbt.putString("2", "frost");
                case 2 -> nbt.putString("2", "arcane");


            }
            switch (entity.getWorld().getRandom().nextInt(3)){
                case 0 -> nbt.putString("3", "fire");
                case 1 -> nbt.putString("3", "frost");
                case 2 -> nbt.putString("3", "arcane");


            }

           nbt.putInt("1modifier", entity.getWorld().getRandom().nextBetween(Hexblade.config.ashes_lower_bound, Hexblade.config.ashes_upper_bound));
           nbt.putInt("2modifier", entity.getWorld().getRandom().nextBetween(Hexblade.config.ashes_lower_bound,Hexblade.config.ashes_upper_bound));
           nbt.putInt("3modifier", entity.getWorld().getRandom().nextBetween(Hexblade.config.ashes_lower_bound,Hexblade.config.ashes_upper_bound));
            stack.apply(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(nbt),UnaryOperator.identity());

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(stack.get(DataComponentTypes.CUSTOM_DATA) != null &&  stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().get("1") == null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().get("2") == null&& stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().get("3") == null){

            tooltip.add(Text.translatable("Unidentified."));
        }
        tooltip.add(Text.translatable("A single facet contains the wisdom").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("of countless disintegrated worlds.").formatted(Formatting.RED).formatted(Formatting.ITALIC));

        super.appendTooltip(stack, context, tooltip, type);
    }


    @Override
    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier slotIdentifier) {
        var modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
        // +10% movement speed
        if(stack.get(DataComponentTypes.CUSTOM_DATA) != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt() != null) {
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("1") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("1").equals("fire")) {
                modifiers.put(SpellSchools.FIRE.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromfire1"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("1modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("1") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("1").equals("frost")) {
                modifiers.put(SpellSchools.FROST.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromfrost1"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("1modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("1") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("1").equals("arcane")) {
                modifiers.put(SpellSchools.ARCANE.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromarcane1"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("1modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("2") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("2").equals("fire")) {
                modifiers.put(SpellSchools.FIRE.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromfire2"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("2modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("2") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("2").equals("frost")) {
                modifiers.put(SpellSchools.FROST.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromfrost2"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("2modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("2") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("2").equals("arcane")) {
                modifiers.put(SpellSchools.ARCANE.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromarcane2"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("2modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("3") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("3").equals("fire")) {
                modifiers.put(SpellSchools.FIRE.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromfire3"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("3modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("3") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("3").equals("frost")) {
                modifiers.put(SpellSchools.FROST.attributeEntry, new EntityAttributeModifier(Identifier.of("hexblade:convertfromfrost3"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("3modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("3") != null && stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getString("3").equals("arcane")) {
                modifiers.put(SpellSchools.ARCANE.attributeEntry, new EntityAttributeModifier( Identifier.of("hexblade:convertfromarcane3"), Math.max(Hexblade.config.ashes_lower_bound, Math.min(Hexblade.config.ashes_upper_bound, stack.get(DataComponentTypes.CUSTOM_DATA).getNbt().getInt("3modifier"))) * 0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }
        return modifiers;
    }


}
