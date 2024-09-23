package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.client.entity.MagisterModel;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.Spellblade;
import mod.azure.azurelib.common.api.client.renderer.DynamicGeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

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