package com.cleannrooster.hexblade.data;

import com.cleannrooster.hexblade.Armors.Armors;
import com.cleannrooster.hexblade.item.Items;
import com.cleannrooster.hexblade.spells.HexbladeSpells;
import com.cleannrooster.spellblades.Spells.SpellbladeSpells;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.concurrent.CompletableFuture;

public class HexbladeDatagen implements DataGeneratorEntrypoint {


    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(HexbladeDatagen.ItemTagGenerator::new);
        pack.addProvider(HexbladeDatagen.SpellbladesSpellGen::new);

    }

    public static class SpellbladesSpellGen extends SpellGenerator {
        public SpellbladesSpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry: HexbladeSpells.entries) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }


    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            generateArmorTags(Armors.entries, RPGSeriesItemTags.ArmorMetaType.MAGIC);
            generateWeaponTags(Items.entries);

        }
    }

}