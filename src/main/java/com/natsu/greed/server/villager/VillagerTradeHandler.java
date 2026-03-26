package com.natsu.greed.server.villager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.natsu.greed.config.ServerConfig;

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
import net.minecraftforge.registries.ForgeRegistries;

public class VillagerTradeHandler {

	public static void init() {
		if (!ServerConfig.USE_CUSTOM_BOOK_TRADES.get()) return;
		
		VillagerTrades.TRADES.get(VillagerProfession.LIBRARIAN).clear();
		
		VillagerTrades.TRADES.get(VillagerProfession.LIBRARIAN).put(1,
				new VillagerTrades.ItemListing[] {
						new ItemsForEmeralds(Items.NAME_TAG, 7, 1, 12, 2),
						new EmeraldForItems(Items.PAPER, 24, 16, 2),
						new ItemsForEmeralds(Blocks.BOOKSHELF, 9, 1, 12, 1)
				});
		VillagerTrades.TRADES.get(VillagerProfession.LIBRARIAN).put(2, 
				new VillagerTrades.ItemListing[] {
					new SimpleEnchantBookForEmeralds(1),
					new ItemsForEmeralds(Items.ENCHANTING_TABLE, 20, 1, 1, 7),
					new ItemsForEmeralds(Items.LANTERN, 1, 1, 5)
				});
		VillagerTrades.TRADES.get(VillagerProfession.LIBRARIAN).put(3,
				new VillagerTrades.ItemListing[] {
					new EmeraldForItems(Items.LAPIS_LAZULI, 5, 12, 10),
					new EnchantBookForEmeralds(5)
				});
		VillagerTrades.TRADES.get(VillagerProfession.LIBRARIAN).put(4,
				new VillagerTrades.ItemListing[] {
					new MultiEnchantBookForEmeralds(10, 1, 3),
					new EmeraldForItems(Items.WRITABLE_BOOK, 2, 12, 30),
					new ItemsForEmeralds(Items.CLOCK, 5, 1, 15)
				});
		VillagerTrades.TRADES.get(VillagerProfession.LIBRARIAN).put(5,
				new VillagerTrades.ItemListing[] {
					new MultiEnchantBookForEmeralds(15, 2, 4)
				});
		
		/*VillagerTrades.TRADES.get(VillagerProfession.FARMER).put(1,
				new VillagerTrades.ItemListing[] {
					new EmeraldForItems(Items.WHEAT, 20, 16, 2),
					new EmeraldForItems(Items.POTATO, 26, 16, 2),
					new EmeraldForItems(Items.CARROT, 22, 16, 2),
					new EmeraldForItems(Items.BEETROOT, 15, 16, 2),
					new ItemsForEmeralds(Items.BREAD, 1, 6, 16, 1)
				});
		VillagerTrades.TRADES.get(VillagerProfession.FARMER).put(2,
				new VillagerTrades.ItemListing[] {
						new ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5),
						new ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5)
				});
		VillagerTrades.TRADES.get(VillagerProfession.FARMER).put(3,
				new VillagerTrades.ItemListing[] {
						new EmeraldForItems(Blocks.MELON, 4, 12, 20)
				});
		VillagerTrades.TRADES.get(VillagerProfession.FARMER).put(4,
				new VillagerTrades.ItemListing[] {
						new SuspiciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15),
						new SuspiciousStewForEmerald(MobEffects.JUMP, 160, 15),
						new SuspiciousStewForEmerald(MobEffects.WEAKNESS, 140, 15),
						new SuspiciousStewForEmerald(MobEffects.BLINDNESS, 120, 15),
						new SuspiciousStewForEmerald(MobEffects.POISON, 280, 15),
						new SuspiciousStewForEmerald(MobEffects.SATURATION, 7, 15)
				});
		VillagerTrades.TRADES.get(VillagerProfession.FARMER).put(5,
				new VillagerTrades.ItemListing[] {
						new ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30)
				});*/
		
		

		
	}
	
	

	
	
	/**
	 * This is garbage, like literally, but i could not be bothered to make reflections etc to reach
	 * code THAT FUCKING EXIST but is private for some reason
	 * */
	
	static class DyedArmorForEmeralds implements VillagerTrades.ItemListing {
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

	static class EmeraldForItems implements VillagerTrades.ItemListing {
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

	static class EmeraldsForVillagerTypeItem implements VillagerTrades.ItemListing {
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

	static class SimpleEnchantBookForEmeralds implements VillagerTrades.ItemListing {
		private final int villagerXp;

		public SimpleEnchantBookForEmeralds(int p_35683_) {
			this.villagerXp = p_35683_;
		}

		public MerchantOffer getOffer(Entity p_35685_, Random p_35686_) {
			List<Enchantment> list = StreamSupport.stream(ForgeRegistries.ENCHANTMENTS.spliterator(), false).filter(Enchantment::isTradeable).filter(enchant -> !enchant.isCurse())
					.filter(enchant -> !enchant.isAllowedOnBooks())
					.filter(enchant -> enchant.getRarity() == Rarity.COMMON || enchant.getRarity() == Rarity.UNCOMMON)
					.collect(Collectors.toList());
			Enchantment enchantment = list.get(p_35686_.nextInt(list.size()));
			int i = Mth.nextInt(p_35686_, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
			int j = 2 + p_35686_.nextInt(5 + i * 10) + 3 * i;
			if (enchantment.isTreasureOnly()) {
				j *= 2;
			}

			if (j > 64) {
				j = 64;
			}

			return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 12,
					this.villagerXp, 0.2F);
		}
	}
	
	static class EnchantBookForEmeralds implements VillagerTrades.ItemListing {
		private final int villagerXp;

		public EnchantBookForEmeralds(int p_35683_) {
			this.villagerXp = p_35683_;
		}

		public MerchantOffer getOffer(Entity p_35685_, Random p_35686_) {
			List<Enchantment> list = StreamSupport.stream(ForgeRegistries.ENCHANTMENTS.spliterator(), false).filter(Enchantment::isTradeable)
					.collect(Collectors.toList());
			Enchantment enchantment = list.get(p_35686_.nextInt(list.size()));
			int i = Mth.nextInt(p_35686_, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
			int j = 2 + p_35686_.nextInt(5 + i * 10) + 3 * i;
			if (enchantment.isTreasureOnly()) {
				j *= 2;
			}

			if (j > 64) {
				j = 64;
			}

			return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 12,
					this.villagerXp, 0.2F);
		}
	}
	// Protection III -> 11 / 46 
	// Sharpness V -> 17 / 72
	// Protection III + Sharpness V -> 28 / 66
	static class MultiEnchantBookForEmeralds implements VillagerTrades.ItemListing {
		private final int villagerXp;
		private final int minEnchant;
		private final int maxEnchant;

		public MultiEnchantBookForEmeralds(int p_35683_, int minEnchantCount, int maxEnchantCount) {
			this.villagerXp = p_35683_;
			this.minEnchant = minEnchantCount;
			this.maxEnchant = maxEnchantCount;
		}

		public MerchantOffer getOffer(Entity entity, Random random) {
			List<Enchantment> enchantList = StreamSupport.stream(ForgeRegistries.ENCHANTMENTS.spliterator(), false).filter(Enchantment::isTradeable)
					.collect(Collectors.toList());
			Collections.shuffle(enchantList);		// Shuffling
			int randomEnchantAmount = random.nextInt(this.minEnchant, this.maxEnchant);
			int emeraldCost = 0;
			ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);
			for (int i = 0; i < randomEnchantAmount; i++) {
				Enchantment enchantment = enchantList.get(i);
				int enchantLevel = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
				emeraldCost += 2 + random.nextInt(3 + enchantLevel * (enchantment.isTreasureOnly() ? 7 : 5)) + 3 * enchantLevel;
				EnchantedBookItem.addEnchantment(itemStack, new EnchantmentInstance(enchantment, enchantLevel));
			}
			if (emeraldCost >= 64) { emeraldCost = 64; }

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.BOOK), itemStack, 12,
					this.villagerXp, 0.2F);
		}
	}

	static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
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

	public interface ItemListing {
		@Nullable
		MerchantOffer getOffer(Entity p_35706_, Random p_35707_);
	}

	static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
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

	static class ItemsForEmeralds implements VillagerTrades.ItemListing {
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

	static class SuspiciousStewForEmerald implements VillagerTrades.ItemListing {
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

	static class TippedArrowForItemsAndEmeralds implements VillagerTrades.ItemListing {
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

	static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
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
