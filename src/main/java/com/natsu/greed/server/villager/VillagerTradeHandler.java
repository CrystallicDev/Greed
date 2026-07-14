package com.natsu.greed.server.villager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.villager.events.GreedFillingTradesEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradeHandler {


	
	
	/**
	 * This is garbage, like literally, but i could not be bothered to make reflections etc to reach
	 * code THAT FUCKING EXIST but is private for some reason
	 * */
	
	public static class DyedArmorForEmeralds implements VillagerTrades.ItemListing {
		private final Item item;
		private final int value;
		private final int maxUses;
		private final int villagerXp;

		public DyedArmorForEmeralds(Item p_35639_, int p_35640_) {
			this(p_35639_, p_35640_, 12, 1);
		}

		public DyedArmorForEmeralds(Item p_35642_, int p_35643_, int p_35644_, int p_35645_) {
			this.item = p_35642_;
			this.value = p_35643_;
			this.maxUses = p_35644_;
			this.villagerXp = p_35645_;
		}

		public MerchantOffer getOffer(Entity p_35647_, net.minecraft.util.RandomSource p_35648_) {
			ItemStack itemstack = new ItemStack(Items.EMERALD, this.value);
			ItemStack itemstack1 = new ItemStack(this.item);
			if (this.item instanceof DyeableArmorItem) {
				List<DyeItem> list = Lists.newArrayList();
				list.add(getRandomDye(p_35648_));
				if (p_35648_.nextFloat() > 0.7F) {
					list.add(getRandomDye(p_35648_));
				}

				if (p_35648_.nextFloat() > 0.8F) {
					list.add(getRandomDye(p_35648_));
				}

				itemstack1 = DyeableLeatherItem.dyeArmor(itemstack1, list);
			}

			return new MerchantOffer(itemstack, itemstack1, this.maxUses, this.villagerXp, 0.2F);
		}

		private static DyeItem getRandomDye(net.minecraft.util.RandomSource p_35650_) {
			return DyeItem.byColor(DyeColor.byId(p_35650_.nextInt(16)));
		}
	}

	public static class EmeraldForItems implements VillagerTrades.ItemListing {
		private final Item item;
		private final int cost;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EmeraldForItems(ItemLike p_35657_, int p_35658_, int p_35659_, int p_35660_) {
			this.item = p_35657_.asItem();
			this.cost = p_35658_;
			this.maxUses = p_35659_;
			this.villagerXp = p_35660_;
			this.priceMultiplier = 0.05F;
		}

		public MerchantOffer getOffer(Entity p_35662_, net.minecraft.util.RandomSource p_35663_) {
			ItemStack itemstack = new ItemStack(this.item, this.cost);
			return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp,
					this.priceMultiplier);
		}
	}

	public static class EmeraldsForVillagerTypeItem implements VillagerTrades.ItemListing {
		private final Map<VillagerType, Item> trades;
		private final int cost;
		private final int maxUses;
		private final int villagerXp;

		public EmeraldsForVillagerTypeItem(int p_35669_, int p_35670_, int p_35671_, Map<VillagerType, Item> p_35672_) {
			BuiltInRegistries.VILLAGER_TYPE.stream().filter((p_35680_) -> {
				return !p_35672_.containsKey(p_35680_);
			}).findAny().ifPresent((p_35677_) -> {
				throw new IllegalStateException(
						"Missing trade for villager type: " + BuiltInRegistries.VILLAGER_TYPE.getKey(p_35677_));
			});
			this.trades = p_35672_;
			this.cost = p_35669_;
			this.maxUses = p_35670_;
			this.villagerXp = p_35671_;
		}

		@Nullable
		public MerchantOffer getOffer(Entity p_35674_, net.minecraft.util.RandomSource p_35675_) {
			if (p_35674_ instanceof VillagerDataHolder) {
				ItemStack itemstack = new ItemStack(
						this.trades.get(((VillagerDataHolder) p_35674_).getVillagerData().getType()), this.cost);
				return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05F);
			} else {
				return null;
			}
		}
	}

	

	

	public static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
		private final ItemStack fromItem;
		private final int fromCount;
		private final int emeraldCost;
		private final ItemStack toItem;
		private final int toCount;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsAndEmeraldsToItems(ItemLike p_35725_, int p_35726_, Item p_35727_, int p_35728_, int p_35729_,
				int p_35730_) {
			this(p_35725_, p_35726_, 1, p_35727_, p_35728_, p_35729_, p_35730_);
		}

		public ItemsAndEmeraldsToItems(ItemLike p_35717_, int p_35718_, int p_35719_, Item p_35720_, int p_35721_,
				int p_35722_, int p_35723_) {
			this.fromItem = new ItemStack(p_35717_);
			this.fromCount = p_35718_;
			this.emeraldCost = p_35719_;
			this.toItem = new ItemStack(p_35720_);
			this.toCount = p_35721_;
			this.maxUses = p_35722_;
			this.villagerXp = p_35723_;
			this.priceMultiplier = 0.05F;
		}

		@Nullable
		public MerchantOffer getOffer(Entity p_35732_, net.minecraft.util.RandomSource p_35733_) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost),
					new ItemStack(this.fromItem.getItem(), this.fromCount),
					new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp,
					this.priceMultiplier);
		}
	}

	public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int emeraldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsForEmeralds(Block item, int emeraldCost, int numberOfItems, int maxTrades, int p_35769_) {
			this(new ItemStack(item), emeraldCost, numberOfItems, maxTrades, p_35769_);
		}

		public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int p_35744_) {
			this(new ItemStack(item), emeraldCost, numberOfItems, 12, p_35744_);
		}

		public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxTrades, int p_35750_) {
			this(new ItemStack(item), emeraldCost, numberOfItems, maxTrades, p_35750_);
		}

		public ItemsForEmeralds(ItemStack item, int emeraldCost, int numberOfItems, int maxTrades, int p_35756_) {
			this(item, emeraldCost, numberOfItems, maxTrades, p_35756_, 0.05F);
		}

		public ItemsForEmeralds(ItemStack item, int emeraldCost, int numberOfItems, int maxTrades, int villagerXp,
				float p_35763_) {
			this.itemStack = item;
			this.emeraldCost = emeraldCost;
			this.numberOfItems = numberOfItems;
			this.maxUses = maxTrades;
			this.villagerXp = villagerXp;
			this.priceMultiplier = p_35763_;
		}

		public MerchantOffer getOffer(Entity p_35771_, net.minecraft.util.RandomSource p_35772_) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost),
					new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp,
					this.priceMultiplier);
		}
	}

	public static class SuspiciousStewForEmerald implements VillagerTrades.ItemListing {
		final MobEffect effect;
		final int duration;
		final int xp;
		private final float priceMultiplier;

		public SuspiciousStewForEmerald(MobEffect p_186313_, int p_186314_, int p_186315_) {
			this.effect = p_186313_;
			this.duration = p_186314_;
			this.xp = p_186315_;
			this.priceMultiplier = 0.05F;
		}

		@Nullable
		public MerchantOffer getOffer(Entity p_186317_, net.minecraft.util.RandomSource p_186318_) {
			ItemStack itemstack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
			SuspiciousStewItem.saveMobEffect(itemstack, this.effect, this.duration);
			return new MerchantOffer(new ItemStack(Items.EMERALD, 1), itemstack, 12, this.xp, this.priceMultiplier);
		}
	}

	public static class TippedArrowForItemsAndEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack toItem;
		private final int toCount;
		private final int emeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final Item fromItem;
		private final int fromCount;
		private final float priceMultiplier;

		public TippedArrowForItemsAndEmeralds(Item p_35793_, int p_35794_, Item p_35795_, int p_35796_, int p_35797_,
				int p_35798_, int p_35799_) {
			this.toItem = new ItemStack(p_35795_);
			this.emeraldCost = p_35797_;
			this.maxUses = p_35798_;
			this.villagerXp = p_35799_;
			this.fromItem = p_35793_;
			this.fromCount = p_35794_;
			this.toCount = p_35796_;
			this.priceMultiplier = 0.05F;
		}

		public MerchantOffer getOffer(Entity p_35801_, net.minecraft.util.RandomSource p_35802_) {
			ItemStack itemstack = new ItemStack(Items.EMERALD, this.emeraldCost);
			List<Potion> list = BuiltInRegistries.POTION.stream().filter((p_35804_) -> {
				return !p_35804_.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(p_35804_);
			}).collect(Collectors.toList());
			Potion potion = list.get(p_35802_.nextInt(list.size()));
			ItemStack itemstack1 = PotionUtils.setPotion(new ItemStack(this.toItem.getItem(), this.toCount), potion);
			return new MerchantOffer(itemstack, new ItemStack(this.fromItem, this.fromCount), itemstack1, this.maxUses,
					this.villagerXp, this.priceMultiplier);
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
