package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.client.entity.MagisterModel;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.Spellblade;
import mod.azure.azurelib.common.api.client.renderer.DynamicGeoEntityRenderer;
import mod.azure.azurelib.common.internal.client.util.RenderUtils;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.spell_power.api.SpellSchools;
import org.joml.Matrix4f;

import java.util.List;

public class MagisterRenderer<T extends Magister, M extends BipedEntityModel<T>> extends DynamicGeoEntityRenderer<Magister> {

    private static final Identifier DEFAULT_LOCATION = Identifier.of(Hexblade.MOD_ID,"textures/mob/arcanehexblade.png");
    private static final Identifier FIRE = Identifier.of(Hexblade.MOD_ID,"textures/mob/firehexblade.png");
    private static final Identifier FROST = Identifier.of(Hexblade.MOD_ID,"textures/mob/frosthexblade.png");
    private static final Identifier ARCANE = Identifier.of(Hexblade.MOD_ID,"textures/mob/arcanehexblade.png");


    public MagisterRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MagisterModel<>());
        addRenderLayer(new RenderLayerItemMagister(this));
        //this.layerRenderers.add((GeoLayerRenderer<Reaver>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,renderManager.getItemInHandRenderer()));
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, Magister animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        var bool = ((bone.getName().equals("rightArm")));
        var bool2 = ((bone.getName().equals("leftArm")));

        if (bone.getName().equals("head") || bool || bool2) {
            poseStack.push();
            RenderUtils.translateMatrixToBone(poseStack, bone);
            RenderUtils.translateToPivotPoint(poseStack, bone);
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
            RenderUtils.scaleMatrixForBone(poseStack, bone);

            if (!bool) {
                poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.clamp(animatable.bodyYaw- animatable.getYaw(partialTick), -180, 180)));
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-animatable.getPitch(partialTick)));

            } else  {

                poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.clamp( -animatable.bodyYaw+animatable.getYaw(partialTick), -180, 180)));

            }

            if (bone.isTrackingMatrices()) {
                Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
                Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

                bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
                bone.setLocalSpaceMatrix(
                        RenderUtils.translateMatrix(localMatrix, getPositionOffset(this.animatable, 1).toVector3f())
                );
                bone.setWorldSpaceMatrix(
                        RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.animatable.getPos().toVector3f())
                );
            }

            RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

            this.textureOverride = getTextureOverrideForBone(bone, this.animatable, partialTick);
            Identifier texture = this.textureOverride == null
                    ? getTexture(this.animatable)
                    : this.textureOverride;
            RenderLayer renderTypeOverride = getRenderTypeOverrideForBone(
                    bone,
                    this.animatable,
                    texture,
                    bufferSource,
                    partialTick
            );

            if (texture != null && renderTypeOverride == null)
                renderTypeOverride = getRenderType(this.animatable, texture, bufferSource, partialTick);

            if (renderTypeOverride != null)
                buffer = bufferSource.getBuffer(renderTypeOverride);

            if (
                    !boneRenderOverride(
                            poseStack,
                            bone,
                            bufferSource,
                            buffer,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            colour
                    )
            )
                super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);

            if (renderTypeOverride != null)
                buffer = bufferSource.getBuffer(
                        getRenderType(this.animatable, getTexture(this.animatable), bufferSource, partialTick)
                );

            if (!isReRender)
                applyRenderLayersForBone(
                        poseStack,
                        animatable,
                        bone,
                        renderType,
                        bufferSource,
                        buffer,
                        partialTick,
                        packedLight,
                        packedOverlay
                );

            super.renderChildBones(
                    poseStack,
                    animatable,
                    bone,
                    renderType,
                    bufferSource,
                    buffer,
                    isReRender,
                    partialTick,
                    packedLight,
                    packedOverlay,
                    colour
            );

            poseStack.pop();
        }
        else {


            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        }


    }

    @Override
    protected void applyRotations(Magister animatable, MatrixStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        List<PlayerEntity> list =  animatable.getWorld().getTargets(PlayerEntity.class, TargetPredicate.DEFAULT,animatable,animatable.getBoundingBox().expand(12));
   /*     if(list.stream().anyMatch(livingEntity ->
                    livingEntity.getAttributeValue((SpellSchools.ARCANE).attributeEntry) > animatable.getMaxHealth()/2 ||
                    livingEntity.getAttributeValue((SpellSchools.FROST).attributeEntry) > animatable.getMaxHealth()/2 ||
                    livingEntity.getAttributeValue((SpellSchools.FIRE).attributeEntry) > animatable.getMaxHealth()/2 ||
                    livingEntity.getAttributeValue((SpellSchools.HEALING).attribute) > animatable.getMaxHealth()/2)

        ){
            rotationYaw += (float) (Math.cos((double) animatable.age * 3.25D) * Math.PI * (double) 0.4F);
        }
*/




        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }

    public Identifier getTextureLocation(Magister p_114891_) {
        if(p_114891_.getMainHandStack().getItem() instanceof Spellblade spellblade){
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


}