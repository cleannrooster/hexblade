package com.cleannrooster.hexblade.client.armor.renderer;

import com.cleannrooster.hexblade.Armors.HeraldArmor;
import com.cleannrooster.hexblade.client.armor.model.HeraldArmorModel;
import mod.azure.azurelib.common.api.client.renderer.GeoArmorRenderer;
public class HeraldArmorRenderer  extends GeoArmorRenderer<HeraldArmor> {

    public HeraldArmorRenderer() {
        super(new HeraldArmorModel());

    }
}
