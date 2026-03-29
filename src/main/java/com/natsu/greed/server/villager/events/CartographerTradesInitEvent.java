package com.natsu.greed.server.villager.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedTags;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.VillagerTradeHandler.EmeraldForItems;
import com.natsu.greed.server.villager.VillagerTradeHandler.ItemsForEmeralds;
import com.natsu.greed.server.villager.VillagerTradeHandler.TreasureMapForEmeralds;
import com.natsu.greed.server.villager.events.GreedFillingTradesEvent.ProfessionLevel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CartographerTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(GreedFillingTradesEvent event) {
		if (event.getProfession() != VillagerProfession.CARTOGRAPHER) return;
		event.clearTradeOf(ProfessionLevel.NOVICE);
		//event.clearTradeOf(ProfessionLevel.APPRENTICE);
		//event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		//event.clearTradeOf(ProfessionLevel.EXPERT);
		//event.clearTradeOf(ProfessionLevel.MASTER);
		
		event.addTradeTo(ProfessionLevel.NOVICE, new ItemsForEmeralds(Items.MAP, 7, 1, 1));
		event.addTradeTo(ProfessionLevel.NOVICE, new StructureMapListing(5, GreedTags.CARTOGRAPHER_STRUCTURE_SIMPLE_DUNGEONS, "map.greed.simple_dungeon", MapDecoration.Type.TARGET_X, 1, 15));
		event.addTradeTo(ProfessionLevel.NOVICE, new BiomeMapListing(5, BiomeTags.HAS_JUNGLE_TEMPLE, "map.greed.cold_forest", MapDecoration.Type.TARGET_X, 1, 15));
		
		/*event.addTradeTo(ProfessionLevel.APPRENTICE,
				new VillagerTrades.ItemListing[] {
						new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 15),
						new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 15)
				});*/
		/*VillagerTrades.TRADES.get(VillagerProfession.CARTOGRAPHER).put(3,
				new VillagerTrades.ItemListing[] {
					new EmeraldForItems(Items.LAPIS_LAZULI, 5, 12, 10),
					new EnchantBookForEmeralds(5)
				});
		VillagerTrades.TRADES.get(VillagerProfession.CARTOGRAPHER).put(4,
				new VillagerTrades.ItemListing[] {
					new MultiEnchantBookForEmeralds(10, 1, 3),
					new EmeraldForItems(Items.WRITABLE_BOOK, 2, 12, 30),
					new ItemsForEmeralds(Items.CLOCK, 5, 1, 15)
				});
		VillagerTrades.TRADES.get(VillagerProfession.CARTOGRAPHER).put(5,
				new VillagerTrades.ItemListing[] {
					new MultiEnchantBookForEmeralds(15, 2, 5)
				});*/
		
		
		
		/*p_35633_.put(VillagerProfession.CARTOGRAPHER, toIntMap(ImmutableMap.of(1,
				new VillagerTrades.ItemListing[] { new VillagerTrades.EmeraldForItems(Items.PAPER, 24, 16, 2),
						new VillagerTrades.ItemsForEmeralds(Items.MAP, 7, 1, 1) },
				2,
				new VillagerTrades.ItemListing[] { new VillagerTrades.EmeraldForItems(Items.GLASS_PANE, 11, 16, 10),
						new VillagerTrades.TreasureMapForEmeralds(13, ConfiguredStructureTags.ON_OCEAN_EXPLORER_MAPS,
								"filled_map.monument", MapDecoration.Type.MONUMENT, 12, 5) },
				3,
				new VillagerTrades.ItemListing[] { new VillagerTrades.EmeraldForItems(Items.COMPASS, 1, 12, 20),
						new VillagerTrades.TreasureMapForEmeralds(14, ConfiguredStructureTags.ON_WOODLAND_EXPLORER_MAPS,
								"filled_map.mansion", MapDecoration.Type.MANSION, 12, 10) },
				4,
				new VillagerTrades.ItemListing[] { new VillagerTrades.ItemsForEmeralds(Items.ITEM_FRAME, 7, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.RED_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 15),
						new VillagerTrades.ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 15) },
				5, new VillagerTrades.ItemListing[] {
						new VillagerTrades.ItemsForEmeralds(Items.GLOBE_BANNER_PATTERN, 8, 1, 30) })));*/
		
	}
	
	
	public static class BiomeMapListing implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<Biome> biomeTag;
		private final String displayName;
		private final MapDecoration.Type destinationType;
		private final int maxUses;
		private final int villagerXp;

		public BiomeMapListing(int emeraldCost, TagKey<Biome> biomeTag, String displayName,
				MapDecoration.Type destinationType, int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.biomeTag = biomeTag;
			this.displayName = displayName;
			this.destinationType = destinationType;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		public MerchantOffer getOffer(Entity entity, Random random) {
			if (!(entity.level instanceof ServerLevel serverLevel))
				return null;
			List<Holder<Biome>> biomesInTag = new ArrayList<>();
			serverLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getTagOrEmpty(biomeTag)
					.forEach(biomesInTag::add);

			if (biomesInTag.isEmpty())
				return null;
			Holder<Biome> randomBiome = biomesInTag.get(random.nextInt(biomesInTag.size()));
			Pair<BlockPos, Holder<Biome>> foundBiome = serverLevel.getChunkSource().getGenerator().getBiomeSource()
					.findBiomeHorizontal(entity.blockPosition().getX(), entity.blockPosition().getY(),
							entity.blockPosition().getZ(), 3200, 8,
							holder -> holder.is(randomBiome.unwrapKey().orElseThrow()), random, true,
							serverLevel.getChunkSource().getGenerator().climateSampler());
			BlockPos biomePos = foundBiome.getFirst();

			if (biomePos == null) {
				return new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.MAP), 5, villagerXp,
						0.2f);
			}

			ItemStack map = MapItem.create(serverLevel, biomePos.getX(), biomePos.getZ(), (byte) 2, true, true);
			MapItem.renderBiomePreviewMap(serverLevel, map);
			MapItemSavedData.addTargetDecoration(map, biomePos, "+", destinationType);
			map.setHoverName(new TranslatableComponent(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.COMPASS), map,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class StructureMapListing implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<ConfiguredStructureFeature<?, ?>> destination;
		private final String displayName;
		private final MapDecoration.Type destinationType;
		private final int maxUses;
		private final int villagerXp;

		public StructureMapListing(int emeraldCost, TagKey<ConfiguredStructureFeature<?, ?>> destination,
				String displayName, MapDecoration.Type destinationType, int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.destination = destination;
			this.displayName = displayName;
			this.destinationType = destinationType;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		@Override
		@Nullable
		public MerchantOffer getOffer(Entity entity, Random random) {
			if (!(entity.level instanceof ServerLevel serverLevel))
				return null;
			List<Holder<ConfiguredStructureFeature<?, ?>>> structuresInTag = new ArrayList<>();
			serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
					.getTagOrEmpty(destination).forEach(structuresInTag::add);

			if (structuresInTag.isEmpty())
				return null;
			Holder<ConfiguredStructureFeature<?, ?>> randomStructure = structuresInTag
					.get(random.nextInt(structuresInTag.size()));
			BlockPos structurePos = serverLevel.findNearestMapFeature(destination,
																					
					entity.blockPosition(), 100, true);
			if (structurePos == null) {
				return new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.MAP), 5, villagerXp,
						0.2f);
			}
			ItemStack map = MapItem.create(serverLevel, structurePos.getX(), structurePos.getZ(), (byte) 2, true, true);
			MapItem.renderBiomePreviewMap(serverLevel, map);
			MapItemSavedData.addTargetDecoration(map, structurePos, "+", destinationType);
			map.setHoverName(new TranslatableComponent(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.COMPASS), map,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<ConfiguredStructureFeature<?, ?>> destination;
		private final String displayName;
		private final MapDecoration.Type destinationType;
		private final int maxUses;
		private final int villagerXp;

		public TreasureMapForEmeralds(int p_207767_, TagKey<ConfiguredStructureFeature<?, ?>> p_207768_,
				String p_207769_, MapDecoration.Type p_207770_, int p_207771_, int p_207772_) {
			this.emeraldCost = p_207767_;
			this.destination = p_207768_;
			this.displayName = p_207769_;
			this.destinationType = p_207770_;
			this.maxUses = p_207771_;
			this.villagerXp = p_207772_;
		}

		@Nullable
		public MerchantOffer getOffer(Entity p_35817_, Random p_35818_) {
			if (!(p_35817_.level instanceof ServerLevel)) {
				return null;
			} else {
				ServerLevel serverlevel = (ServerLevel) p_35817_.level;
				BlockPos blockpos = serverlevel.findNearestMapFeature(this.destination, p_35817_.blockPosition(), 100,
						true);
				if (blockpos != null) {
					ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte) 2, true,
							true);
					MapItem.renderBiomePreviewMap(serverlevel, itemstack);
					MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
					itemstack.setHoverName(new TranslatableComponent(this.displayName));
					return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost),
							new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.villagerXp, 0.2F);
				} else {
					return null;
				}
			}
		}
	}
	
}
