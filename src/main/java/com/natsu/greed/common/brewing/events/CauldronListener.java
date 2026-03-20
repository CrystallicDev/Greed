package com.natsu.greed.common.brewing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.common.brewing.blockentity.GreedCauldonBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class CauldronListener {

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent e) {
		Level level = e.getWorld();
		BlockPos pos = e.getPos();
		BlockState state = level.getBlockState(pos);
		Player player = e.getPlayer();
		ItemStack stack = e.getItemStack();
		
		if (!(state.getBlock() instanceof CauldronBlock)) return;
		if (!(stack.getItem() instanceof PotionItem)) return;
		
		e.setCanceled(true);
		e.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
		
		if (level.isClientSide()) return;
		
		BlockEntity entity = level.getBlockEntity(pos);
		if (!(entity instanceof GreedCauldonBlockEntity cauldron)) {
			level.setBlockEntity(new GreedCauldonBlockEntity(pos, state));
			entity = level.getBlockEntity(pos);
			if (!(entity instanceof GreedCauldonBlockEntity cauldron2)) return;
			handleAdd(level, pos, state, player, stack, cauldron2);
			return;
		}
		if (stack.getItem() == Items.GLASS_BOTTLE) {
		    if (cauldron.isEmpty()) return;

		    Potion extracted = cauldron.getPotion();
		    ItemStack potionBottle = PotionUtils.setPotion(
		        new ItemStack(Items.POTION), extracted);
		    cauldron.empty();
		    
		    
		    if (!player.isCreative()) {
		        stack.shrink(1);
		        if (!player.getInventory().add(potionBottle)) {
		            player.drop(potionBottle, false);
		        }
		    }

		    updateCauldronAppearance(level, pos, state, cauldron.getPotLevel());
		    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1f, 1f);
		    level.sendBlockUpdated(pos, state, state, 3);
		    
		    
		    handleAdd(level, pos, state, player, stack, cauldron);
		    return;
		}
		
	}
	
	private static void handleAdd(Level level, BlockPos pos, BlockState state, Player player, ItemStack stack,
			GreedCauldonBlockEntity cauldron) {
		Potion incoming = PotionUtils.getPotion(stack);

		if (incoming == Potions.EMPTY || incoming == Potions.WATER)
			return;

		boolean success = cauldron.addPotion(incoming);
		if (!success) {
			level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1f, 1f);
			return;
		}

		if (!player.isCreative()) {
			stack.shrink(1);
			ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
			if (!player.getInventory().add(bottle)) {
				player.drop(bottle, false);
			}
		}

		updateCauldronAppearance(level, pos, state, cauldron.getPotLevel());

		level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1f, 1f);
		level.sendBlockUpdated(pos, state, state, 3);
	}

	private static void updateCauldronAppearance(Level level, BlockPos pos, BlockState state, int potionLevel) {
		if (state.hasProperty(LayeredCauldronBlock.LEVEL)) {
			level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, potionLevel), 3);
		}
	}
	
}
