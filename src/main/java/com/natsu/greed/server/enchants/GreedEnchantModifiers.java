package com.natsu.greed.server.enchants;

/**
 * Custom enchant rarity (useCustomEnchantmentRarity).
 *
 * <p>rarity is now the immutable {@code weight} field of the {@code EnchantmentDefinition},
 * so we can't mutate it by reflection at startup. driving it from config isn't possible;
 * you'd need a datapack that rewrites the enchant JSONs.
 * feature is deferred, {@link #init()} is a no-op for now.
 */
public class GreedEnchantModifiers {

	public static void init() {
		// no-op (see javadoc) - TODO: override the weight via a datapack.
	}

}
