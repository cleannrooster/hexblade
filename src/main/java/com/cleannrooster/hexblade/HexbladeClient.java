package com.cleannrooster.hexblade;

import com.cleannrooster.hexblade.client.entity.HexbladePortalRenderer;
import com.cleannrooster.hexblade.client.entity.MagisterRenderer;
import com.cleannrooster.hexblade.client.entity.MagusRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.LightningEntityRenderer;

import static com.cleannrooster.hexblade.Hexblade.HEXBLADEPORTAL;

public class HexbladeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Hexblade.REAVER, MagisterRenderer::new);
        EntityRendererRegistry.register(HEXBLADEPORTAL, HexbladePortalRenderer::new);
        EntityRendererRegistry.register(Hexblade.ARCHMAGUS, MagusRenderer::new);
    }
}
