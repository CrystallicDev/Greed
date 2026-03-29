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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ConfiguredStructureTags;
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
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradeHandler {

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent e) {
    	if (!ServerConfig.USE_CUSTOM_BOOK_TRADES.get()) return;
		
		VillagerProfession[] profList = {VillagerProfession.ARMORER, VillagerProfession.BUTCHER, VillagerProfession.CARTOGRAPHER, VillagerProfession.CLERIC, VillagerProfession.FARMER,
				VillagerProfession.FISHERMAN, VillagerProfession.FLETCHER, VillagerProfession.LEATHERWORKER, VillagerProfession.LIBRARIAN, VillagerProfession.MASON, VillagerProfession.NITWIT,
				VillagerProfession.SHEPHERD, VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH};
		
		for (VillagerProfession profession : profList) {
			HashMap<Integer, List<ItemListing>> TradeMap = toMap(VillagerTrades.TRADES.get(profession));
			GreedFillingTradesEvent event = new GreedFillingTradesEvent(profession, TradeMap);
			MinecraftForge.EVENT_BUS.post(event);
			HashMap<Integer, List<ItemListing>> newTrades = event.getTrades();
			for (int i = 1; i <= 5; i++) {
				if (newTrades.get(i) != null) {
					VillagerTrades.TRADES.get(profession).put(i, newTrades.get(i).toArray(new VillagerTrades.ItemListing[0]));
				}
			}
		}
		
		 /*
		p_35633_.put(VillagerProfession.CLERIC, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.ROTTEN_FLESH, 32, 16, 2), new VillagerTrades.ItemsForEmeralds(Items.REDSTONE, 1, 2, 1)}, 2, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.GOLD_INGOT, 3, 12, 10), new VillagerTrades.ItemsForEmeralds(Items.LAPIS_LAZULI, 1, 1, 5)}, 3, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.RABBIT_FOOT, 2, 12, 20), new VillagerTrades.ItemsForEmeralds(Blocks.GLOWSTONE, 4, 1, 12, 10)}, 4, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.SCUTE, 4, 12, 30), new VillagerTrades.EmeraldForItems(Items.GLASS_BOTTLE, 9, 12, 30), new VillagerTrades.ItemsForEmeralds(Items.ENDER_PEARL, 5, 1, 15)}, 5, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.NETHER_WART, 22, 12, 30), new VillagerTrades.ItemsForEmeralds(Items.EXPERIENCE_BOTTLE, 3, 1, 30)})));
	      p_35633_.put(VillagerProfession.ARMORER, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 4, 12, 10), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.LAVA_BUCKET, 1, 12, 20), new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 12, 20), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2F)}, 4, new VillagerTrades.ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2F), new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2F)}, 5, new VillagerTrades.ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 8, 3, 30, 0.2F), new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2F)})));
	      p_35633_.put(VillagerProfession.WEAPONSMITH, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F), new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_SWORD, 2, 3, 1)}, 2, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 4, 12, 10), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.FLINT, 24, 12, 20)}, 4, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 12, 30), new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F)}, 5, new VillagerTrades.ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_SWORD, 8, 3, 30, 0.2F)})));
	      p_35633_.put(VillagerProfession.TOOLSMITH, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 4, 12, 10), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.FLINT, 30, 12, 20), new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_AXE, 1, 3, 10, 0.2F), new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_SHOVEL, 2, 3, 10, 0.2F), new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_PICKAXE, 3, 3, 10, 0.2F), new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)}, 4, new VillagerTrades.ItemListing[]{new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 12, 30), new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F), new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2F)}, 5, new VillagerTrades.ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2F)})));
	      
		*/

		
	}
	
	private static HashMap<Integer, List<VillagerTrades.ItemListing>> toMap(Int2ObjectMap<VillagerTrades.ItemListing[]> intMap){
	    HashMap<Integer, List<VillagerTrades.ItemListing>> map = new HashMap<>();
	    if (intMap != null) {
	        for (int i = 1; i <= 5; i++) {
	            if (intMap.get(i) != null) {
	                List<VillagerTrades.ItemListing> list = new ArrayList<>(Arrays.asList(intMap.get(i))); // <--
	                map.put(i, list);
	            }
	        }
	    }
	    return map;
	}

	
	
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

		public MerchantOffer getOffer(Entity p_35647_, Random p_35648_) {
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

		private static DyeItem getRandomDye(Random p_35650_) {
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

		public MerchantOffer getOffer(Entity p_35662_, Random p_35663_) {
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
			Registry.VILLAGER_TYPE.stream().filter((p_35680_) -> {
				return !p_35672_.containsKey(p_35680_);
			}).findAny().ifPresent((p_35677_) -> {
				throw new IllegalStateException(
						"Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(p_35677_));
			});
			this.trades = p_35672_;
			this.cost = p_35669_;
			this.maxUses = p_35670_;
			this.villagerXp = p_35671_;
		}

		@Nullable
		public MerchantOffer getOffer(Entity p_35674_, Random p_35675_) {
			if (p_35674_ instanceof VillagerDataHolder) {
				ItemStack itemstack = new ItemStack(
						this.trades.get(((VillagerDataHolder) p_35674_).getVillagerData().getType()), this.cost);
				return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05F);
			} else {
				return null;
			}
		}
	}

	

	public static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EnchantedItemForEmeralds(Item p_35693_, int p_35694_, int p_35695_, int p_35696_) {
			this(p_35693_, p_35694_, p_35695_, p_35696_, 0.05F);
		}

		public EnchantedItemForEmeralds(Item p_35698_, int p_35699_, int p_35700_, int p_35701_, float p_35702_) {
			this.itemStack = new ItemStack(p_35698_);
			this.baseEmeraldCost = p_35699_;
			this.maxUses = p_35700_;
			this.villagerXp = p_35701_;
			this.priceMultiplier = p_35702_;
		}

		public MerchantOffer getOffer(Entity p_35704_, Random p_35705_) {
			int i = 5 + p_35705_.nextInt(15);
			ItemStack itemstack = EnchantmentHelper.enchantItem(p_35705_, new ItemStack(this.itemStack.getItem()), i,
					false);
			int j = Math.min(this.baseEmeraldCost + i, 64);
			ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);
			return new MerchantOffer(itemstack1, itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
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
		public MerchantOffer getOffer(Entity p_35732_, Random p_35733_) {
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

		public MerchantOffer getOffer(Entity p_35771_, Random p_35772_) {
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
		public MerchantOffer getOffer(Entity p_186317_, Random p_186318_) {
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

		public MerchantOffer getOffer(Entity p_35801_, Random p_35802_) {
			ItemStack itemstack = new ItemStack(Items.EMERALD, this.emeraldCost);
			List<Potion> list = Registry.POTION.stream().filter((p_35804_) -> {
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
