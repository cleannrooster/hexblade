package com.cleannrooster.hexblade.client.armor.renderer;

import com.cleannrooster.hexblade.Armors.MagisterArmor;
import com.cleannrooster.hexblade.client.armor.model.MagisterArmorItemModel;
import mod.azure.azurelib.common.api.client.renderer.GeoItemRenderer;

public class MagisterArmorItemRenderer extends GeoItemRenderer<MagisterArmor> {

    public MagisterArmorItemRenderer() {
        super(new MagisterArmorItemModel());

    }
}
