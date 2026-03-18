package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.server.lootmodifier.ScarcityLootModifier;
import com.natsu.greed.server.lootmodifier.VoidingLootModifier;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GreedLootModifiers {

	public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS.get(), Greed.MODID);
	
	public static final RegistryObject<VoidingLootModifier.Serializer> VOIDING_MODIFIER = SERIALIZERS.register("voiding", VoidingLootModifier.Serializer::new);
	public static final RegistryObject<ScarcityLootModifier.Serializer> SCARCITY_MODIFIER = SERIALIZERS.register("scarcity", ScarcityLootModifier.Serializer::new);
	
}
