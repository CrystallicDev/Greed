package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.21 : Enchantment est un record final, on ne peut plus l'étendre. Les enchants de Greed sont
 * désormais des entrées du registre datapack {@link Registries#ENCHANTMENT}, définies en JSON dans
 * data/greed/enchantment/. Ici on ne garde que les clés ; on résout le Holder à l'usage via le
 * RegistryAccess (voir {@link #get}).
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
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Greed.MODID, name));
	}

	/** Résout le Holder de l'enchant, ou null si le datapack ne le fournit pas encore (Bloc B). */
	public static Holder<Enchantment> get(RegistryAccess access, ResourceKey<Enchantment> key) {
		return access.lookupOrThrow(Registries.ENCHANTMENT).get(key).orElse(null);
	}

}
