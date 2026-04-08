package com.natsu.greed.server.brewing.blockentity;

import java.util.ArrayList;
import java.util.List;

import com.natsu.greed.common.registry.GreedBlockEntities;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.utils.PotionCreatorUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class GreedCauldronBlockEntity extends BlockEntity {

	private List<MobEffectInstance> potions = new ArrayList<>();
	private int level = 0;
	private static final int MAX_LEVEL = 3;
	
	public GreedCauldronBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		super(GreedBlockEntities.CAULDRON.get(), p_155229_, p_155230_);
	}
	
	public List<MobEffectInstance> getPotion() { return potions; }
	public void setPotion(List<MobEffectInstance> potion) { this.potions = potion; }
	public void empty() { this.level = 0; this.potions.isEmpty(); }
	public int getPotLevel() { return level; }
	public boolean isEmpty() { return level == 0 || this.potions.isEmpty(); }
	public boolean isFull() { return level >= MAX_LEVEL; }
	
	
	public boolean addPotion(Potion in) {
		if (isEmpty()) {
			this.potions = in.getEffects();
			this.level = 1;
			setChanged();
			return true;
		}
		if (!isFull()) {
			this.level++;
			setChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("level", level);
		ListTag effectsTag = new ListTag();
		potions.forEach(effect -> {
			CompoundTag nt = new CompoundTag();
			effect.save(nt);
			effectsTag.add(effectsTag);
		});
		tag.put("effects", effectsTag);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		potions.clear();
		tag.getList("effects", Tag.TAG_COMPOUND)
			.forEach(t -> {
				MobEffectInstance effect = MobEffectInstance.load((CompoundTag) t);
				if (potions != null) potions.add(effect);
			});
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		return saveWithFullMetadata();
	}
	
	private static void updateCauldronAppearance(Level level, BlockPos pos, BlockState state, int potionLevel) {
		if (state.hasProperty(LayeredCauldronBlock.LEVEL)) {
			level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, potionLevel), 3);
		}
	}
	
	public boolean onAddingPotion(Potion incoming) {
		System.out.println("onAddingPotion");
		if (isFull()) return false;		// No potion can be added
		
		boolean hasMadeUpdate = false;
		List<MobEffectInstance> finalEffects = new ArrayList<>();
		if (isEmpty()) {
			System.out.println("onAddingPotion - Cauldron is Empty");
			finalEffects = incoming.getEffects();
			hasMadeUpdate = true;
		} else {
			System.out.println("onAddingPotion - Cauldron is not Empty");
			for (MobEffectInstance effect : incoming.getEffects()) {
				boolean hasFoundMatch = false;
				boolean isAllowed = true;
				for (MobEffectInstance existing : potions) {
					if (!isEffectCompatible(existing, effect)) { isAllowed = false; continue; }		// Avoid effect with different amplifier
					if (effect.getEffect().equals(existing.getEffect()) && effect.getAmplifier() == existing.getAmplifier()) {
						finalEffects.add(new MobEffectInstance(existing.getEffect(), (int)Math.round(existing.getDuration() + (ServerConfig.CAULDRONS_POTION_DURATION_MERGE_FACTOR.get() * effect.getDuration())), existing.getAmplifier()));
						hasFoundMatch = true;
						hasMadeUpdate = true;
					}
				}
				if (!hasFoundMatch && isAllowed) {
					// This effect is not already present, adding
					finalEffects.add(effect);
					hasMadeUpdate = true;
				}
			}
		}
		if (hasMadeUpdate) {
			System.out.println("onAddingPotion - Cauldron is now updated !");
			setPotion(finalEffects);
			this.level++;
			return hasMadeUpdate;
		}
		return hasMadeUpdate;
	}
	
	private boolean isEffectCompatible(MobEffectInstance effect, MobEffectInstance effect2) {
		if (effect.getEffect() == effect2.getEffect() && effect.getAmplifier() != effect2.getAmplifier()) return false;
		return true;
	}

	public boolean onTakingPotion(Player player) {
		System.out.println("onTakingPotion");
		List<MobEffectInstance> outgoing = getPotion();
		if (outgoing.isEmpty()) {
			System.out.println("onTakingPotion - Cauldron has a potion");
			ItemStack potionBottle = PotionCreatorUtils.makeIntoPotion(Items.POTION, outgoing);
			empty();
			if (!player.getInventory().add(potionBottle)) {
	            player.drop(potionBottle, false);
	        }
			return true;
		}
		return false;
	}
	
}
