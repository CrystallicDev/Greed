package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class GreedTags {

	public static final TagKey<ConfiguredStructureFeature<?, ?>> ON_BASTION_EXPLORER_MAPS = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, 
			new ResourceLocation(Greed.MODID, "bastion_explorer_maps"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>> ON_FORTRESS_EXPLORER_MAPS = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, 
			new ResourceLocation(Greed.MODID, "fortress_explorer_maps"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>> ON_END_CITY_EXPLORER_MAPS = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, 
			new ResourceLocation(Greed.MODID, "end_city_explorer_maps"));
	
	/*public static final TagKey<ConfiguredStructureFeature<?, ?>> CARTOGRAPHER_STRUCTURE_LEVEL_2 = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, 
			new ResourceLocation(Greed.MODID, "cartographer_structure_level_2"));
	public static final TagKey<Biome> CARTOGRAPHER_BIOMES_LEVEL_2 = TagKey.create(Registry.BIOME_REGISTRY, 
			new ResourceLocation(Greed.MODID, "cartographer_biome_level_2"));
	
	public static final TagKey<ConfiguredStructureFeature<?, ?>> CARTOGRAPHER_STRUCTURE_LEVEL_3 = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, 
			new ResourceLocation(Greed.MODID, "cartographer_structure_level_3"));
	public static final TagKey<Biome> CARTOGRAPHER_BIOMES_LEVEL_3 = TagKey.create(Registry.BIOME_REGISTRY, 
			new ResourceLocation(Greed.MODID, "cartographer_biome_level_3"));*/
}
