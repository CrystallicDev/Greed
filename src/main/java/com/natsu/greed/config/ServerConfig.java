package com.natsu.greed.config;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.natsu.greed.server.enchants.EnchantmentTableState;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class ServerConfig {

	public static final ForgeConfigSpec SPEC;
	
	// # Custom Enchant system
	public static final ForgeConfigSpec.BooleanValue USE_ENCHANTING_SYSTEM;
	public static final ForgeConfigSpec.BooleanValue DISABLE_BOOKSHELVES_CAP;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DEFAULTSTAGE_CURSE_LIST;
	public static final ForgeConfigSpec.DoubleValue DEFAULTSTAGE_CURSE_PROBA;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DEFAULTSTAGE_ENCHANTMENT_BAN_LIST;
	public static final ForgeConfigSpec.BooleanValue DEFAULTSTAGE_ENCHANT_ISWHITELIST;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LAPIS_CURSE_LIST;
	public static final ForgeConfigSpec.DoubleValue LAPIS_CURSE_PROBA;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LAPIS_ENCHANTMENT_BAN_LIST;
	public static final ForgeConfigSpec.BooleanValue LAPIS_ENCHANT_ISWHITELIST;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AMETHYST_CURSE_LIST;
	public static final ForgeConfigSpec.DoubleValue AMETHYST_CURSE_PROBA;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AMETHYST_ENCHANTMENT_BAN_LIST;
	public static final ForgeConfigSpec.BooleanValue AMETHYST_ENCHANT_ISWHITELIST;
	
	// # Custom Enchants Values
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_RARITY;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANTMENTS_RARITY;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_MAX_LEVELS;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANTMENTS_MAX_LEVELS;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_MAX_COST;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANTMENTS_MAX_COST;
	
	// # Custom Trades
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_BOOK_TRADES;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_MAP_TRADES;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_CLERIC_TRADES;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_ARMOR_TRADES;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_TOOL_TRADES;
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_WEAPON_TRADES;
	
	// # Custom Cauldrons
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_CAULDRONS;
	public static final ForgeConfigSpec.DoubleValue CAULDRONS_POTION_DURATION_MERGE_FACTOR;
	
	// # Custom Witches Thrown potions
	public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_WITCHES_POTION;
	
	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("# Enchanting Table Stages");
		USE_ENCHANTING_SYSTEM = builder.define("useEnchantingTableStages", true);
		DISABLE_BOOKSHELVES_CAP = builder.define("disableBookshelvesCap", true);
		
		builder.push("# Default Stage (Any block)");
		DEFAULTSTAGE_CURSE_LIST = builder.defineList("defaultStage_curseList",
			    List.of("minecraft:curse_of_vanishing",
			    		"minecraft:curse_of_binding",
			    		"greed:curse_of_absorption",
			    		"greed:curse_of_combination",
			    		"greed:curse_of_creeping",
			    		"greed:curse_of_heavyweight",
			    		"greed:curse_of_scarcity",
			    		"greed:curse_of_the_sponge",
			    		"greed:curse_of_voiding"),
			    obj -> {
			    	if (!(obj instanceof String s)) return false;
			        ResourceLocation rl = ResourceLocation.tryParse(s);
			        if (rl == null) { System.out.println("Cannot find curse : "+s); }
			        return rl != null && ForgeRegistries.ENCHANTMENTS.containsKey(rl);
			    });
		
		DEFAULTSTAGE_CURSE_PROBA = builder
				.comment("The probability on each enchant option to have a curse (Ranging from 0 - 1)")
				.defineInRange("defaultStage_curseProbability", 0.25d, 0d, 1d);
		
		DEFAULTSTAGE_ENCHANTMENT_BAN_LIST = builder
				.comment("The list of enchantment that CANNOT be obtained in the enchanting table\n"
						+ "when in the default stage.")
				.defineList("defaultStage_enchantBanList",
			    List.of("minecraft:silk_touch",
			    		"minecraft:fortune",
			    		"minecraft:looting",
			    		"minecraft:mending",
			    		"minecraft:riptide",
			    		"minecraft:infinity",
			    		"minecraft:flame",
			    		"minecraft:unbreaking",
			    		"minecraft:fire_aspect",
			    		"minecraft:frost_walker"),
			    obj -> {
			    	if (!(obj instanceof String s)) return false;
			        ResourceLocation rl = ResourceLocation.tryParse(s);
			        if (rl == null) { System.out.println("Cannot find enchant : "+s); }
			        return rl != null && ForgeRegistries.ENCHANTMENTS.containsKey(rl);
			    });
		DEFAULTSTAGE_ENCHANT_ISWHITELIST = builder
				.comment("When true, reverse the previous ban list as a white list. Only listed enchantments will\n"
						+ " be obtainable."
				).define("defaultStage_useBanListAsWhitelist", false);
		
		
		builder.pop();
		builder.push("# Lapis Stage (Lapis Lazuli block)");
		LAPIS_CURSE_LIST = builder.defineList("lapisStage_curseList",
			    List.of("minecraft:curse_of_vanishing",
			    		"minecraft:curse_of_binding",
			    		"greed:curse_of_absorption",
			    		"greed:curse_of_combination",
			    		"greed:curse_of_creeping",
			    		"greed:curse_of_heavyweight",
			    		"greed:curse_of_scarcity",
			    		"greed:curse_of_the_sponge",
			    		"greed:curse_of_voiding"),
			    obj -> {
			    	if (!(obj instanceof String s)) return false;
			        ResourceLocation rl = ResourceLocation.tryParse(s);
			        if (rl == null) { System.out.println("Cannot find curse : "+s); }
			        return rl != null && ForgeRegistries.ENCHANTMENTS.containsKey(rl);
			    });
		
		LAPIS_CURSE_PROBA = builder
				.comment("The probability on each enchant option to have a curse (Ranging from 0 - 1)")
				.defineInRange("lapisStage_curseProbability", 0.15d, 0d, 1d);
		
		LAPIS_ENCHANTMENT_BAN_LIST = builder
				.comment("The list of enchantment that CANNOT be obtained in the enchanting table\n"
						+ "when in the lapis stage.")
				.defineList("lapisStage_enchantBanList",
					    List.of("minecraft:fortune",
					    		"minecraft:looting",
					    		"minecraft:mending",
					    		"minecraft:riptide",
					    		"minecraft:infinity",
					    		"minecraft:flame",
					    		"minecraft:fire_aspect",
					    		"minecraft:frost_walker"),
			    obj -> {
			    	if (!(obj instanceof String s)) return false;
			        ResourceLocation rl = ResourceLocation.tryParse(s);
			        if (rl == null) { System.out.println("Cannot find enchant : "+s); }
			        return rl != null && ForgeRegistries.ENCHANTMENTS.containsKey(rl);
			    });
		LAPIS_ENCHANT_ISWHITELIST = builder
				.comment("When true, reverse the previous ban list as a white list. Only listed enchantments will\n"
						+ " be obtainable."
				).define("lapisStage_useBanListAsWhitelist", false);
		
		

		builder.pop();
		builder.push("# Amethyst Stage (Amethyst Cluster block)");
		AMETHYST_CURSE_LIST = builder.defineList("amethystStage_curseList",
			    new ArrayList<>(),
			    obj -> {
			    	if (!(obj instanceof String s)) return false;
			        ResourceLocation rl = ResourceLocation.tryParse(s);
			        if (rl == null) { System.out.println("Cannot find curse : "+s); }
			        return rl != null && ForgeRegistries.ENCHANTMENTS.containsKey(rl);
			    });
		
		AMETHYST_CURSE_PROBA = builder
				.comment("The probability on each enchant option to have a curse (Ranging from 0 - 1)")
				.defineInRange("amethystStage_curseProbability", 0d, 0d, 1d);
		
		AMETHYST_ENCHANTMENT_BAN_LIST = builder
				.comment("The list of enchantment that CANNOT be obtained in the enchanting table\n"
						+ "when in the amethyst stage.")
				.defineList("amethystStage_enchantBanList",
			    List.of("minecraft:silk_touch"),
			    obj -> {
			    	if (!(obj instanceof String s)) return false;
			        ResourceLocation rl = ResourceLocation.tryParse(s);
			        if (rl == null) { System.out.println("Cannot find enchant : "+s); }
			        return rl != null && ForgeRegistries.ENCHANTMENTS.containsKey(rl);
			    });
		AMETHYST_ENCHANT_ISWHITELIST = builder
				.comment("When true, reverse the previous ban list as a white list. Only listed enchantments will\n"
						+ " be obtainable."
				).define("amethystStage_useBanListAsWhitelist", false);
		


		builder.pop();
		builder.pop();
		builder.push("# Enchantment Rarity");
		USE_CUSTOM_RARITY = builder.comment("Use a custom rarity for each enchantment. Disable this to use vanilla.").define("useCustomEnchantmentRarity", true);
		ENCHANTMENTS_RARITY = builder.comment(
	                "Map each enchantment to a rarity value:\n" +
	                "  0 = COMMON, 1 = UNCOMMON, 2 = RARE, 3 = VERY_RARE"
	        ).define("rarities", getDefaultRarities());

		builder.push("# Enchantment Max Levels");
		USE_CUSTOM_MAX_LEVELS = builder.comment("Use a custom max level for each enchantment. Disable this to use vanilla.").define("useCustomEnchantmentMaxLevels", true);
		ENCHANTMENTS_MAX_LEVELS = builder.comment(
	                "Map each enchantment to a max level."
	        ).define("maxLevels", getDefaultMaxLevels());

		builder.push("# Enchantment Max Costs");
		USE_CUSTOM_MAX_COST = builder.comment("Use a custom max cost for each enchantment. This is recommended to be put at 999, otherwise, some enchants become unavailable at higher bookshelves level. Disable this to use vanilla.").define("useCustomEnchantmentMaxCosts", true);
		ENCHANTMENTS_MAX_COST = builder.comment(
	                "Map each enchantment to a max cost."
	        ).define("maxCosts", getDefaultMaxCosts());
		

		builder.pop();
		builder.pop();
		builder.push("# Custom Villager Trades");
		USE_CUSTOM_BOOK_TRADES = builder
				.comment("Make the Librarian villagers trade really simple books (Sharpness I, Knockback I, etc) at first, but trade\n"
						+ " higher books, with multiple enchantments at higher levels.")
				.define("useCustomBooksTrades", true);
		USE_CUSTOM_MAP_TRADES = builder
				.comment("Make the Cartographer villager trades biome maps, and simple structures maps at first,\n"
						+ " and bastion, end cities of nether fortress maps at higher levels.")
				.define("useCustomMapTrades", true);
		USE_CUSTOM_CLERIC_TRADES = builder
				.comment("Make the Cleric villagers trade really simple books (Sharpness I, Knockback I, etc) at first, but trade\n"
						+ " higher books, with multiple enchantments at higher levels.")
				.define("useCustomClericTrades", true);
		USE_CUSTOM_ARMOR_TRADES = builder
				.comment("Make the weaponsmith villagers trade really simple armors at first, but trade\n"
						+ " better armor, with multiple enchantments at higher levels.")
				.define("useCustomArmorTrades", true);
		USE_CUSTOM_TOOL_TRADES = builder
				.comment("Make the weaponsmith villagers trade really simple tools at first, but trade\n"
						+ " better tools, with multiple enchantments at higher levels.")
				.define("useCustomToolTrades", true);
		USE_CUSTOM_WEAPON_TRADES = builder
				.comment("Make the weaponsmith villagers trade really simple weapons at first, but trade\n"
						+ " better weapon, with multiple enchantments at higher levels.")
				.define("useCustomWeaponTrades", true);

		builder.pop();
		builder.push("# Custom Cauldrons");
		USE_CUSTOM_CAULDRONS = builder
				.comment("Make the cauldrons able to hold potions, and able to merge multiple potions in one.\n")
				.define("useCustomCauldrons", true);
		CAULDRONS_POTION_DURATION_MERGE_FACTOR = builder
				.comment("This value is used when 2 potion of the same type are merged in a cauldron, to\n"
						+ "increase the duration. For example : potion1.duration + (0.5 * potion2.duration)")
				.defineInRange("cauldronPotionDurationMergeFactor", 0.5d, 0.1d, 1d);
		
		
		builder.pop();
		builder.push("# Witches thrown potions");
		USE_CUSTOM_WITCHES_POTION = builder
				.comment("Make the witches throw more aggressive potions, and sometime even throw lingering potions")
				.define("useCustomWitchesPotions", true);
		
		builder.pop();
		SPEC = builder.build();
	}
	
	
	public static List<Enchantment> getCurseList(EnchantmentTableState tableState, ItemStack applicableItem) {
		switch (tableState) {
		case AMETHYST_STATE:
			return AMETHYST_CURSE_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).filter(rl -> rl.canEnchant(applicableItem)).toList();
		case DEFAULT:
			return DEFAULTSTAGE_CURSE_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).filter(rl -> rl.canEnchant(applicableItem)).toList();
		case LAPIS_STATE:
			return LAPIS_CURSE_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).filter(rl -> rl.canEnchant(applicableItem)).toList();
		default:
			return DEFAULTSTAGE_CURSE_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).filter(rl -> rl.canEnchant(applicableItem)).toList();
		}
	}
	
	public static List<Enchantment> getEnchantmentList(EnchantmentTableState tableState) {
		switch (tableState) {
		case AMETHYST_STATE:
			return AMETHYST_ENCHANTMENT_BAN_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).toList();
		case DEFAULT:
			return DEFAULTSTAGE_ENCHANTMENT_BAN_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).toList();
		case LAPIS_STATE:
			return LAPIS_ENCHANTMENT_BAN_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).toList();
		default:
			return DEFAULTSTAGE_ENCHANTMENT_BAN_LIST.get().stream().map(ResourceLocation::new).map(rl -> ForgeRegistries.ENCHANTMENTS.getValue(rl)).filter(Objects::nonNull).toList();
		}
	}
	
	public static double getCurseProbability(EnchantmentTableState tableState) {
		switch (tableState) {
		case AMETHYST_STATE:
			return AMETHYST_CURSE_PROBA.get();
		case DEFAULT:
			return DEFAULTSTAGE_CURSE_PROBA.get();
		case LAPIS_STATE:
			return LAPIS_CURSE_PROBA.get();
		default:
			return DEFAULTSTAGE_CURSE_PROBA.get();
		
		}
	}

	public static boolean isWhiteList(EnchantmentTableState tableState) {
		switch (tableState) {
		case AMETHYST_STATE:
			return AMETHYST_ENCHANT_ISWHITELIST.get();
		case DEFAULT:
			return DEFAULTSTAGE_ENCHANT_ISWHITELIST.get();
		case LAPIS_STATE:
			return LAPIS_ENCHANT_ISWHITELIST.get();
		default:
			return DEFAULTSTAGE_ENCHANT_ISWHITELIST.get();
		
		}
	}
	
	public static Enchantment.Rarity intToRarity(int value) {
	    return switch (value) {
	        case 0  -> Enchantment.Rarity.COMMON;
	        case 1  -> Enchantment.Rarity.UNCOMMON;
	        case 2  -> Enchantment.Rarity.RARE;
	        default -> Enchantment.Rarity.VERY_RARE;
	    };
	}
	
	private static List<String> getDefaultRarities() {
		Map<String, Integer> defaults = new HashMap<>();
		defaults.put("minecraft:protection", 1); // UNCOMMON
		defaults.put("minecraft:fire_protection", 0); // COMMON
		defaults.put("minecraft:feather_falling", 0); // COMMON
		defaults.put("minecraft:blast_protection", 1); // UNCOMMON
		defaults.put("minecraft:projectile_protection", 0); // COMMON, may be stupid, we'll see
		defaults.put("minecraft:respiration", 1); // UNCOMMON (why was it even rare)
		defaults.put("minecraft:aqua_affinity", 2); // RARE (Kinda useful)
		defaults.put("minecraft:thorns", 3); // VERY_RARE (untouched)
		defaults.put("minecraft:depth_strider", 2); // RARE (untouched)
		defaults.put("minecraft:frost_walker", 3); // VERY_RARE
		defaults.put("minecraft:soul_speed", 3); // VERY_RARE
		defaults.put("minecraft:sharpness", 1); // UNCOMMON
		defaults.put("minecraft:smite", 0); // COMMON
		defaults.put("minecraft:bane_of_arthropods", 0); // COMMON
		defaults.put("minecraft:knockback", 0); // COMMON
		defaults.put("minecraft:fire_aspect", 3); // VERY_RARE
		defaults.put("minecraft:looting", 2); // RARE
		defaults.put("minecraft:sweeping", 2); // RARE
		defaults.put("minecraft:efficiency", 1); // UNCOMMON
		defaults.put("minecraft:silk_touch", 3); // VERY_RARE (untouched)
		defaults.put("minecraft:unbreaking", 2); // RARE
		defaults.put("minecraft:fortune", 3); // VERY_RARE 
		defaults.put("minecraft:power", 0); // COMMON (untouched)
		defaults.put("minecraft:punch", 2); // RARE (untouched)
		defaults.put("minecraft:flame", 2); // RARE (untouched)
		defaults.put("minecraft:infinity", 3); // VERY_RARE (untouched)
		defaults.put("minecraft:luck_of_the_sea", 1); // UNCOMMON (Useless, not enough enchants on fishing rod)
		defaults.put("minecraft:lure", 1); // UNCOMMON (Useless, not enough enchants on fishing rod)
		defaults.put("minecraft:loyalty", 1); // UNCOMMON (untouched)
		defaults.put("minecraft:impaling", 2); // RARE (untouched)
		defaults.put("minecraft:riptide", 2); // RARE (untouched)
		defaults.put("minecraft:channeling", 3); // VERY_RARE (untouched)
		defaults.put("minecraft:multishot", 2); // RARE (untouched)
		defaults.put("minecraft:quick_charge", 1); // UNCOMMON (untouched)
		defaults.put("minecraft:piercing", 0); // COMMON (untouched)
		return mapToList(defaults);
	}
	
	private static List<String> getDefaultMaxCosts() {
		Map<String, Integer> defaults = new HashMap<>();
		defaults.put("minecraft:protection", 999); // Always Available
		defaults.put("minecraft:fire_protection", 999); // i wont write it every time but you got the idea
		defaults.put("minecraft:feather_falling", 999);
		defaults.put("minecraft:blast_protection", 999); 
		defaults.put("minecraft:projectile_protection", 999); 
		defaults.put("minecraft:respiration", 999);
		defaults.put("minecraft:aqua_affinity", 999); 
		defaults.put("minecraft:thorns", 999); 
		defaults.put("minecraft:depth_strider", 999); 
		defaults.put("minecraft:frost_walker", 999); 
		defaults.put("minecraft:soul_speed", 999);
		defaults.put("minecraft:sharpness", 999); 
		defaults.put("minecraft:smite", 999); 
		defaults.put("minecraft:bane_of_arthropods", 999); 
		defaults.put("minecraft:knockback", 999); 
		defaults.put("minecraft:fire_aspect", 999);
		defaults.put("minecraft:looting", 999); 
		defaults.put("minecraft:sweeping", 999); 
		defaults.put("minecraft:efficiency", 999); 
		defaults.put("minecraft:silk_touch", 999); 
		defaults.put("minecraft:unbreaking", 999);
		defaults.put("minecraft:fortune", 999);
		defaults.put("minecraft:power", 999); 
		defaults.put("minecraft:punch", 999); 
		defaults.put("minecraft:flame", 999); 
		defaults.put("minecraft:infinity", 999);
		defaults.put("minecraft:luck_of_the_sea", 999);
		defaults.put("minecraft:lure", 999); 
		defaults.put("minecraft:loyalty", 999); 
		defaults.put("minecraft:impaling", 999); 
		defaults.put("minecraft:riptide", 999);
		defaults.put("minecraft:channeling", 999); 
		defaults.put("minecraft:multishot", 999);
		defaults.put("minecraft:quick_charge", 999); 
		defaults.put("minecraft:piercing", 999);
		return mapToList(defaults);
	}
	
	private static List<String> getDefaultMaxLevels() {
		Map<String, Integer> defaults = new HashMap<>();
		defaults.put("minecraft:respiration", 5); // +2
		defaults.put("minecraft:depth_strider", 5); // +2
		defaults.put("minecraft:luck_of_the_sea", 5); // +2
		defaults.put("minecraft:lure", 5); // +2
		defaults.put("minecraft:quick_charge", 5); // +2
		defaults.put("minecraft:piercing", 5); // +1
		return mapToList(defaults);
	}
	
	private static List<String> mapToList(Map<String, Integer> map) {
	    return map.entrySet().stream()
	            .map(e -> e.getKey() + "=" + e.getValue())
	            .toList();
	}
	
	public static Map<String, Integer> getMap(List<? extends String> targetValue) {
	    Map<String, Integer> map = new HashMap<>();

	    for (String entry : targetValue) {
	        String[] parts = entry.split("=");

	        if (parts.length == 2) {
	            try {
	                String key = parts[0].trim();
	                int value = Integer.parseInt(parts[1].trim());
	                map.put(key, value);
	            } catch (NumberFormatException ignored) {
	            }
	        }
	    }

	    return map;
	}

}
