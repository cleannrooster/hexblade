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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.WorldScheduler;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

import static com.cleannrooster.hexblade.Hexblade.*;
import static com.cleannrooster.spellblades.SpellbladesAndSuch.MOD_ID;
import static com.extraspellattributes.ReabsorptionInit.*;
import static net.spell_engine.internals.SpellHelper.ammoForSpell;
import static net.spell_engine.internals.SpellHelper.impactTargetingMode;

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
        if(living.hasStatusEffect(HEXED) && living instanceof ServerPlayerEntity player){
            attackeventArrayList.add(new attackevent(player.getWorld(),player));
        }
    }

        @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void hurtreal(final DamageSource player, float f, final CallbackInfoReturnable<Boolean> info) {


        if (player.getAttacker() != null &&player.getAttacker() instanceof PlayerEntity player3 && player.getTypeRegistryEntry().getIdAsString().contains("spell_power")) {
            player3.increaseStat(HEXRAID, (int) Math.ceil(f));
        }
        if(player.getAttacker() instanceof PlayerEntity player1 && !player.getAttacker().getWorld().isClient() && player.getAttacker().getWeaponStack() != null && player.getAttacker().getWeaponStack().getItem() instanceof Starforge )
        {
            ItemStack stack = player.getAttacker().getWeaponStack();
            {
                Predicate<Entity> selectionPredicate = (target2) -> {
                    return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player1, target2)
                    );
                };
                Spell spell = SpellRegistry.getSpell(Identifier.of(MOD_ID, "arcaneoverdrive"));

                if (player1 instanceof SpellCasterEntity entity && player1 instanceof PlayerEntity player2 && ammoForSpell(player2, spell, stack).satisfied() && !entity.getCooldownManager().isCoolingDown(Identifier.of(MOD_ID, "arcaneoverdrive"))) {
                    entity.getCooldownManager().set(Identifier.of(MOD_ID, "arcaneoverdrive"), (int) (20 * SpellHelper.getCooldownDuration(player1, spell)));

                    int i = 0;
                    List<Entity> targets = player1.getWorld().getOtherEntities(player1, player1.getBoundingBox().expand(spell.range), selectionPredicate);


                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1.0F, 1.0F, (Vec3d) null, SpellPower.getSpellPower(spell.school, player1), impactTargetingMode(spell));

                    for (Entity target1 : targets) {
                        SpellInfo spell1 = new SpellInfo(SpellRegistry.getSpell(Identifier.of(MOD_ID, "arcaneoverdrive")), Identifier.of(MOD_ID, "arcaneoverdrive"));

                        SpellHelper.performImpacts(player1.getWorld(), player1, target1, player1, spell1, new SpellHelper.ImpactContext());
                    }
                    ParticleHelper.sendBatches(player1, spell.release.particles);
                    SpellHelper.AmmoResult ammoResult = ammoForSpell(player2, spell, stack);
                    if (ammoResult.ammo() != null) {
                        for (int ii = 0; ii < player2.getInventory().size(); ++ii) {
                            ItemStack stack1 = player2.getInventory().getStack(ii);
                            if (stack1.isOf(ammoResult.ammo().getItem())) {
                                stack1.decrement(1);
                                if (stack1.isEmpty()) {
                                    player2.getInventory().removeOne(stack1);
                                }
                                break;
                            }
                        }
                    }

                }
            }
            ((WorldScheduler) player1.getWorld()).schedule(5, () -> {
                Predicate<Entity> selectionPredicate = (target2) -> {
                    return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player1, target2)
                    );
                };
                Spell spell = SpellRegistry.getSpell(Identifier.of(MOD_ID, "fireoverdrive"));

                if (player1 instanceof PlayerEntity player2 && player1 instanceof SpellCasterEntity entity && ammoForSpell(player2, spell, stack).satisfied() && !entity.getCooldownManager().isCoolingDown(Identifier.of(MOD_ID, "fireoverdrive"))) {
                    entity.getCooldownManager().set(Identifier.of(MOD_ID, "fireoverdrive"), (int) (20 * SpellHelper.getCooldownDuration(player1, spell)));

                    int i = 0;
                    List<Entity> targets = player1.getWorld().getOtherEntities(player1, player1.getBoundingBox().expand(spell.range), selectionPredicate);

                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1.0F, 1.0F, (Vec3d) null, SpellPower.getSpellPower(spell.school, player1), impactTargetingMode(spell));

                    for (Entity target1 : targets) {
                        SpellInfo spell1 = new SpellInfo(SpellRegistry.getSpell(Identifier.of(MOD_ID, "fireoverdrive")), Identifier.of(MOD_ID, "fireoverdrive"));

                        SpellHelper.performImpacts(player1.getWorld(), player1, target1, player1, spell1, new SpellHelper.ImpactContext());
                    }
                    ParticleHelper.sendBatches(player1, spell.release.particles);
                    SpellHelper.AmmoResult ammoResult = ammoForSpell(player2, spell, stack);
                    if (ammoResult.ammo() != null) {
                        for (int ii = 0; ii < player2.getInventory().size(); ++ii) {
                            ItemStack stack1 = player2.getInventory().getStack(ii);
                            if (stack1.isOf(ammoResult.ammo().getItem())) {
                                stack1.decrement(1);
                                if (stack1.isEmpty()) {
                                    player2.getInventory().removeOne(stack1);
                                }
                                break;
                            }
                        }
                    }


                }
            });
            ((WorldScheduler) player1.getWorld()).schedule(10, () -> {

                Predicate<Entity> selectionPredicate = (target2) -> {
                    return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player1, target2)
                    );
                };
                Spell spell = SpellRegistry.getSpell(Identifier.of(MOD_ID, "frostoverdrive"));

                if (player1 instanceof PlayerEntity player2 && player1 instanceof SpellCasterEntity entity && ammoForSpell(player2, spell, stack).satisfied() && !entity.getCooldownManager().isCoolingDown(Identifier.of(MOD_ID, "frostoverdrive"))) {
                    entity.getCooldownManager().set(Identifier.of(MOD_ID, "frostoverdrive"), (int) (20 * SpellHelper.getCooldownDuration(player1, spell)));

                    int i = 0;
                    List<Entity> targets = player1.getWorld().getOtherEntities(player1, player1.getBoundingBox().expand(spell.range), selectionPredicate);

                    SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1.0F, 1.0F, (Vec3d) null, SpellPower.getSpellPower(spell.school, player1), impactTargetingMode(spell));

                    for (Entity target1 : targets) {
                        SpellInfo spell1 = new SpellInfo(SpellRegistry.getSpell(Identifier.of(MOD_ID, "frostoverdrive")), Identifier.of(MOD_ID, "frostoverdrive"));

                        SpellHelper.performImpacts(player1.getWorld(), player1, target1, player1, spell1, new SpellHelper.ImpactContext());
                    }
                    ParticleHelper.sendBatches(player1, spell.release.particles);
                    SpellHelper.AmmoResult ammoResult = ammoForSpell(player2, spell, stack);
                    if (ammoResult.ammo() != null) {
                        for (int ii = 0; ii < player2.getInventory().size(); ++ii) {
                            ItemStack stack1 = player2.getInventory().getStack(ii);
                            if (stack1.isOf(ammoResult.ammo().getItem())) {
                                stack1.decrement(1);
                                if (stack1.isEmpty()) {
                                    player2.getInventory().removeOne(stack1);
                                }
                                break;
                            }
                        }

                    }
                }
            });
        }

    }
    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void hexLivingAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(OMNI);

    }
}
