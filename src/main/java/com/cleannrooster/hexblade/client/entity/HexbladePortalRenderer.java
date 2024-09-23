package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.entity.HexbladePortal;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class HexbladePortalRenderer extends GeoEntityRenderer<HexbladePortal> {

    public HexbladePortalRenderer(EntityRendererFactory.Context context) {
        super(context, new PortalModel());
    }
}