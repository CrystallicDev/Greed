package com.natsu.greed.common.brewing.events;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import com.google.common.base.Preconditions;
import com.natsu.greed.common.brewing.blockentity.GreedCauldronBlockEntity;

public class CauldronTakingPotionEvent extends PlayerEvent {

	private final InteractionHand hand;
    private final BlockPos cauldronPos;
    private final GreedCauldronBlockEntity blockEntity;
    private InteractionResult cancellationResult = InteractionResult.PASS;
    
    public CauldronTakingPotionEvent(Player player, InteractionHand hand, BlockPos pos, GreedCauldronBlockEntity cauldron) {
    	super(Preconditions.checkNotNull(player, "Null player in CauldronTakingPotionEvent!"));
        this.hand = Preconditions.checkNotNull(hand, "Null hand in CauldronTakingPotionEvent!");
        this.blockEntity = Preconditions.checkNotNull(cauldron, "Null blockEntity in CauldronTakingPotionEvent!");
        this.cauldronPos = pos;
    }

	public InteractionResult getCancellationResult() {
		return cancellationResult;
	}

	public void setCancellationResult(InteractionResult cancellationResult) {
		this.cancellationResult = cancellationResult;
	}

	public InteractionHand getHand() {
		return hand;
	}

	public BlockPos getCauldronPos() {
		return cauldronPos;
	}

	public GreedCauldronBlockEntity getBlockEntity() {
		return blockEntity;
	}
    
	@Override
	public boolean isCancelable() {
		return true;
	}
    
	
}
