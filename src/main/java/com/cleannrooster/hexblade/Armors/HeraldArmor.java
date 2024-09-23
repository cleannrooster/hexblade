package com.cleannrooster.hexblade.Armors;

import com.cleannrooster.hexblade.client.armor.renderer.HeraldArmorRenderer;
import mod.azure.azurelib.common.api.common.animatable.GeoItem;
import mod.azure.azurelib.common.internal.client.RenderProvider;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.SpellSchool;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class HeraldArmor extends Armor.CustomItem implements GeoItem {
    public SpellSchool school;
    public HeraldArmor(RegistryEntry<ArmorMaterial> material, ArmorItem.Type type, Item.Settings settings,SpellSchool school) {
        super(material, type, settings);
        this.school= school;
    }

    public SpellSchool getMagicSchool() {
        return school;
    }

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);




    @Override
    public void createRenderer(Consumer<RenderProvider> consumer) {
        consumer.accept(new RenderProvider() {
            private HeraldArmorRenderer renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new HeraldArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
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
