package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.spellblades.items.Spellblade;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellSchools;

public class MagisterModel<T extends Magister> extends GeoModel<Magister> implements ModelWithArms {
    private static final Identifier DEFAULT_LOCATION = Identifier.of(Hexblade.MOD_ID,"textures/mob/arcanehexblade.png");
    private static final Identifier FIRE = Identifier.of(Hexblade.MOD_ID,"textures/mob/firehexblade.png");
    private static final Identifier FROST = Identifier.of(Hexblade.MOD_ID,"textures/mob/frosthexblade.png");
    private static final Identifier ARCANE = Identifier.of(Hexblade.MOD_ID,"textures/mob/arcanehexblade.png");

    @Override
    public Identifier getModelResource(Magister reaver) {

        if(reaver.getMainHandStack().getItem() instanceof Spellblade spellblade){
            if(spellblade.getSchool().equals(SpellSchools.FIRE)){
                return Identifier.of(Hexblade.MOD_ID,"geo/firehexblade.json");
            }
            if(spellblade.getSchool().equals(SpellSchools.FROST)){
                return Identifier.of(Hexblade.MOD_ID,"geo/frosthexblade.geo.json");
            }
            if(spellblade.getSchool().equals(SpellSchools.ARCANE)){
                return Identifier.of(Hexblade.MOD_ID,"geo/arcanehexblade.geo.json");
            }
        }
        return Identifier.of(Hexblade.MOD_ID,"geo/arcanehexblade.geo.json");
    }
    @Override
    public Identifier getTextureResource(Magister reaver) {
        if(reaver.getMainHandStack().getItem() instanceof Spellblade spellblade){
            if(spellblade.getSchool().equals(SpellSchools.FIRE)){
                return FIRE;
            }
            if(spellblade.getSchool().equals(SpellSchools.FROST)){
                return FROST;
            }
            if(spellblade.getSchool().equals(SpellSchools.ARCANE)){
                return ARCANE;
            }
        }
        return DEFAULT_LOCATION;
    }

    @Override
    public Identifier getAnimationResource(Magister reaver) {
        return Identifier.of(Hexblade.MOD_ID,"animations/mobs.animations.json");
    }
    public void setArmAngle(Arm humanoidArm, MatrixStack poseStack) {
        this.translateAndRotate(poseStack);
    }
    public void translateAndRotate(MatrixStack arg) {
        arg.translate((double)(1), (double)(0 / 16.0F), (double)(0 / 16.0F));
        arg.scale(2, 2, 2);



    }
}
