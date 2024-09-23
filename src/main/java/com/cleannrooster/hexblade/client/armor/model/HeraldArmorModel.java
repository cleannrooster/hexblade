package com.cleannrooster.hexblade.client.armor.model;

import com.cleannrooster.hexblade.Armors.HeraldArmor;
import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class HeraldArmorModel extends GeoModel<HeraldArmor> {


    @Override
    public Identifier getModelResource(HeraldArmor animatable) {
        if(animatable.getMagicSchool().equals(SpellSchools.ARCANE)) {

            return Identifier.of(Hexblade.MOD_ID, "geo/enigmaherald.geo.json");
        }
        if(animatable.getMagicSchool().equals(SpellSchools.FIRE)) {

            return Identifier.of(Hexblade.MOD_ID, "geo/ashenherald.geo.json");
        }
        if(animatable.getMagicSchool().equals(SpellSchools.FROST)) {

            return Identifier.of(Hexblade.MOD_ID, "geo/frigidherald.geo.json");
        }
        return Identifier.of(Hexblade.MOD_ID, "geo/enigmaherald.geo.json");

    }

    @Override
    public Identifier getTextureResource(HeraldArmor animatable) {

        if(animatable.getMagicSchool().equals(SpellSchools.ARCANE)){

            return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/enigmaherald.png");
        }
        if(animatable.getMagicSchool().equals(SpellSchools.FROST)){

            return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/frigidherald.png");
        }
        if(animatable.getMagicSchool().equals(SpellSchools.FIRE)){

            return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/ashenherald.png");
        }
    /*    if(animatable.getMagicschool().contains(MagicSchool.FIRE)){

            return Identifier.of(Spellblades.MOD_ID,"textures/armor/rimeblaze.png");

        }*/
        return Identifier.of(SpellbladesAndSuch.MOD_ID,"textures/armor/enigmaherald.png");
    }

    @Override
    public Identifier getAnimationResource(HeraldArmor animatable) {
        return null;
    }
}
