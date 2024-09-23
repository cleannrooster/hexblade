package com.cleannrooster.hexblade.client.armor.model;

import com.cleannrooster.hexblade.Armors.MagisterArmor;
import com.cleannrooster.hexblade.Hexblade;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagisterArmorItemModel extends GeoModel<MagisterArmor> {
    @Override
    public Identifier getModelResource(MagisterArmor orb) {
        if(orb.getSlotType() == EquipmentSlot.HEAD){
            if(orb.getMagicSchool().equals(SpellSchools.ARCANE)){

                    return Identifier.of(Hexblade.MOD_ID,"geo/magebane.geo.json");


            }
            if(orb.getMagicSchool().equals(SpellSchools.FROST)){
                return Identifier.of(Hexblade.MOD_ID,"geo/mageseeker.geo.json");

            }
            return Identifier.of(Hexblade.MOD_ID,"geo/magebreaker.geo.json");

        }
        if(orb.getSlotType() == EquipmentSlot.CHEST){
            return Identifier.of(Hexblade.MOD_ID,"geo/inquisitor_chest.json");

        }
        if(orb.getSlotType() == EquipmentSlot.FEET){
            return Identifier.of(Hexblade.MOD_ID,"geo/inquisitor_feet.json");

        }
            return Identifier.of(Hexblade.MOD_ID,"geo/inquisitor_legs.json");


    }

    @Override
    public Identifier getTextureResource(MagisterArmor orb) {
        if(orb.getSlotType() == EquipmentSlot.HEAD){
            if(orb.getMagicSchool().equals(SpellSchools.ARCANE)){
                return Identifier.of(Hexblade.MOD_ID,"textures/armor/magebane_crown.png");


            }
            if(orb.getMagicSchool().equals(SpellSchools.FROST)){
                return Identifier.of(Hexblade.MOD_ID,"textures/armor/mageseeker_hat.png");

            }
            return Identifier.of(Hexblade.MOD_ID,"textures/armor/magebreaker_helmet.png");

        }
        if(orb.getMagicSchool().equals(SpellSchools.ARCANE)){

            return Identifier.of(Hexblade.MOD_ID,"textures/armor/aetherfire.png");
        }
        if(orb.getMagicSchool().equals(SpellSchools.FIRE)){
            return Identifier.of(Hexblade.MOD_ID,"textures/armor/rimeblaze.png");

        }
            return Identifier.of(Hexblade.MOD_ID,"textures/armor/deathchill.png");

    }


    @Override
    public Identifier getAnimationResource(MagisterArmor orb) {
        return null;
    }

}
