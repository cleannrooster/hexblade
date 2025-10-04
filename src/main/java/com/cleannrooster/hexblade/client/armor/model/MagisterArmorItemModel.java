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

        if(orb.getMagicSchool().equals(SpellSchools.ARCANE)){

            return Identifier.of(Hexblade.MOD_ID,"textures/armor/aetherfire.png");
        }
        if(orb.getMagicSchool().equals(SpellSchools.FIRE)){
            return Identifier.of(Hexblade.MOD_ID,"textures/armor/sunfire.png");

        }
            return Identifier.of(Hexblade.MOD_ID,"textures/armor/soulfrost.png");

    }


    @Override
    public Identifier getAnimationResource(MagisterArmor orb) {
        return null;
    }

}
