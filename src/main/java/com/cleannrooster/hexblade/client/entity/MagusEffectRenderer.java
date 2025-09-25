package com.cleannrooster.hexblade.client.entity;


import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.client.compatibility.ShaderCompatibility;
import net.spell_power.api.SpellSchools;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import static net.minecraft.client.render.RenderPhase.*;
import static net.minecraft.client.render.item.ItemRenderer.getDirectItemGlintConsumer;

public class MagusEffectRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier textureIDArcane = Identifier.of(Hexblade.MOD_ID, "item/arcaneaura");


    public static final Identifier textureIDFrost = Identifier.of(Hexblade.MOD_ID, "item/frostaura");
    public static final Identifier textureIDFire = Identifier.of(Hexblade.MOD_ID, "item/flameaura");
    public MagusEffectRenderer() {
    }
RenderLayer layer =      CustomLayers.create(
        SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
        BEACON_BEAM_PROGRAM,
        TRANSLUCENT_TRANSPARENCY,
        DISABLE_CULLING,
        COLOR_MASK,
        ENABLE_OVERLAY_COLOR,
        MAIN_TARGET,
        true);


    @Override
    public void renderEffect(int i, LivingEntity entity, float v, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(getTexture(entity));
        Sprite sprite2 = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(getTexture(entity));
        matrices.push();
        float f = entity.getWidth() * 1.4F;
        matrices.scale(f, f, f);
        float g = 0.5F;
        float h = 0.0F;
        float j = 0.0F;
        matrices.translate(0.0F, 0.0F, 0.3F - (float)((int)i) * 0.02F);
        float k = 0.0F;
        int l = 0;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntityCutout());

        for(MatrixStack.Entry entry = matrices.peek(); i > 0.0F; ++l) {
            Sprite sprite3 = l % 2 == 0 ? sprite : sprite2;
            float m = sprite3.getMinU();
            float n = sprite3.getMinV();
            float o = sprite3.getMaxU();
            float p = sprite3.getMaxV();
            if (l / 2 % 2 == 0) {
                float q = o;
                o = m;
                m = q;
            }

            drawFireVertex(entry, vertexConsumer, -g - 0.0F, 0.0F - j, k, o, p);
            drawFireVertex(entry, vertexConsumer, g - 0.0F, 0.0F - j, k, m, p);
            drawFireVertex(entry, vertexConsumer, g - 0.0F, 1.4F - j, k, m, n);
            drawFireVertex(entry, vertexConsumer, -g - 0.0F, 1.4F - j, k, o, n);
            i -= 0.45F;
            j -= 0.45F;
            g *= 0.9F;
            k -= 0.03F;
        }

        matrices.pop();
    }
    private static void drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(entry, x, y, z).color(-1).texture(u, v).overlay(0, 10).light(240).normal(entry, 0.0F, 1.0F, 0.0F);
    }
    public Identifier getTexture(LivingEntity livingEntity){
        if(livingEntity instanceof Magus magus){
            if (magus.getMagicSchool().equals(SpellSchools.ARCANE)) {

                return textureIDArcane;
            }
            if (magus.getMagicSchool().equals(SpellSchools.FROST)) {

                return textureIDFrost;
            }
            if (magus.getMagicSchool().equals(SpellSchools.FIRE)) {

                return textureIDFire;
            }
        }
        return textureIDArcane;
                }
}