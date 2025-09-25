package com.cleannrooster.hexblade.item;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.Orb;
import com.cleannrooster.spellblades.items.Spellblade;
import com.extraspellattributes.ReabsorptionInit;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class Items {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory item, WeaponConfig defaults) {
        return entry(null, name, material, item, defaults);
    }

    private static Weapon.Entry entry(String requiredMod, String name, Weapon.CustomMaterial material, Weapon.Factory item, WeaponConfig defaults) {
        var entry = new Weapon.Entry(Hexblade.MOD_ID, name, material, item, defaults, Equipment.WeaponType.SPELL_BLADE);
        if (entry.isRequiredModInstalled()) {
            entries.add(entry);
        }
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString) {
        return ingredient(idString, net.minecraft.item.Items.DIAMOND);
    }

    private static Supplier<Ingredient> ingredient(String idString, Item fallback) {
        var id = Identifier.of(idString);
        return () -> {
            var item = Registries.ITEM.get(id);
            var ingredient = item != null ? item : fallback;
            return Ingredient.ofItems(ingredient);
        };
    }

    private static final float bladeValue = 2F;
    private static final float bladeDamage = 2;
    private static final float claymoreDamage = 4F;
    private static final float bladeSpeed = -3;
    private static final float claymoreSpeed = -3;



    private static Weapon.Entry voidforge(String requiredMod, String name, Weapon.CustomMaterial material, float damage, SpellSchool school) {
        var settings = new Item.Settings();
        return entry(requiredMod, name, material, Voidforge::new, new WeaponConfig(damage, -2.4F))
                .loot(Equipment.LootProperties.of(6));
    }

    private static Weapon.Entry starforge(String requiredMod, String name, Weapon.CustomMaterial material, float damage, SpellSchool school) {
        var settings = new Item.Settings();
        return entry(requiredMod, name, material, Starforge::new, new WeaponConfig(damage, -3F))
                .loot(Equipment.LootProperties.of(6));
    }
    public static final Weapon.Entry starforge = starforge(null,"starforge",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.NETHER_STAR)), 9F, SpellSchools.FROST)
            .attribute(AttributeModifier.bonus((SpellSchools.FROST).id, 4))
            .attribute(AttributeModifier.bonus((SpellSchools.ARCANE).id, 4))
            .attribute(AttributeModifier.bonus((SpellSchools.FIRE).id, 4))
            .loot(Equipment.LootProperties.of(6));

    public static final Weapon.Entry voidforge = voidforge(null,"voidforge",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.END_CRYSTAL)), 0F, SpellSchools.FROST)
            .attribute(AttributeModifier.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofrost"), 1F))
            .attribute(AttributeModifier.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoarcane"), 1F))
            .attribute(AttributeModifier.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofire"), 1F))
            .loot(Equipment.LootProperties.of(6));




    public static void register(Map<String, WeaponConfig> configs) {
        Weapon.register(configs, entries, RegistryKey.of(Registries.ITEM_GROUP.getKey(),Identifier.of(Hexblade.MOD_ID,"generic")));
    }
}

