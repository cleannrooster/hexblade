package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.HexbladePortal;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class PortalModel<T extends HexbladePortal> extends GeoModel<HexbladePortal> {

    @Override
    public Identifier getModelResource(HexbladePortal reaver) {

        return Identifier.of(Hexblade.MOD_ID,"geo/portal.geo.json");
    }
    @Override
    public Identifier getTextureResource(HexbladePortal reaver) {
        return Identifier.of(Hexblade.MOD_ID,"textures/mob/ender_eye.png");
    }

    @Override
    public Identifier getAnimationResource(HexbladePortal reaver) {
        return Identifier.of(Hexblade.MOD_ID,"animations/portal.animation.json");
    }

}
