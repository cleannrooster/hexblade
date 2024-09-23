package com.cleannrooster.hexblade;

import com.cleannrooster.hexblade.Armors.Armors;
import com.cleannrooster.hexblade.block.HexbladeBlockItem;
import com.cleannrooster.hexblade.config.ConfigSync;
import com.cleannrooster.hexblade.config.ServerConfig;
import com.cleannrooster.hexblade.config.ServerConfigWrapper;
import com.cleannrooster.hexblade.effect.Hex;
import com.cleannrooster.hexblade.entity.HexbladePortal;
import com.cleannrooster.hexblade.entity.Magister;
import com.cleannrooster.hexblade.entity.Magus;
import com.cleannrooster.hexblade.invasions.attackevent;
import com.cleannrooster.hexblade.item.*;
import com.cleannrooster.spellblades.SpellbladesAndSuch;
import com.cleannrooster.spellblades.effect.CustomEffect;
import com.cleannrooster.spellblades.items.loot.Default;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

import static com.extraspellattributes.ReabsorptionInit.CONVERTTOARCANE;
import static com.extraspellattributes.ReabsorptionInit.MOD_ID;
import static net.minecraft.registry.Registries.ENTITY_TYPE;

public class Hexblade implements ModInitializer {
	public static final String MOD_ID = "hexblade";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static EntityType<Magister> REAVER;
	public static ArrayList<attackevent> attackeventArrayList = new ArrayList<>();
	public static RegistryEntry<EntityAttribute> OMNI;

	public static final RegistryEntry.Reference<StatusEffect> HEXED;
	public static final RegistryEntry.Reference<StatusEffect> MAGISTERFRIEND ;
	public static final RegistryEntry.Reference<StatusEffect> PORTALSICKNESS ;
	public static ServerConfig config;
	public static Item RUNEBLAZEPLATE = new Item(new Item.Settings().maxCount(64));
	public static Item RUNEFROSTPLATE = new Item(new Item.Settings().maxCount(64));
	public static Item RUNEGLEAMPLATE = new Item(new Item.Settings().maxCount(64));
	public static Item PRISMATIC = new PrismaticEffigy(new Item.Settings());
	public static Item THREAD = new Item(new Item.Settings().maxCount(64));
	public static final Item ASHES = new Ashes(new Item.Settings().maxCount(1));
	public static final Item OMNIS = new Omni(new Item.Settings().maxCount(1));

	public static final Item OFFERING = new Offering(new Item.Settings());
	public static EntityType<HexbladePortal> HEXBLADEPORTAL;
	public static final Block HEXBLADE = new com.cleannrooster.hexblade.block.Hexblade(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(5.0F, 6.0F).requiresTool().requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque());

	public static final Item HEXBLADEITEM = new HexbladeBlockItem(HEXBLADE,new Item.Settings());
	public static final Identifier SINCELASTHEX = Identifier.of(MOD_ID, "threat");
	public static final Identifier HEXRAID = Identifier.of(MOD_ID, "hex");
	public static Item MAGUS_SPAWN_EGG ;
	public static Item MAGISTER_EGG ;
	public static final GameRules.Key<GameRules.BooleanRule> SHOULD_INVADE = GameRuleRegistry.register("hexbladeInvade", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));

	public static final RegistryKey<World> DIMENSIONKEY = RegistryKey.of(RegistryKeys.WORLD,Identifier.of(Hexblade.MOD_ID,"glassocean"));

	public static final RegistryKey<DimensionType> DIMENSION_TYPE_RESOURCE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,Identifier.of(Hexblade.MOD_ID,"glassocean"));

	private static PacketByteBuf configSerialized = PacketByteBufs.create();

	public static EntityType<Magus> ARCHMAGUS;

	public static ItemGroup SPELLBLADES;
	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<ItemConfig>
			("items_v1", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),Identifier.of(Hexblade.MOD_ID,"generic"));


	static {
		HEXED = Registry.registerReference(Registries.STATUS_EFFECT,Identifier.of(MOD_ID,"hexed"), new Hex(StatusEffectCategory.HARMFUL, 0xff4bdd));
		MAGISTERFRIEND = Registry.registerReference(Registries.STATUS_EFFECT,Identifier.of(MOD_ID,"magisterfriend"), new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xff4bdd));
		PORTALSICKNESS = Registry.registerReference(Registries.STATUS_EFFECT,Identifier.of(MOD_ID,"portalsickness"),new CustomEffect(StatusEffectCategory.HARMFUL, 0xff4bdd));
		OMNI = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "omni"), new ClampedEntityAttribute("attribute.name.hexblade.omni", 0,0,9999));

	}
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));

		config = AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig().server;

		itemConfig.refresh();
		Items.register(itemConfig.value.weapons);
		Armors.register(itemConfig.value.armor_sets);
		Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"ashes"), ASHES);
		Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"omni"), OMNIS);

		((ClampedEntityAttribute)(OMNI.value())).setTracked(true);

		Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"offering"), OFFERING);
		ARCHMAGUS = Registry.register(
				ENTITY_TYPE,
				Identifier.of(MOD_ID, "magus"),
				FabricEntityTypeBuilder.<Magus>create(SpawnGroup.MISC, Magus::new)
						.dimensions(EntityDimensions.fixed(0.6F, 1.8F)) // dimensions in Minecraft units of the render
						.trackRangeBlocks(128)
						.trackedUpdateRate(1)
						.build()
		);
		REAVER = Registry.register(
				ENTITY_TYPE,
				Identifier.of(MOD_ID, "magister"),
				FabricEntityTypeBuilder.<Magister>create(SpawnGroup.MISC, Magister::new)
						.dimensions(EntityDimensions.fixed(0.6F, 1.8F)) // dimensions in Minecraft units of the render
						.trackRangeBlocks(128)
						.trackedUpdateRate(1)
						.build()
		);

		MAGUS_SPAWN_EGG = new SpawnEggItem(ARCHMAGUS, 0x09356B, 0xebcb6a, new Item.Settings());
		MAGISTER_EGG = new SpawnEggItem(REAVER, 0xA020F0, 0xebcb6a, new Item.Settings());

		SPELLBLADES = FabricItemGroup.builder()
				.icon(() -> new ItemStack(HEXBLADEITEM))
				.displayName(Text.translatable("itemGroup.hexblade.general"))
				.build();
		Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "magus_spawn_egg"), MAGUS_SPAWN_EGG);
		Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "hexblade_magister_spawn_egg"), MAGISTER_EGG);

		Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "thread"), THREAD);
		Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "prismatic"), PRISMATIC);
		Registry.register(Registries.ITEM, Identifier.of(SpellbladesAndSuch.MOD_ID, "runeblaze_plate"), RUNEBLAZEPLATE);
		Registry.register(Registries.ITEM, Identifier.of(SpellbladesAndSuch.MOD_ID, "runefrost_plate"), RUNEFROSTPLATE);
		Registry.register(Registries.ITEM, Identifier.of(SpellbladesAndSuch.MOD_ID, "runegleam_plate"), RUNEGLEAMPLATE);

		Registry.register(Registries.CUSTOM_STAT, "threat", SINCELASTHEX);
		Registry.register(Registries.CUSTOM_STAT, "hex", HEXRAID);
		Registry.register(Registries.ITEM_GROUP, KEY, SPELLBLADES);
		Registry.register(Registries.BLOCK,Identifier.of(MOD_ID,"hexblade"),HEXBLADE);

		Registry.register(Registries.ITEM, Identifier.of(MOD_ID,"hexbladeitem"), HEXBLADEITEM);
		ItemGroupEvents.modifyEntriesEvent(SpellbladesAndSuch.KEY).register((content) -> {

			content.add(RUNEBLAZEPLATE);
			content.add(RUNEFROSTPLATE);
			content.add(RUNEGLEAMPLATE);
				})
		;
		ItemGroupEvents.modifyEntriesEvent(KEY).register((content) -> {

			content.add(HEXBLADEITEM);
			content.add(OFFERING);

			content.add(MAGUS_SPAWN_EGG);
			content.add(MAGISTER_EGG);

			content.add(PRISMATIC);
			content.add(THREAD);
			content.add(ASHES);
			content.add(OMNIS);

			/*content.add(RIFLE);*/
		});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

				if (player.getWorld().getRegistryKey().equals(DIMENSIONKEY) && player.getY() < -32) {
					player.requestTeleport(player.getX(), 150, player.getZ());
				}
				if (((int) (player.getWorld().getTimeOfDay() % 24000L)) % 1200 == 0 && server.getGameRules().getBoolean(SHOULD_INVADE) &&server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE) && Hexblade.config.hexblade_on) {

					if (player.getWorld().getRegistryKey().equals(DIMENSIONKEY) && !player.hasStatusEffect(PORTALSICKNESS)  && !(Math.abs(player.getPos().getX()) < config.hexblade_grace  && Math.abs(player.getPos().getZ()) < config.hexblade_grace) && player.getWorld().isSkyVisible(player.getBlockPos().up())) {
						attackeventArrayList.add(new attackevent(player.getWorld(), player));
					}


					if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(HEXRAID)) > 0 && !player.hasStatusEffect(PORTALSICKNESS)) {
						if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(SINCELASTHEX)) == 9) {
							player.sendMessage(Text.translatable("Your use of magic has not gone unnoticed.").formatted(Formatting.LIGHT_PURPLE));
						}
						player.increaseStat(SINCELASTHEX, 1);

							if (!player.hasStatusEffect(HEXED) && player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(SINCELASTHEX)) > 10 && player.getRandom().nextFloat() < config.spawnmodifier* 0.01 * (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(HEXRAID)) / 100F) * Math.pow((1.02930223664), player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(SINCELASTHEX)))) {

								Optional<BlockPos> pos2 = BlockPos.findClosest(player.getBlockPos(), 64, 128,
										blockPos -> player.getWorld().getBlockState(blockPos).getBlock().equals(HEXBLADE));
								if (pos2.isPresent() || player.getInventory().containsAny(item -> item.getItem() instanceof HexbladeBlockItem)) {
								} else {
									player.addStatusEffect(new StatusEffectInstance(HEXED, 20 * 60 * 3, 0, false, false));
								}
							
						}
					}
					player.getStatHandler().setStat(player, Stats.CUSTOM.getOrCreateStat(HEXRAID), 0);
				}

			}
			attackeventArrayList.removeIf(attackevent -> attackevent.tickCount > 500 || attackevent.done);
			for (attackevent attackEvent : attackeventArrayList) {
				attackEvent.tick();
			}
		});

		HEXBLADEPORTAL = Registry.register(
				ENTITY_TYPE,
				Identifier.of(MOD_ID, "hexbladeportal"),
				FabricEntityTypeBuilder.<HexbladePortal>create(SpawnGroup.MISC, HexbladePortal::new)
						.dimensions(EntityDimensions.fixed(2F, 3F)) // dimensions in Minecraft units of the render
						.trackRangeBlocks(128)
						.trackedUpdateRate(1)
						.build()
		);

		for(SpellSchool school : SpellSchools.all()) {
			school.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, queryArgs -> {
				double additional = 0;
				for(SpellSchool school2 : SpellSchools.all()) {
					if (!(school2.equals(ExternalSpellSchools.PHYSICAL_MELEE) || school2.equals(ExternalSpellSchools.PHYSICAL_RANGED))) {
						additional += queryArgs.entity().getAttributeValue(school2.attributeEntry) * queryArgs.entity().getAttributeValue(OMNI);

					}
				}
				return additional;

			});
		}

		FabricDefaultAttributeRegistry.register(ARCHMAGUS,Magus.createAttributes());
		FabricDefaultAttributeRegistry.register(HEXBLADEPORTAL,HexbladePortal.createAttributes());

		FabricDefaultAttributeRegistry.register(REAVER,Magister.createAttributes());

		LOGGER.info("Hello Fabric world!");
	}
}