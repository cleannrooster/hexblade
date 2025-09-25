package com.cleannrooster.hexblade.mixin;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.entity.SpellProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

@Mixin(SpellProjectile.class)
public class SpellProjectileMixin {
    @Inject(at = @At("TAIL"), method = "tick", cancellable = true)
    public  void tickSpellblade(CallbackInfo callbackInfo) {
        SpellProjectile entity = (SpellProjectile) (Object) this;
        if(entity.getSpellEntry() != null && entity.getSpellEntry().equals(SpellRegistry.from(entity.getWorld()).getEntry(Identifier.of(Hexblade.MOD_ID,"amethyst_slash2")).get())){
            {
                double x = (entity.age/16F)*cos(entity.age/5F);
                double z = (entity.age/16F)*sin(entity.age/5F);
                if(entity.getOwner()!= null) {
                    entity.setPos(entity.getOwner().getX() + x, entity.getOwner().getBoundingBox().getCenter().getY(), entity.getOwner().getZ() + z);
                    entity.setVelocity(entity.getX()-entity.prevX,entity.getY()-entity.prevY,entity.getZ()-entity.prevZ);
                    ProjectileUtil.setRotationFromVelocity(entity,999);
                }

            }

        }
        if(entity.getSpellEntry() != null && (
                entity.getSpellEntry().equals(SpellRegistry.from(entity.getWorld()).getEntry(Identifier.of(Hexblade.MOD_ID,"flame_slash")).get())||
                entity.getSpellEntry().equals(SpellRegistry.from(entity.getWorld()).getEntry(Identifier.of(Hexblade.MOD_ID,"frost_slash")).get()) ||
                entity.getSpellEntry().equals(SpellRegistry.from(entity.getWorld()).getEntry(Identifier.of(Hexblade.MOD_ID,"amethyst_slash")).get()))){
            if(entity.age > 10){
                entity.setFollowedTarget(null);
            }
        }
    }

}