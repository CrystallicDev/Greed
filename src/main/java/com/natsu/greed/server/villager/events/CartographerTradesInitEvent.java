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
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.villager.VillagerTradeHandler;
import com.natsu.greed.server.villager.VillagerTradeHandler.EmeraldForItems;
import com.natsu.greed.server.villager.VillagerTradeHandler.ItemsForEmeralds;
import com.natsu.greed.server.villager.VillagerTradeHandler.TreasureMapForEmeralds;
import com.natsu.greed.server.villager.events.GreedFillingTradesEvent.ProfessionLevel;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CartographerTradesInitEvent {

	@SubscribeEvent
	public static void onTradeSetup(VillagerTradesEvent vte) {
		GreedFillingTradesEvent event = new GreedFillingTradesEvent(vte);
		if (event.getProfession() != VillagerProfession.CARTOGRAPHER || !ServerConfig.USE_CUSTOM_MAP_TRADES.get()) return;
		
		event.clearTradeOf(ProfessionLevel.NOVICE);
		event.clearTradeOf(ProfessionLevel.APPRENTICE);
		event.clearTradeOf(ProfessionLevel.JOURNEYMAN);
		event.clearTradeOf(ProfessionLevel.EXPERT);
		event.clearTradeOf(ProfessionLevel.MASTER);

		event.addTradeTo(ProfessionLevel.NOVICE, new ItemsForEmeralds(Items.MAP, 7, 1, 1));
		event.addTradeTo(ProfessionLevel.NOVICE, new TreasureMapForEmeralds(13, StructureTags.MINESHAFT,
						"map.greed.mineshaft", MapDecoration.Type.TARGET_X, 12, 5));
		event.addTradeTo(ProfessionLevel.NOVICE, new VillagerTradeHandler.EmeraldForItems(Items.PAPER, 24, 16, 2));
		event.addTradeTo(ProfessionLevel.NOVICE, new BiomeMapListing(5, BiomeTags.HAS_MINESHAFT_MESA, "map.greed.mesa", MapDecoration.Type.TARGET_X, 1, 8));
		event.addTradeTo(ProfessionLevel.NOVICE, new BiomeMapListing(5, BiomeTags.IS_JUNGLE, "map.greed.jungle", MapDecoration.Type.TARGET_X, 1, 8));
		event.addTradeTo(ProfessionLevel.NOVICE, new BiomeMapListing(5, BiomeTags.IS_TAIGA, "map.greed.taiga", MapDecoration.Type.TARGET_X, 1, 8));
		
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.APPRENTICE, new ItemsForEmeralds(Items.GLOBE_BANNER_PATTERN, 3, 1, 15));

		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new TreasureMapForEmeralds(13, StructureTags.ON_OCEAN_EXPLORER_MAPS,
				"filled_map.monument", MapDecoration.Type.MONUMENT, 12, 5));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new TreasureMapForEmeralds(13, StructureTags.ON_WOODLAND_EXPLORER_MAPS,
				"filled_map.mansion", MapDecoration.Type.MANSION, 12, 5));
		
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new ItemsForEmeralds(Items.CREEPER_BANNER_PATTERN, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.JOURNEYMAN, new DimensionalStructureMapListing(15, Level.NETHER, GreedTags.ON_FORTRESS_EXPLORER_MAPS, "map.greed.nether_fortress", MapDecoration.Type.TARGET_X, 1, 15));

		event.addTradeTo(ProfessionLevel.EXPERT, new ItemsForEmeralds(Items.PIGLIN_BANNER_PATTERN, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.EXPERT, new DimensionalStructureMapListing(15, Level.NETHER, GreedTags.ON_BASTION_EXPLORER_MAPS, "map.greed.bastion", MapDecoration.Type.TARGET_X, 1, 15));

		event.addTradeTo(ProfessionLevel.MASTER, new ItemsForEmeralds(Items.MOJANG_BANNER_PATTERN, 3, 1, 15));
		event.addTradeTo(ProfessionLevel.MASTER, new DimensionalStructureMapListing(15, Level.END, GreedTags.ON_END_CITY_EXPLORER_MAPS, "map.greed.end_city", MapDecoration.Type.TARGET_X, 1, 15));

		
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

		public MerchantOffer getOffer(Entity entity, net.minecraft.util.RandomSource random) {
			if (!(entity.level() instanceof ServerLevel serverLevel))
				return null;
			List<Holder<Biome>> biomesInTag = new ArrayList<>();
			serverLevel.registryAccess().registryOrThrow(Registries.BIOME).getTagOrEmpty(biomeTag)
					.forEach(biomesInTag::add);

			if (biomesInTag.isEmpty())
				return null;
			Holder<Biome> randomBiome = biomesInTag.get(random.nextInt(biomesInTag.size()));
			Pair<BlockPos, Holder<Biome>> foundBiome = serverLevel.findClosestBiome3d(
					holder -> holder.is(randomBiome.unwrapKey().orElseThrow()),
					entity.blockPosition(), 3200, 8, 8);
			BlockPos biomePos = foundBiome != null ? foundBiome.getFirst() : null;

			if (biomePos == null) {
				return new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.MAP), 5, villagerXp,
						0.2f);
			}

			ItemStack map = MapItem.create(serverLevel, biomePos.getX(), biomePos.getZ(), (byte) 2, true, true);
			MapItem.renderBiomePreviewMap(serverLevel, map);
			MapItemSavedData.addTargetDecoration(map, biomePos, "+", destinationType);
			map.setHoverName(Component.translatable(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.COMPASS), map,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class DimensionalStructureMapListing implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<Structure> destination;
		private final String displayName;
		private final MapDecoration.Type destinationType;
		private final int maxUses;
		private final int villagerXp;
		private final ResourceKey<Level> dimension;

		public DimensionalStructureMapListing(int emeraldCost, ResourceKey<Level> dimension, TagKey<Structure> destination,
				String displayName, MapDecoration.Type destinationType, int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.dimension = dimension;
			this.destination = destination;
			this.displayName = displayName;
			this.destinationType = destinationType;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		@Override
		@Nullable
		public MerchantOffer getOffer(Entity entity, net.minecraft.util.RandomSource random) {
			if (entity.getServer() == null) return null;
			ServerLevel level = entity.getServer().getLevel(dimension);
			if (level == null) return null;
			List<Holder<Structure>> structuresInTag = new ArrayList<>();
			level.registryAccess().registryOrThrow(Registries.STRUCTURE)
					.getTagOrEmpty(destination).forEach(structuresInTag::add);

			if (structuresInTag.isEmpty())
				return null;
			Holder<Structure> randomStructure = structuresInTag
					.get(random.nextInt(structuresInTag.size()));
			BlockPos structurePos = level.findNearestMapStructure(destination,
																					
					entity.blockPosition(), 100, true);
			if (structurePos == null) {
				return new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.MAP), 5, villagerXp,
						0.2f);
			}
			ItemStack map = MapItem.create(level, structurePos.getX(), structurePos.getZ(), (byte) 2, true, true);
			MapItem.renderBiomePreviewMap(level, map);
			MapItemSavedData.addTargetDecoration(map, structurePos, "+", destinationType);
			map.setHoverName(Component.translatable(displayName));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.COMPASS), map,
					maxUses, villagerXp, 0.2f);
		}
	}
	
	public static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<Structure> destination;
		private final String displayName;
		private final MapDecoration.Type destinationType;
		private final int maxUses;
		private final int villagerXp;

		public TreasureMapForEmeralds(int p_207767_, TagKey<Structure> p_207768_,
				String p_207769_, MapDecoration.Type p_207770_, int p_207771_, int p_207772_) {
			this.emeraldCost = p_207767_;
			this.destination = p_207768_;
			this.displayName = p_207769_;
			this.destinationType = p_207770_;
			this.maxUses = p_207771_;
			this.villagerXp = p_207772_;
		}

		@Nullable
		public MerchantOffer getOffer(Entity p_35817_, net.minecraft.util.RandomSource p_35818_) {
			if (!(p_35817_.level() instanceof ServerLevel)) {
				return null;
			} else {
				ServerLevel serverlevel = (ServerLevel) p_35817_.level();
				BlockPos blockpos = serverlevel.findNearestMapStructure(this.destination, p_35817_.blockPosition(), 100,
						true);
				if (blockpos != null) {
					ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte) 2, true,
							true);
					MapItem.renderBiomePreviewMap(serverlevel, itemstack);
					MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
					itemstack.setHoverName(Component.translatable(this.displayName));
					return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost),
							new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.villagerXp, 0.2F);
				} else {
					return null;
				}
			}
		}
	}
	
}
