package com.natsu.greed.common.brewing.blockentity;

import java.util.ArrayList;
import java.util.List;

import com.natsu.greed.common.registry.GreedBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GreedCauldronBlockEntity extends BlockEntity {

	private Potion potion = Potions.EMPTY;
	private int level = 0;
	private static final int MAX_LEVEL = 3;
	
	public GreedCauldronBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		super(GreedBlockEntities.CAULDRON.get(), p_155229_, p_155230_);
	}
	
	public Potion getPotion() { return potion; }
	public void setPotion(Potion potion) { this.potion = potion; }
	public void empty() { this.level = 0; this.potion = Potions.EMPTY; }
	public int getPotLevel() { return level; }
	public boolean isEmpty() { return level == 0 || potion == Potions.EMPTY; }
	public boolean isFull() { return level >= MAX_LEVEL; }
	
	
	public boolean addPotion(Potion in) {
		if (isEmpty()) {
			this.potion = in;
			this.level = 1;
			setChanged();
			return true;
		}
		if (this.potion == in && !isFull()) {
			this.level++;
			setChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		
	}
	
	@Override
	public void load(CompoundTag tag) {
		
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		return saveWithFullMetadata();
	}
	
	
	public boolean onAddingPotion(Potion incoming) {
		if (isFull()) return false;
		List<MobEffectInstance> finalEffects = new ArrayList<>();
		if (isEmpty()) {
			finalEffects = incoming.getEffects();
		} else {
			for (MobEffectInstance effect : incoming.getEffects()) {
				for (MobEffectInstance existing : potion.getEffects()) {
					if (effect.getEffect().equals(existing.getEffect()) && 
							effect.getAmplifier() == existing.getAmplifier()) {
						finalEffects.add(new MobEffectInstance(existing.getEffect(), (int)Math.round(existing.getDuration() + (0.5 * effect.getDuration())), existing.getAmplifier()));
					}
				}
			}
		}
		if (finalEffects.size() >= potion.getEffects().size()) {
			setPotion(new Potion((MobEffectInstance[]) finalEffects.toArray()));
			this.level++;
			return true;
		}
		return false;
	}
	
	public boolean onTakingPotion(Player player) {
		Potion outgoing = getPotion();
		if (outgoing != null) {
			ItemStack potionBottle = PotionUtils.setPotion(
			        new ItemStack(Items.POTION), outgoing);
			empty();
			if (!player.getInventory().add(potionBottle)) {
	            player.drop(potionBottle, false);
	        }
			return true;
		}
		return false;
	}
	
}
