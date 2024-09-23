package com.cleannrooster.hexblade.client.armor.model;

import com.cleannrooster.hexblade.Armors.MagusArmor;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class MagusArmorModel extends GeoModel<MagusArmor> {


    @Override
    public Identifier getModelResource(MagusArmor orb) {

        return Identifier.of(SpellbladesAndSuch.MOD_ID,"geo/robes.geo.json");

    }

    @Override
    public Identifier getTextureResource(MagusArmor orb) {
        return Identifier.of(SpellbladesAndSuch.MOD_ID, "textures/armor/robestexture_default.png");
    }

    @Override
    public Identifier getAnimationResource(MagusArmor orb) {
        return null;
    }

}
