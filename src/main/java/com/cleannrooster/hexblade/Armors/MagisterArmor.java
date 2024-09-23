package com.cleannrooster.hexblade.Armors;

import com.cleannrooster.hexblade.client.armor.renderer.MagisterArmorItemRenderer;
import com.cleannrooster.hexblade.client.armor.renderer.MagisterArmorRenderer;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mod.azure.azurelib.common.api.common.animatable.GeoItem;
import mod.azure.azurelib.common.internal.client.RenderProvider;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.SpellSchool;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.extraspellattributes.ReabsorptionInit.WARDING;

public class MagisterArmor extends Armor.CustomItem implements GeoItem {
    public SpellSchool school;
    public MagisterArmor(RegistryEntry<ArmorMaterial> material, ArmorItem.Type type, Item.Settings settings,SpellSchool school) {
        super(material, type, settings);
        this.school = school;
    }

    public SpellSchool getMagicSchool() {
        return school;
    }

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);



    @Override
    public void createRenderer(Consumer<RenderProvider> consumer) {
        consumer.accept(new RenderProvider() {
            private MagisterArmorRenderer renderer;
            private MagisterArmorItemRenderer renderer1;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new MagisterArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (renderer1 == null) return new MagisterArmorItemRenderer();
                return this.renderer1;
            }

        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
