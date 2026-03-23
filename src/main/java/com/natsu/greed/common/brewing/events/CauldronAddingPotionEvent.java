package com.natsu.greed.common.brewing.events;

import com.google.common.base.Preconditions;
import com.natsu.greed.common.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CauldronAddingPotionEvent extends PlayerEvent {

	private final InteractionHand hand;
    private final BlockPos cauldronPos;
    private final GreedCauldronBlockEntity blockEntity;
    private InteractionResult cancellationResult = InteractionResult.PASS;
    private final Potion incomingPotion;
    
    public CauldronAddingPotionEvent(Player player, InteractionHand hand, BlockPos pos, GreedCauldronBlockEntity cauldron, Potion potion) {
    	super(Preconditions.checkNotNull(player, "Null player in CauldronAddingPotionEvent!"));
        this.hand = Preconditions.checkNotNull(hand, "Null hand in CauldronAddingPotionEvent!");
        this.blockEntity = Preconditions.checkNotNull(cauldron, "Null blockEntity in CauldronAddingPotionEvent!");
        this.cauldronPos = pos;
        this.incomingPotion = Preconditions.checkNotNull(potion, "Null Potion in CauldronAddingPotionEvent!");;
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


	public Potion getIncomingPotion() {
		return incomingPotion;
	}
    
	@Override
	public boolean isCancelable() {
		return true;
	}
	
}