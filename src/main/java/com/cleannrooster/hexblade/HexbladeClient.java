package com.cleannrooster.hexblade;

import com.cleannrooster.hexblade.client.entity.HexbladePortalRenderer;
import com.cleannrooster.hexblade.client.entity.MagisterRenderer;
import com.cleannrooster.hexblade.client.entity.MagusEffectRenderer;
import com.cleannrooster.hexblade.client.entity.MagusRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.cleannrooster.hexblade.Hexblade.HEXBLADEPORTAL;
import static com.cleannrooster.hexblade.Hexblade.PRISMATIC;
@Environment(EnvType.CLIENT)
public class HexbladeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
        ));
        EntityRendererRegistry.register(Hexblade.REAVER, MagisterRenderer::new);
        EntityRendererRegistry.register(HEXBLADEPORTAL, HexbladePortalRenderer::new);
        EntityRendererRegistry.register(Hexblade.ARCHMAGUS, MagusRenderer::new);


    }
}
