package com.cleannrooster.hexblade.client.armor.model;

import com.cleannrooster.hexblade.Armors.MagisterArmor;
import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagisterArmorModel extends GeoModel<MagisterArmor> {


    @Override
    public Identifier getModelResource(MagisterArmor animatable) {

        return Identifier.of(Hexblade.MOD_ID,"geo/inquisitor.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagisterArmor animatable) {

        if(animatable.getMagicSchool().equals(SpellSchools.ARCANE)){

            return Identifier.of(Hexblade.MOD_ID,"textures/armor/aetherfire.png");
        }
        if(animatable.getMagicSchool().equals(SpellSchools.FIRE)){

            return Identifier.of(Hexblade.MOD_ID,"textures/armor/sunfire.png");

        }
        return Identifier.of(Hexblade.MOD_ID,"textures/armor/soulfrost.png");
    }

    @Override
    public Identifier getAnimationResource(MagisterArmor animatable) {
        return null;
    }
}
