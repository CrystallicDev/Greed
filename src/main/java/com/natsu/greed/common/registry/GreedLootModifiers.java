package com.natsu.greed.common.registry;

import com.mojang.serialization.Codec;
import com.natsu.greed.Greed;
import com.natsu.greed.server.lootmodifier.ScarcityLootModifier;
import com.natsu.greed.server.lootmodifier.VoidingLootModifier;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GreedLootModifiers {

	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Greed.MODID);

	public static final RegistryObject<Codec<VoidingLootModifier>> VOIDING_MODIFIER =
			SERIALIZERS.register("voiding", VoidingLootModifier.CODEC);
	public static final RegistryObject<Codec<ScarcityLootModifier>> SCARCITY_MODIFIER =
			SERIALIZERS.register("scarcity", ScarcityLootModifier.CODEC);

}
