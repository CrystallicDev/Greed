package com.natsu.greed.server.enchants;

/**
 * Rareté d'enchant personnalisée (useCustomEnchantmentRarity).
 *
 * <p>1.21 : la "rareté" 1.18 est devenue le champ {@code weight} de l'{@code EnchantmentDefinition},
 * un record immuable — on ne peut plus la muter par réflexion au démarrage. Un pilotage runtime par
 * config n'est plus possible ; il faudrait passer par un datapack qui réécrit les JSON d'enchants.
 * Feature reportée : {@link #init()} est un no-op pour l'instant.
 */
public class GreedEnchantModifiers {

	public static void init() {
		// no-op (voir javadoc) — TODO 1.21 : override du poids via datapack.
	}

}
