package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.Spellblade;
import mod.azure.azurelib.common.api.client.renderer.DynamicGeoEntityRenderer;
import mod.azure.azurelib.common.internal.client.util.RenderUtils;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

import java.time.LocalDate;
import java.time.Month;

public class MagusRenderer<T extends Magus, M extends BipedEntityModel<T>> extends DynamicGeoEntityRenderer<Magus> {

    private static final Identifier DEFAULT_LOCATION = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FIRE = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FROST = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus_frost.png");
    private static final Identifier ARCANE = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus_arcane.png");

    private static final Identifier STEVE = Identifier.of(Hexblade.MOD_ID,"textures/mob/stevetexture.png");

    public MagusRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MagusModel<>());
        addRenderLayer(new RenderLayerItemMagus(this));

        //this.layerRenderers.add((GeoLayerRenderer<Reaver>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,renderManager.getItemInHandRenderer()));
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, Magus animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(Magus entity, float entityYaw, float partialTick, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int packedLight) {
        super.render(entity,entityYaw,partialTick,matrices,vertexConsumerProvider,packedLight);
        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(getTextureMagus(entity));
        Sprite sprite2 = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(getTextureMagus(entity));
        matrices.push();
        float f = entity.getWidth() * 1.4F;
        matrices.scale(f, f, f);
        float g = 2F;
        float h = 0.0F;
        float i = entity.getHeight() / f;
        float j = 0.0F;
        matrices.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation());
        matrices.translate(0.0F, 0.0F, -0.1F);

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
    public static void drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v, int light) {
        vertices.vertex(entry, x, y, z).color(-1).texture(u, v).overlay(0, 10).light(light).normal(entry, 0.0F, 1.0F, 0.0F);
    }
    public static void drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(entry, x, y, z).color(1,1,1,0.65f).texture(u, v).overlay(0, 10).light(240).normal(entry, 0.0F, 1.0F, 0.0F);
    }
    public Identifier getTextureMagus(LivingEntity livingEntity){
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
    public static final Identifier textureIDArcane = Identifier.of(Hexblade.MOD_ID, "item/arcaneaura");


    public static final Identifier textureIDFrost = Identifier.of(Hexblade.MOD_ID, "item/frostaura");
    public static final Identifier textureIDFire = Identifier.of(Hexblade.MOD_ID, "item/flameaura");

    public Identifier getTextureLocation(Magus p_114891_) {
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


}