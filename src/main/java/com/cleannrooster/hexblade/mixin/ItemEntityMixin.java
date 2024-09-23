package com.cleannrooster.hexblade.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.cleannrooster.hexblade.Hexblade.DIMENSIONKEY;
@Mixin(ItemEntity.class)
abstract  class ItemEntityMixin {
    @Inject(at = @At("HEAD"), method = "applyWaterBuoyancy", cancellable = true)
    private void setBounding( CallbackInfo info) {
        ItemEntity itenEntity = (ItemEntity) (Object) this;
        if (itenEntity.getWorld().getRegistryKey().equals(DIMENSIONKEY)) {

            Vec3d vec3d = itenEntity.getVelocity();
            itenEntity.setVelocity(vec3d.x * 0.9900000095367432D, vec3d.y + 0.01, vec3d.z * 0.9900000095367432D);

        }

    }

}
