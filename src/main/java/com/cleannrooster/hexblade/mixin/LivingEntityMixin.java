package com.cleannrooster.hexblade.mixin;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.invasions.attackevent;
import com.cleannrooster.hexblade.item.Starforge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_power.api.SpellPower;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

import static com.cleannrooster.hexblade.Hexblade.*;
import static com.cleannrooster.hexblade.effect.Effects.HEXED;
import static com.cleannrooster.spellblades.SpellbladesAndSuch.MOD_ID;
import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "canWalkOnFluid", cancellable = true)
    private void walkOnFluidReturn(FluidState fluidState, CallbackInfoReturnable<Boolean> info) {
        if(fluidState.getFluid()== Fluids.WATER){
            LivingEntity entity = (LivingEntity) (Object) this;
            if(entity.getWorld().getRegistryKey().equals(Hexblade.DIMENSIONKEY) ) {
                info.setReturnValue(true);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "clearStatusEffects", cancellable = true)
    public void clearStatusEffectsHex(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        LivingEntity living = (LivingEntity) (Object) this;
        if(living.hasStatusEffect(HEXED.registryEntry) && living instanceof ServerPlayerEntity player){
            attackeventArrayList.add(new attackevent(player.getWorld(),player));
        }
    }

        @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void hurtreal(final DamageSource player, float f, final CallbackInfoReturnable<Boolean> info) {
        if(player.getAttacker() instanceof ServerPlayerEntity realPlayer && player.getTypeRegistryEntry().isIn(TagKey.of(RegistryKeys.DAMAGE_TYPE,Identifier.of("c:is_magic")))){
            int hex = (int) Math.ceil(f);
            realPlayer.getStatHandler().setStat(realPlayer,Stats.CUSTOM.getOrCreateStat(HEXRAID),(int)(realPlayer.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(HEXRAID))+hex));
        }
    }
    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void hexLivingAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(OMNI);

    }
}
