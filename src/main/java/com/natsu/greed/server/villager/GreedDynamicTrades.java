package com.natsu.greed.server.villager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/**
 * dynamic villager trades (biome maps, cross-dimension maps, multi-enchant books) that can't be
 * expressed in a datapack: they need a world search or the full enchant registry (so modded
 * enchants too). injected at runtime through VillagerMixin (updateTrades TAIL).
 */
public final class GreedDynamicTrades {

	private GreedDynamicTrades() {}

	private static final ItemCost COMPASS = new ItemCost(Items.COMPASS);

	private static MerchantOffer emptyMapFallback(int xp) {
		return new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(Items.MAP), 5, xp, 0.2f);
	}

	private static MerchantOffer mapOffer(ServerLevel level, BlockPos pos, String nameKey,
			Holder<MapDecorationType> deco, int cost, int maxUses, int xp) {
		ItemStack map = MapItem.create(level, pos.getX(), pos.getZ(), (byte) 2, true, true);
		MapItem.renderBiomePreviewMap(level, map);
		MapItemSavedData.addTargetDecoration(map, pos, "+", deco);
		map.set(DataComponents.ITEM_NAME, Component.translatable(nameKey));
		return new MerchantOffer(new ItemCost(Items.EMERALD, cost), Optional.of(COMPASS), map, maxUses, xp, 0.2f);
	}

	/** map to a random biome from the tag (searched in the villager's dimension). */
	public static MerchantOffer biomeMap(ServerLevel level, RandomSource rng, int cost, TagKey<Biome> biomeTag,
			String nameKey, Holder<MapDecorationType> deco, BlockPos origin, int maxUses, int xp) {
		List<Holder<Biome>> biomes = new ArrayList<>();
		level.registryAccess().lookupOrThrow(Registries.BIOME).getTagOrEmpty(biomeTag).forEach(biomes::add);
		if (biomes.isEmpty()) return null;
		ResourceKey<Biome> target = biomes.get(rng.nextInt(biomes.size())).unwrapKey().orElse(null);
		if (target == null) return null;
		// expensive 3D search: coarse steps (32/64) over a 2400 radius to avoid freezing the server.
		Pair<BlockPos, Holder<Biome>> found = level.findClosestBiome3d(h -> h.is(target), origin, 2400, 32, 64);
		if (found == null || found.getFirst() == null) return emptyMapFallback(xp);
		return mapOffer(level, found.getFirst(), nameKey, deco, cost, maxUses, xp);
	}

	/** map to a structure in ANOTHER dimension (Nether/End). */
	public static MerchantOffer dimStructMap(MinecraftServer server, ResourceKey<Level> dimension, int cost,
			TagKey<Structure> dest, String nameKey, Holder<MapDecorationType> deco, BlockPos origin, int maxUses, int xp) {
		if (server == null) return null;
		ServerLevel level = server.getLevel(dimension);
		if (level == null) return null;
		BlockPos pos = level.findNearestMapStructure(dest, origin, 50, true);
		if (pos == null) return emptyMapFallback(xp);
		return mapOffer(level, pos, nameKey, deco, cost, maxUses, xp);
	}

	/** book with 1..N enchants pulled from the full registry (tradeable tag, modded enchants included). */
	public static MerchantOffer multiBook(ServerLevel level, RandomSource rng, int minEnch, int maxEnch, int xp) {
		List<Holder<Enchantment>> pool = new ArrayList<>();
		level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getTagOrEmpty(EnchantmentTags.TRADEABLE).forEach(pool::add);
		if (pool.isEmpty()) return null;
		Collections.shuffle(pool);
		int amount = Mth.nextInt(rng, minEnch, maxEnch);
		int emeraldCost = 0;
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		for (int i = 0; i < Math.min(amount, pool.size()); i++) {
			Holder<Enchantment> ench = pool.get(i);
			int lvl = Mth.nextInt(rng, ench.value().getMinLevel(), ench.value().getMaxLevel());
			if (ench.is(EnchantmentTags.CURSE)) {
				emeraldCost += -2 + rng.nextInt(1 + lvl * 5) + lvl;
			} else {
				emeraldCost += 2 + rng.nextInt(3 + lvl * (ench.is(EnchantmentTags.TREASURE) ? 7 : 5)) + 3 * lvl;
			}
			emeraldCost = Math.max(emeraldCost, 1);
			book.enchant(ench, lvl);
		}
		if (emeraldCost > 64) emeraldCost = 64;
		return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), Optional.of(new ItemCost(Items.BOOK)),
				book, 12, xp, 0.2f);
	}
}
