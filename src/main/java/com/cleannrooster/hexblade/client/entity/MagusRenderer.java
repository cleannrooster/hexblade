package com.cleannrooster.hexblade.client.entity;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.items.Spellblade;
import mod.azure.azurelib.common.api.client.renderer.DynamicGeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagusRenderer<T extends Magus, M extends BipedEntityModel<T>> extends DynamicGeoEntityRenderer<Magus> {

    private static final Identifier DEFAULT_LOCATION = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FIRE = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FROST = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus_frost.png");
    private static final Identifier ARCANE = Identifier.of(Hexblade.MOD_ID,"textures/mob/magus_arcane.png");


    public MagusRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MagusModel<>());
        addRenderLayer(new RenderLayerItemMagus(this));

        //this.layerRenderers.add((GeoLayerRenderer<Reaver>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,renderManager.getItemInHandRenderer()));
    }



    public Identifier getTextureLocation(Magus p_114891_) {
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