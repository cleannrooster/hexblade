package com.cleannrooster.hexblade.effect;

import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.spellblades.effect.CustomEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Effects {
    private static final ArrayList<Entry> entries = new ArrayList<>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(Hexblade.MOD_ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(Hexblade.MOD_ID, "effect." + id.getPath());
        }
    }

    public static final Entry HEXED = new Entry("hexed",
            new Hex(StatusEffectCategory.HARMFUL, 0xff0000));
    public static final Entry MAGISTERFRIEND = new Entry("magisterfriend",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xff0000));
    public static final Entry PORTALSICKNESS = new Entry("portalsickness",
            new CustomEffect(StatusEffectCategory.HARMFUL, 0x993333));


    public static void register() {

        for (var entry: entries) {
            entry.register();
        }
    }
}