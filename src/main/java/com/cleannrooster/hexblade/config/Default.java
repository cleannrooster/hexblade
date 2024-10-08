package com.cleannrooster.hexblade.config;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.cleannrooster.hexblade.Armors.Armors;
import com.cleannrooster.hexblade.item.Items;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.api.loot.LootConfig;

public class Default {
    public static final ItemConfig itemConfig = new ItemConfig();
    public static final LootConfig lootConfig;

    public Default() {
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return (List)Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }

    static {
        Iterator var0 = Items.entries.iterator();

        while(var0.hasNext()) {
            Weapon.Entry weapon = (Weapon.Entry)var0.next();
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }

        var0 = Armors.entries.iterator();

        while(var0.hasNext()) {
            Armor.Entry armor = (Armor.Entry)var0.next();
            itemConfig.armor_sets.put(armor.name(), armor.defaults());
        }

        lootConfig = new LootConfig();
        new LootConfig.Pool();
        String var3 = "weapons";
    }
}
