package com.cleannrooster.hexblade.client.armor.renderer;

import com.cleannrooster.hexblade.Armors.MagusArmor;
import com.cleannrooster.hexblade.client.armor.model.MagusArmorModel;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import mod.azure.azurelib.common.api.client.renderer.GeoArmorRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

public class MagusArmorRenderer extends GeoArmorRenderer<MagusArmor> {

    public MagusArmorRenderer() {
        super(new MagusArmorModel());

    }

    @Override
    public Identifier getTextureLocation(MagusArmor animatable) {
        if(this.getCurrentEntity() instanceof PlayerEntity player) {
            double arcane = SpellPower.getSpellPower(SpellSchools.ARCANE,player).baseValue();
            double fire = SpellPower.getSpellPower(SpellSchools.FIRE,player).baseValue();
            double frost = SpellPower.getSpellPower(SpellSchools.FROST,player).baseValue();
            if(arcane > fire && arcane > frost){
                return Identifier.of(SpellbladesAndSuch.MOD_ID, "textures/armor/robestexture_arcane.png");
            }
            if(fire > arcane && fire > frost){
                return Identifier.of(SpellbladesAndSuch.MOD_ID, "textures/armor/robestexture_fire.png");
            }
            if(frost > arcane && frost > fire){
                return Identifier.of(SpellbladesAndSuch.MOD_ID, "textures/armor/robestexture_frost.png");
            }
            if(arcane == fire && fire == frost){
                return Identifier.of(SpellbladesAndSuch.MOD_ID, "textures/armor/robestexture_default.png");

            }
        }

        return Identifier.of(SpellbladesAndSuch.MOD_ID, "textures/armor/robestexture_default.png");
    }
}
