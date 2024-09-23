package com.cleannrooster.hexblade.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server")
public class ServerConfig  implements ConfigData {
    public ServerConfig(){}
    @Comment("Upper bound of Ashes of the Stars (Default: 15)")
    public  int ashes_upper_bound = 15;
    @Comment("Lower bound of Ashes of the Stars (Default: 5)")
    public  int ashes_lower_bound = 5;
    @Comment("Upper bound of Crystallised Omniscience (Default: 70)")
    public  int omni_upper = 70;
    @Comment("Lower bound of Crystallised Omniscience (Default: 50)")
    public  int omni_lower = 50;
    @Comment("Coefficient for Voidforge gained damage (Default: 1x, results in 100% gained as)")
    public  int voidforge = 1;

    @Comment("Distance from 0,0 below which Hexblades will not invade in the Glass Ocean (Default: 0)")
    public  int hexblade_grace = 0;
    @Comment("Damage multiplier for spin attacks (eg: Whirlwind) (Default: 1.0)")
    public  float spin_attack_coeff = 1.0F;
    @Comment("Hexblade module (Default: TRUE)")
    public  boolean hexblade_on = true;
    @Comment("Magus changes the weather on phase end (Default: TRUE)")
    public  boolean magusWeather = true;
    @Comment("Magus can be summoned outside the glass ocean (Default: TRUE)")
    public  boolean magusWeaker = true;
    @Comment("Reactive ward regen rate coefficient (Default: 1.0)")
    public  float wardrate = 1.0F;
    @Comment("Horde Spawn with Mutations (Only with Fight or Die: Mutations)")
    public  boolean horde = false;
    @Comment("Enable Teleporting to Glass Ocean")
    public  boolean glassocean = true;
    @Comment("Horde/Hexblade spawn modifier (Default: 1.0)")

    public  float spawnmodifier = 1.0F;

    @Comment("Magisters give different xp in the Glass Ocean (Default: false)")
    public  boolean diffXP = false;
    @Comment("Magister XP in the Glass Ocean, if different (Default: 25)")
    public  int magisterXP = 25;
    @Comment("Do not apply Spell Oil capability to any item matching this regex. (Not applied of empty)")
    public String blacklist_spell_oil_regex = "";
    @Comment("Enable tabula rasa (Default: true")
    public boolean tab = true;

}
