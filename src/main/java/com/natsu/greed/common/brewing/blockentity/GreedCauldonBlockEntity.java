package com.natsu.greed.common.brewing.blockentity;

import com.natsu.greed.common.registry.GreedBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GreedCauldonBlockEntity extends BlockEntity {

	private Potion potion = Potions.EMPTY;
	private int level = 0;
	private static final int MAX_LEVEL = 3;
	
	public GreedCauldonBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
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

}
