package com.cleannrooster.hexblade.client.armor.model;

import com.cleannrooster.hexblade.Armors.MagisterArmor;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagisterArmorModel extends GeoModel<MagisterArmor> {


    @Override
    public Identifier getModelResource(MagisterArmor animatable) {
        if(animatable.getSlotType() == EquipmentSlot.HEAD){
            if(animatable.getMagicSchool().equals(SpellSchools.ARCANE)){
                return Identifier.of(SpellbladesAndSuch.MOD_ID,"geo/magebane.geo.json");



            }
            if(animatable.getMagicSchool().equals(SpellSchools.FIRE)){
                return Identifier.of(SpellbladesAndSuch.MOD_ID,"geo/magebreaker.geo.json");


            }
            if(animatable.getMagicSchool().equals(SpellSchools.FROST)){
                return Identifier.of(SpellbladesAndSuch.MOD_ID,"geo/mageseeker.geo.json");

            }

        }
        return Identifier.of(SpellbladesAndSuch.MOD_ID,"geo/inquisitor.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagisterArmor animatable) {
        if(animatable.getSlotType() == EquipmentSlot.HEAD){
            if(animatable.getMagicSchool().equals(SpellSchools.ARCANE)){

                return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/magebane_crown.png");

            }
            if(animatable.getMagicSchool().equals(SpellSchools.FROST)){

                return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/mageseeker_hat.png");

            }
            if(animatable.getMagicSchool().equals(SpellSchools.FIRE)){

                return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/magebreaker_helmet.png");


            }

        }
        if(animatable.getMagicSchool().equals(SpellSchools.ARCANE)){

            return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/aetherfire.png");
        }
        if(animatable.getMagicSchool().equals(SpellSchools.FIRE)){

            return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/rimeblaze.png");

        }
        return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/deathchill.png");
    }

    @Override
    public Identifier getAnimationResource(MagisterArmor animatable) {
        return null;
    }
}
