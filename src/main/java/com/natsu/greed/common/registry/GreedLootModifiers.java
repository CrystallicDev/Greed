package com.natsu.greed.common.registry;

import com.mojang.serialization.MapCodec;
import com.natsu.greed.Greed;
import com.natsu.greed.server.lootmodifier.ScarcityLootModifier;
import com.natsu.greed.server.lootmodifier.VoidingLootModifier;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GreedLootModifiers {

	// 1.21/NeoForge : le registre des sérialiseurs contient des MapCodec, plus des Codec.
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> SERIALIZERS =
			DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Greed.MODID);

	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<VoidingLootModifier>> VOIDING_MODIFIER =
			SERIALIZERS.register("voiding", VoidingLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ScarcityLootModifier>> SCARCITY_MODIFIER =
			SERIALIZERS.register("scarcity", ScarcityLootModifier.CODEC);

}
