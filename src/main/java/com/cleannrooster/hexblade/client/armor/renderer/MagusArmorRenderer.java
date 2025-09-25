package com.cleannrooster.hexblade.client.armor.renderer;

import com.cleannrooster.hexblade.Armors.MagusArmor;
import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.client.armor.model.MagusArmorModel;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import mod.azure.azurelib.common.api.client.renderer.GeoArmorRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagusArmorRenderer extends GeoArmorRenderer<MagusArmor> {

    public MagusArmorRenderer() {
        super(new MagusArmorModel());

    }
    public static final Identifier textureIDArcane = Identifier.of(Hexblade.MOD_ID, "item/arcaneaura");


    public static final Identifier textureIDFrost = Identifier.of(Hexblade.MOD_ID, "item/frostaura");
    public static final Identifier textureIDFire = Identifier.of(Hexblade.MOD_ID, "item/flameaura");

    @Override
    public void preRender(MatrixStack matrices, MagusArmor animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSources, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        super.preRender(matrices, animatable, model, bufferSources, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public void renderRecursively(MatrixStack matrices, MagusArmor animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSources, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        super.renderRecursively(matrices, animatable, bone, renderType, bufferSources, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void actuallyRender(MatrixStack matrices, MagusArmor animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSources, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {


        super.actuallyRender(matrices, animatable, model, renderType, bufferSources, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public void postRender(MatrixStack matrices, MagusArmor animatable, BakedGeoModel model, VertexConsumerProvider bufferSources, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        super.postRender(matrices, animatable, model, bufferSources, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public void renderFinal(MatrixStack matrices, MagusArmor animatable, BakedGeoModel model, VertexConsumerProvider bufferSources, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {

        super.renderFinal(matrices, animatable, model, bufferSources, buffer, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(@NotNull MatrixStack matrices, VertexConsumer buffer, int packedLight, int packedOverlay, int var5) {


        super.render(matrices,buffer,packedLight,packedOverlay,var5);


    }
    private static void drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(entry, x, y, z).color(-1).texture(u, v).overlay(0, 10).light(240).normal(entry, 0.0F, 1.0F, 0.0F);
    }
    public static Identifier getFlameTexture(Entity animatable) {
        if(animatable instanceof PlayerEntity player) {
            double arcane = SpellPower.getSpellPower(SpellSchools.ARCANE,player).baseValue();
            double fire = SpellPower.getSpellPower(SpellSchools.FIRE,player).baseValue();
            double frost = SpellPower.getSpellPower(SpellSchools.FROST,player).baseValue();
            if(arcane > fire && arcane > frost){
                return textureIDArcane;
            }
            if(fire > arcane && fire > frost){
                return textureIDFire;
            }
            if(frost > arcane && frost > fire){
                return textureIDFrost;
            }
            if(arcane == fire && fire == frost){
                return null;

            }
        }

        return null;
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
