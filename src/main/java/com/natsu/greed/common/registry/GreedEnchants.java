package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Greed's enchants live as entries in the datapack {@link Registries#ENCHANTMENT} registry,
 * defined as JSON in data/greed/enchantment/. here we only keep the keys and resolve the
 * Holder on demand through the RegistryAccess (see {@link #get}), since Enchantment can't be
 * subclassed anymore.
 */
public class GreedEnchants {

	public static final ResourceKey<Enchantment> CURSE_OF_THE_SPONGE = key("curse_of_the_sponge");
	public static final ResourceKey<Enchantment> CURSE_OF_ABSORPTION = key("curse_of_absorption");
	public static final ResourceKey<Enchantment> CURSE_OF_VOIDING = key("curse_of_voiding");
	public static final ResourceKey<Enchantment> CURSE_OF_CREEPING = key("curse_of_creeping");
	public static final ResourceKey<Enchantment> CURSE_OF_COMBINATION = key("curse_of_combination");
	public static final ResourceKey<Enchantment> CURSE_OF_SCARCITY = key("curse_of_scarcity");
	public static final ResourceKey<Enchantment> CURSE_OF_HEAVYWEIGHT = key("curse_of_heavyweight");
	public static final ResourceKey<Enchantment> LIGHT = key("light");
	public static final ResourceKey<Enchantment> REELING = key("reeling");
	public static final ResourceKey<Enchantment> GRAPPLING = key("grappling");
	public static final ResourceKey<Enchantment> STRETCHED = key("stretched");

	private static ResourceKey<Enchantment> key(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Greed.MODID, name));
	}

	/** resolves the enchant's Holder, or null if the datapack doesn't provide it yet. */
	public static Holder<Enchantment> get(RegistryAccess access, ResourceKey<Enchantment> key) {
		return access.lookupOrThrow(Registries.ENCHANTMENT).get(key).orElse(null);
	}

}
