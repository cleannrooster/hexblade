package com.cleannrooster.hexblade.spells;

import com.cleannrooster.hexblade.Hexblade;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class HexbladeSpells {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);

        return entry;
    }
    public static Spell activeSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 7;
        spell.learn = new Spell.Learn();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.scroll = new Spell.Active.Scroll();


        return spell;
    }
    private static Spell.Delivery createDelivery(Spell.Delivery.Type type) {
        var delivery = new Spell.Delivery();
        delivery.type = type;
        return delivery;

    }
    private static Spell.Impact createImpact(Spell.Impact.Action.Type type, float coeff, float knockback) {
        var impact = new Spell.Impact();
        impact.action = new Spell.Impact.Action();
        impact.action.type = type;
        if(type == Spell.Impact.Action.Type.DAMAGE) {
            impact.action.damage = new Spell.Impact.Action.Damage();
            impact.action.damage.knockback = knockback;
            impact.action.damage.spell_power_coefficient = coeff;
        }
        return impact;
    }
    private static Spell projectileBase(SpellSchool school, Identifier projIdentifier, float velocity, float knockback) {

        var spell = activeSpellBase();
        spell.school = school;


        var delivery = createDelivery(Spell.Delivery.Type.PROJECTILE);
        delivery.projectile = new Spell.Delivery.ShootProjectile();

        delivery.projectile.launch_properties.velocity = velocity;

        delivery.projectile.projectile = new Spell.ProjectileData();

        delivery.projectile.projectile.client_data = new Spell.ProjectileData.Client();

        var model = new Spell.ProjectileModel();
        model.model_id = String.valueOf(projIdentifier);
        delivery.projectile.projectile.client_data.model = model;
        spell.deliver = delivery;
        Spell.Impact[] impact = new Spell.Impact[1];
        impact[0] = createImpact(Spell.Impact.Action.Type.DAMAGE,1.8F,knockback);
        spell.impacts = List.of(impact[0]);

        return spell;
    }
    public static Spell.Impact createArcaneImpact(float coeff, float knockback) {
        var impact = createImpact(Spell.Impact.Action.Type.DAMAGE,coeff, knockback);
        impact.school = SpellSchools.ARCANE;
        ParticleBatch[] hitParticles = new ParticleBatch[]{
                new ParticleBatch("spell_engine:magic_arcane_impact_burst", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK, 20, 0.2f, 0.7F, 360)
        };
        impact.particles = hitParticles;
        var sound = new Sound(SpellEngineSounds.GENERIC_ARCANE_RELEASE.id());
        impact.sound = sound;
        return impact;
    }
    public static Spell.Impact createLightningImpact(float coeff, float knockback) {
        var impact = createImpact(Spell.Impact.Action.Type.DAMAGE,coeff, knockback);
        impact.school = SpellSchools.LIGHTNING;
        ParticleBatch[] hitParticles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.getMagicParticleVariant(SpellEngineParticles.WHITE, SpellEngineParticles.MagicParticleFamily.Shape.IMPACT, SpellEngineParticles.MagicParticleFamily.Motion.BURST).id().toString(), ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK, 20, 0.2f, 0.7F, 360)
        };
        impact.particles = hitParticles;
        var sound = new Sound(SpellEngineSounds.GENERIC_LIGHTNING_RELEASE.id());
        impact.sound = sound;
        return impact;
    }
    private static Spell.Impact createFrostImpact(float coeff, float knockback) {
        var impact = createImpact(Spell.Impact.Action.Type.DAMAGE,coeff, knockback);
        impact.school = SpellSchools.FROST;
        ParticleBatch[] hitParticles = new ParticleBatch[]{
                new ParticleBatch("spell_engine:magic_frost_impact_burst", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK, 20, 0.2f, 0.7F, 360)
        };
        impact.particles = hitParticles;
        Spell.TargetCondition targetCondition = new Spell.TargetCondition();
        targetCondition.entity_type = "#minecraft:freeze_immune_entity_types";
        Spell.Impact.TargetModifier targetModifier = new Spell.Impact.TargetModifier();

        targetModifier.conditions = List.of(targetCondition);
        targetModifier.modifier = new Spell.Impact.Modifier();
        targetModifier.modifier.power_multiplier = -0.3F;
        Spell.Impact.TargetModifier targetModifier2 = new Spell.Impact.TargetModifier();
        targetCondition.entity_type = "#minecraft:freeze_hurts_extra_types";
        targetModifier2.conditions = List.of(targetCondition);
        targetModifier2.modifier = new Spell.Impact.Modifier();
        targetModifier2.modifier.power_multiplier = 0.3F;
        impact.target_modifiers = List.of(targetModifier,targetModifier2);
        var sound = new Sound(SpellEngineSounds.GENERIC_FROST_IMPACT.id());
        impact.sound = sound;
        return impact;
    }
    private static Spell.Impact createFireImpact(float coeff, float knockback) {
        var impact = createImpact(Spell.Impact.Action.Type.DAMAGE,coeff, knockback);
        impact.school = SpellSchools.FIRE;
        ParticleBatch[] hitParticles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(), ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK, 20, 0.2f, 0.7F, 360),
                new ParticleBatch("minecraft:smoke", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK, 20, 0.2f, 0.7F, 360)

        };
        Spell.TargetCondition targetCondition = new Spell.TargetCondition();
        targetCondition.entity_type = "#minecraft:freeze_immune_entity_types";
        Spell.Impact.TargetModifier targetModifier = new Spell.Impact.TargetModifier();

        targetModifier.conditions = List.of(targetCondition);
        targetModifier.modifier = new Spell.Impact.Modifier();
        targetModifier.modifier.critical_chance_bonus = 0.3F;

        impact.target_modifiers = List.of(targetModifier);
        impact.particles = hitParticles;

        var sound = new Sound("minecraft:entity.player.hurt_on_fire");
        impact.sound = sound;
        return impact;
    }
    private static ParticleBatch arcaneCastingParticles() {
        return new ParticleBatch(
                SpellEngineParticles.getMagicParticleVariant(
                        SpellEngineParticles.ARCANE,
                        SpellEngineParticles.MagicParticleFamily.Shape.SPELL,
                        SpellEngineParticles.MagicParticleFamily.Motion.ASCEND
                ).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                1, 0.05F, 0.1F);
    }
    private static ParticleBatch fireCastingParticles() {
        return new ParticleBatch(
                SpellEngineParticles.flame.id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                1, 0.05F, 0.1F);
    }
    private static ParticleBatch frostCastingParticles() {
        return new ParticleBatch(
                SpellEngineParticles.getMagicParticleVariant(
                        SpellEngineParticles.FROST,
                        SpellEngineParticles.MagicParticleFamily.Shape.SPELL,
                        SpellEngineParticles.MagicParticleFamily.Motion.ASCEND
                ).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                1, 0.05F, 0.1F);
    }
    private static Spell.Active.Cast createCast(int channelticks, float duration, String sound, String animation, @Nullable SpellSchool school) {
        var cast = new Spell.Active.Cast();
        cast = new Spell.Active.Cast();
        cast.animation = animation;
        cast.sound = new Sound(sound);
        cast.channel_ticks = channelticks;
        cast.duration = duration;
        if(school != null) {
            if (school.equals(SpellSchools.FIRE)) {
                cast.particles = new ParticleBatch[]{fireCastingParticles()};
            }
            if (school.equals(SpellSchools.FROST)) {
                cast.particles = new ParticleBatch[]{frostCastingParticles()};
            }
            if (school.equals(SpellSchools.ARCANE)) {
                cast.particles = new ParticleBatch[]{arcaneCastingParticles()};
            }
        }
        return cast;
    }
    private static void configureCooldown(Spell spell, float duration, boolean proportional,@Nullable String id) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        if (spell.cost.cooldown == null) {
            spell.cost.cooldown = new Spell.Cost.Cooldown();
        }
        if(id != null){
            spell.cost.item = new Spell.Cost.Item();
            spell.cost.item.id = id;
            spell.cost.item.amount = 1;

        }
        if(proportional){
            spell.cost.cooldown.proportional = true;
        }
        spell.cost.cooldown.duration = duration;
    }
    private static Entry frost_slash = add(frost_slash());
    private static Entry frost_slash() {
        var spell = projectileBase(SpellSchools.ARCANE, Identifier.of("spellbladenext:projectile/gladius"),1F,0);
        spell.school = SpellSchools.FROST;

        var id = Identifier.of(Hexblade.MOD_ID, "frost_slash");
        var description = "Attack with a flurry of frost blade projectiles, dealing {damage} Frost damage per second.";
        var title = "Amethyst Slash";
        spell.deliver.projectile.projectile.perks.ricochet = 2;
        spell.deliver.projectile.projectile.divergence = 15;
        spell.deliver.projectile.projectile.client_data.model.scale = 1;
        spell.deliver.projectile.projectile.homing_angle = 10;
        spell.deliver.projectile.projectile.homing_after_absolute_distance = 8;
        spell.deliver.projectile.projectile.homing_after_relative_distance = 0.4F;

        spell.learn = new Spell.Learn();
        spell.tier = 3;
        spell.range = 64;
        spell.active.cast = createCast(3,6,"spell_engine:generic_frost_casting","spell_engine:flameslash",SpellSchools.FROST);

        ParticleBatch[] particlebatch = new ParticleBatch[]{
                new ParticleBatch("spell_engine:magic_frost_spark_float", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360),
                new ParticleBatch(SpellEngineParticles.getMagicParticleVariant(SpellEngineParticles.FROST, SpellEngineParticles.MagicParticleFamily.Shape.SPARK, SpellEngineParticles.MagicParticleFamily.Motion.FLOAT).id().toString()
                        , ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360)

        };

        spell.deliver.projectile.projectile.client_data.travel_particles = particlebatch;
        Spell.Impact[] impacts = new Spell.Impact[1];
        impacts[0] = createFrostImpact(1.8F,0F);

        spell.impacts = List.of(impacts[0]);

        configureCooldown(spell, 4, true, "runes:frost_stone");

        return new Entry(id, spell, title, description, null);
    }
    private static Entry flame_slash = add(flame_slash());
    private static Entry flame_slash() {
        var spell = projectileBase(SpellSchools.ARCANE,Identifier.of("spellbladenext:projectile/flamewaveprojectile"),1F,0);
        spell.school = SpellSchools.FIRE;

        var id = Identifier.of(Hexblade.MOD_ID, "flame_slash");
        var description = "Attack with a flurry of flame wave projectiles, dealing {damage} Fire damage per second.";
        var title = "Flame Slash";
        spell.deliver.projectile.projectile.divergence = 15;
        spell.deliver.projectile.projectile.client_data.model.scale = 1;
        spell.deliver.projectile.projectile.homing_angle = 10;
        spell.deliver.projectile.projectile.homing_after_absolute_distance = 8;
        spell.deliver.projectile.projectile.homing_after_relative_distance = 0.4F;
        spell.learn = new Spell.Learn();
        spell.tier = 3;
        spell.range = 64;
        spell.active.cast = createCast(3,6,"spell_engine:generic_frost_casting","spell_engine:flameslash",SpellSchools.FROST);

        ParticleBatch[] particlebatch = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame.id().toString(), ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER, null,3,0.05f,0.1F,360),
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(), ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER, null,1,0.05f,0.1F,360),
                new ParticleBatch(SpellEngineParticles.flame_medium_b.id().toString(), ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER, null,1,0.05f,0.1F,360),
                new ParticleBatch("minecraft:smoke", ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360)

        };

        spell.deliver.projectile.projectile.client_data.travel_particles = particlebatch;
        Spell.Impact[] impacts = new Spell.Impact[1];
        impacts[0] = createFireImpact(1F,0F);

        spell.impacts = List.of(impacts[0]);

        configureCooldown(spell, 4, true, "runes:fire_stone");

        return new Entry(id, spell, title, description, null);
    }
    private static Entry amethyst_slash = add(amethyst_slash());
    private static Entry amethyst_slash() {
        var spell = projectileBase(SpellSchools.ARCANE,Identifier.of("spellbladenext:projectile/amethyst"),1F,0);
        spell.school = SpellSchools.ARCANE;
        spell.deliver.projectile.projectile.perks.bounce = 4;
        spell.deliver.projectile.projectile.divergence = 15;
        spell.deliver.projectile.projectile.client_data.model.scale = 1;
        spell.deliver.projectile.projectile.homing_angle = 10;
        spell.deliver.projectile.projectile.homing_after_absolute_distance = 8;
        spell.deliver.projectile.projectile.homing_after_relative_distance = 0.4F;
        var id = Identifier.of(Hexblade.MOD_ID, "amethyst_slash");
        var description = "Attack with a flurry of amethyst projectiles, dealing {damage} Arcane damage per second.";
        var title = "Amethyst Slash";

        spell.learn = new Spell.Learn();
        spell.tier = 3;
        spell.range = 64;
        spell.active.cast = createCast(3,6,"spell_engine:generic_arcane_casting","spell_engine:flameslash", SpellSchools.ARCANE);

        ParticleBatch[] particlebatch = new ParticleBatch[]{
                new ParticleBatch("spell_engine:magic_arcane_spark_float", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360),
                new ParticleBatch(SpellEngineParticles.getMagicParticleVariant(SpellEngineParticles.ARCANE, SpellEngineParticles.MagicParticleFamily.Shape.SPARK, SpellEngineParticles.MagicParticleFamily.Motion.FLOAT).id().toString()
                        , ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360)

        };

        spell.deliver.projectile.projectile.client_data.travel_particles = particlebatch;
        Spell.Impact[] impacts = new Spell.Impact[1];
        impacts[0] = createArcaneImpact(1F,0F);

        spell.impacts = List.of(impacts[0]);

        configureCooldown(spell, 4, true, "runes:arcane_stone");

        return new Entry(id, spell, title, description, null);
    }
    private static Entry amethyst_slash2 = add(amethyst_slash2());
    private static Entry amethyst_slash2() {
        var spell = projectileBase(SpellSchools.ARCANE,Identifier.of("spellbladenext:projectile/amethyst"),0.1F,0);
        spell.school = SpellSchools.ARCANE;
        spell.deliver.projectile.projectile.perks.bounce = 4;
        spell.deliver.projectile.projectile.divergence = 15;
        spell.deliver.projectile.projectile.client_data.model.scale = 1;

        var id = Identifier.of(Hexblade.MOD_ID, "amethyst_slash2");
        var description = "Attack with a flurry of amethyst projectiles, dealing {damage} Arcane damage per second.";
        var title = "Amethyst Slash";

        spell.learn = new Spell.Learn();
        spell.tier = 3;
        spell.range = 64;
        spell.active.cast = createCast(3,6,"spell_engine:generic_arcane_casting","spell_engine:flameslash", SpellSchools.ARCANE);

        ParticleBatch[] particlebatch = new ParticleBatch[]{
                new ParticleBatch("spell_engine:magic_arcane_spark_float", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360),
                new ParticleBatch(SpellEngineParticles.getMagicParticleVariant(SpellEngineParticles.ARCANE, SpellEngineParticles.MagicParticleFamily.Shape.SPARK, SpellEngineParticles.MagicParticleFamily.Motion.FLOAT).id().toString()
                        , ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, null,2,0.05f,0.1F,360)

        };

        spell.deliver.projectile.projectile.client_data.travel_particles = particlebatch;
        Spell.Impact[] impacts = new Spell.Impact[1];
        impacts[0] = createArcaneImpact(1F,0F);

        spell.impacts = List.of(impacts[0]);

        configureCooldown(spell, 4, true, "runes:arcane_stone");

        return new Entry(id, spell, title, description, null);
    }
}
