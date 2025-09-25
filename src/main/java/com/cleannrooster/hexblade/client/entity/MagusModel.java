package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.items.Spellblade;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

import java.time.LocalDate;
import java.time.Month;

public class MagusModel<T extends MagusModel> extends GeoModel<Magus> implements ModelWithArms {
    private static final Identifier DEFAULT_LOCATION = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FIRE = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FROST = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus_frost.png");
    private static final Identifier ARCANE = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus_arcane.png");

    private static final Identifier STEVE = Identifier.of(Hexblade.MOD_ID,"textures/mob/stevetexture.png");

    @Override
    public Identifier getModelResource(Magus reaver) {
        if((reaver.getDataTracker().get(Magus.APRILFOOLS) || Hexblade.config.aprilFools) || (LocalDate.now().getDayOfMonth() == 1 && LocalDate.now().getMonth() == Month.APRIL)){
            return Identifier.of(Hexblade.MOD_ID,"geo/steve.json");
        }
        return Identifier.of(Hexblade.MOD_ID,"geo/archmagus.geo.json");
    }
    public Identifier getTextureResource(Magus p_114891_) {
        if((p_114891_.getDataTracker().get(Magus.APRILFOOLS) || Hexblade.config.aprilFools) || (LocalDate.now().getDayOfMonth() == 1 && LocalDate.now().getMonth() == Month.APRIL)){
            return STEVE;
        }
            if(p_114891_.getMagicSchool().equals(SpellSchools.FIRE)){
                return FIRE;
            }
            if(p_114891_.getMagicSchool().equals(SpellSchools.FROST)){
                return FROST;
            }
            if(p_114891_.getMagicSchool().equals(SpellSchools.ARCANE)){
                return ARCANE;
            }

        return DEFAULT_LOCATION;
    }

    @Override
    public Identifier getAnimationResource(Magus reaver) {
        return Identifier.of(Hexblade.MOD_ID,"animations/magus.animation.json");
    }
    public void setArmAngle(Arm humanoidArm, MatrixStack poseStack) {
        this.translateAndRotate(poseStack);
    }
    public void translateAndRotate(MatrixStack arg) {
        arg.translate((double)(1), (double)(0 / 16.0F), (double)(0 / 16.0F));
        arg.scale(2, 2, 2);



    }
}
