package com.cleannrooster.hexblade.client.armor.renderer;

import com.cleannrooster.hexblade.Armors.MagisterArmor;
import com.cleannrooster.hexblade.client.armor.model.MagisterArmorModel;
import mod.azure.azurelib.common.api.client.renderer.GeoArmorRenderer;
public class MagisterArmorRenderer  extends GeoArmorRenderer<MagisterArmor> {

    public MagisterArmorRenderer() {
        super(new MagisterArmorModel());

    }
}
