package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natsu.greed.common.registry.GreedTags;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.villager.GreedDynamicTrades;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

/**
 * 26.1 : l'API programmatique de trades villageois a disparu (les trades sont data-driven). Pour les
 * trades qui nécessitent un accès runtime (recherche de biome/structure, registre complet d'enchants),
 * on ajoute nos MerchantOffer à la fin de updateTrades — l'équivalent moderne du VillagerTradesEvent.
 */
@Mixin(value = Villager.class, remap = false)
public abstract class VillagerMixin {

	@Unique
	private static void greed$add(MerchantOffers offers, MerchantOffer offer) {
		if (offer != null) offers.add(offer);
	}

	@Inject(method = "updateTrades", at = @At("TAIL"))
	private void greed$dynamicTrades(ServerLevel level, CallbackInfo ci) {
		Villager self = (Villager) (Object) this;
		VillagerData data = self.getVillagerData();
		Holder<VillagerProfession> prof = data.profession();
		int lvl = data.level();
		RandomSource rng = self.getRandom();
		MerchantOffers offers = self.getOffers();
		BlockPos pos = self.blockPosition();

		if (prof.is(VillagerProfession.CARTOGRAPHER) && ServerConfig.getOrDefault(ServerConfig.USE_CUSTOM_MAP_TRADES)) {
			var deco = MapDecorationTypes.TARGET_X;
			switch (lvl) {
				case 1 -> {
					greed$add(offers, GreedDynamicTrades.biomeMap(level, rng, 5, BiomeTags.HAS_MINESHAFT_MESA, "map.greed.mesa", deco, pos, 1, 8));
					greed$add(offers, GreedDynamicTrades.biomeMap(level, rng, 5, BiomeTags.IS_JUNGLE, "map.greed.jungle", deco, pos, 1, 8));
					greed$add(offers, GreedDynamicTrades.biomeMap(level, rng, 5, BiomeTags.IS_TAIGA, "map.greed.taiga", deco, pos, 1, 8));
				}
				case 3 -> greed$add(offers, GreedDynamicTrades.dimStructMap(level.getServer(), Level.NETHER, 15, GreedTags.ON_FORTRESS_EXPLORER_MAPS, "map.greed.nether_fortress", deco, pos, 1, 15));
				case 4 -> greed$add(offers, GreedDynamicTrades.dimStructMap(level.getServer(), Level.NETHER, 15, GreedTags.ON_BASTION_EXPLORER_MAPS, "map.greed.bastion", deco, pos, 1, 15));
				case 5 -> greed$add(offers, GreedDynamicTrades.dimStructMap(level.getServer(), Level.END, 15, GreedTags.ON_END_CITY_EXPLORER_MAPS, "map.greed.end_city", deco, pos, 1, 15));
				default -> { }
			}
		} else if (prof.is(VillagerProfession.LIBRARIAN) && ServerConfig.getOrDefault(ServerConfig.USE_CUSTOM_BOOK_TRADES)) {
			if (lvl == 4) greed$add(offers, GreedDynamicTrades.multiBook(level, rng, 1, 3, 10));
			else if (lvl == 5) greed$add(offers, GreedDynamicTrades.multiBook(level, rng, 2, 5, 15));
		}
	}
}
