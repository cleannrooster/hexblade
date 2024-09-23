package com.cleannrooster.hexblade.Armors;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.armor.RunicArmor;
import com.extraspellattributes.ReabsorptionInit;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Armors {
    private static final Supplier<Ingredient> WOOL_INGREDIENTS = () -> { return Ingredient.ofItems(
            Items.WHITE_WOOL,
            Items.ORANGE_WOOL,
            Items.MAGENTA_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL,
            Items.LIME_WOOL,
            Items.PINK_WOOL,
            Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.CYAN_WOOL,
            Items.PURPLE_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.GREEN_WOOL,
            Items.RED_WOOL,
            Items.BLACK_WOOL);
    };

    public static RegistryEntry<ArmorMaterial> material(String name,
                                                        int protectionHead, int protectionChest, int protectionLegs, int protectionFeet,
                                                        int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient) {
        var material = new ArmorMaterial(
                Map.of(
                        ArmorItem.Type.HELMET, protectionHead,
                        ArmorItem.Type.CHESTPLATE, protectionChest,
                        ArmorItem.Type.LEGGINGS, protectionLegs,
                        ArmorItem.Type.BOOTS, protectionFeet),
                enchantability, equipSound, repairIngredient,
                List.of(new ArmorMaterial.Layer(Identifier.of(SpellbladesAndSuch.MOD_ID, name))),
                0,0
        );
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(SpellbladesAndSuch.MOD_ID, name), material);
    }

    public static RegistryEntry<ArmorMaterial> heraldFire = material(
            "ashherald",
            3, 8, 6, 3,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, () -> Ingredient.ofItems(SpellbladesAndSuch.RUNEBLAZE, Hexblade.RUNEBLAZEPLATE));

    public static RegistryEntry<ArmorMaterial> heraldArcane = material(
            "arcaneherald",
            3, 8, 6, 3,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,  () -> Ingredient.ofItems(SpellbladesAndSuch.RUNEGLEAM,Hexblade.RUNEGLEAMPLATE));

    public static RegistryEntry<ArmorMaterial> heraldFrost = material(
            "frostherald",
            3, 8, 6, 3,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,  () -> Ingredient.ofItems(SpellbladesAndSuch.RUNEFROST,Hexblade.RUNEFROSTPLATE));
    public static RegistryEntry<ArmorMaterial> magus = material(
            "magus",
            3, 8, 6, 3,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,  () -> Ingredient.ofItems(Hexblade.THREAD));

    public static RegistryEntry<ArmorMaterial> magisterFire = material(
            "blazing",
            2, 6, 4, 2,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, () -> Ingredient.ofItems(SpellbladesAndSuch.RUNEBLAZE));

    public static RegistryEntry<ArmorMaterial> magisterArcane = material(
            "gleaming",
            2, 6, 4, 2,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,  () -> Ingredient.ofItems(SpellbladesAndSuch.RUNEGLEAM));

    public static RegistryEntry<ArmorMaterial> magisterFrost = material(
            "frozen",
            2, 6, 4, 2,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,  () -> Ingredient.ofItems(SpellbladesAndSuch.RUNEFROST));



    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(RegistryEntry<ArmorMaterial> material, Identifier id, int durability, Armor.Set.ItemFactory factory, ItemConfig.ArmorSet defaults) {
        var entry = Armor.Entry.create(
                material,
                id,
                durability,
                factory,
                defaults);
        entries.add(entry);
        return entry;
    }


    private static final float specializedRobeSpellPower = 0.25F;
    private static final float specializedRobeCritDamage = 0.1F;
    private static final float specializedRobeCritChance = 0.02F;
    private static final float specializedRobeHaste = 0.03F;
    public static final Armor.Set Magus = create(
            magus,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "magus"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new MagusArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.ARCANE),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),

                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 5)
                            )),
                    new ItemConfig.ArmorSet.Piece(8)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 5)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 5)
                            )),
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 5)
                            ))
            ))
            .armorSet();

    public static final Armor.Set arcaneHerald = create(
            heraldArcane,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "arcaneherald"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new HeraldArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.ARCANE),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoarcane"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(8)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoarcane"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoarcane"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoarcane"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            ))
            ))
            .armorSet();

    public static final Armor.Set frostHerald = create(
            heraldFrost,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "frostherald"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new HeraldArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.FROST),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofrost"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(8)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofrost"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofrost"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofrost"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),

                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            ))
            ))
            .armorSet();

    public static final Armor.Set fireHerald = create(
            heraldFire,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "ashherald"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new HeraldArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.FIRE),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofire"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),

                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(8)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofire"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),

                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 1),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofire"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),

                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            )),
                    new ItemConfig.ArmorSet.Piece(3)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttofire"), 0.125F),
                                    ItemConfig.Attribute.multiply(Identifier.of(ReabsorptionInit.MOD_ID,"converttoheal"), 0.125F),

                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 2)
                            ))
            ))
            .armorSet();

    public static final Armor.Set gleaming = create(
            magisterArcane,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "gleaming"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new MagisterArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.ARCANE),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(2)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_DAMAGE.id, 0.4F),
                                    ItemConfig.Attribute.multiply(SpellPowerMechanics.HASTE.id, 0.12F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(4)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(2)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            ))
            ))
            .armorSet();

    public static final Armor.Set blazing = create(
            magisterFire,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "blazing"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new MagisterArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.FIRE),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(2)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_CHANCE.id, 0.08F),
                                    ItemConfig.Attribute.multiply(SpellPowerMechanics.HASTE.id, 0.12F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(4)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(2)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            ))
            ))
            .armorSet();

    public static final Armor.Set frozen = create(
            magisterFrost,
            Identifier.of(SpellbladesAndSuch.MOD_ID, "frozen"),
            20,
            (armorMaterialRegistryEntry,type,settings ) ->new MagisterArmor(armorMaterialRegistryEntry,type,settings, SpellSchools.FROST),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(2)
                            .addAll(List.of(
                                    ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_CHANCE.id, 0.08F),
                                    ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_DAMAGE.id, 0.4F),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(6)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(4)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            )),
                    new ItemConfig.ArmorSet.Piece(2)
                            .addAll(List.of(
                                    ItemConfig.Attribute.bonus(SpellSchools.FROST.id, 2),
                                    ItemConfig.Attribute.bonus(Identifier.of(ReabsorptionInit.MOD_ID,"reabsorption"), 3)
                            ))
            ))
            .armorSet();

    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries, SpellbladesAndSuch.KEY);
    }
}