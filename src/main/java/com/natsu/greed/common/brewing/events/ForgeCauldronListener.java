package com.natsu.greed.common.brewing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.common.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class ForgeCauldronListener {

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent e) {
		Level level = e.getWorld();
		BlockPos pos = e.getPos();
		BlockState state = level.getBlockState(pos);
		Player player = e.getPlayer();
		ItemStack stack = e.getItemStack();

		if (level.isClientSide()) return;
		if (!(state.getBlock() instanceof CauldronBlock)) return;
		if (!(stack.getItem() instanceof PotionItem) || (stack.getItem() != Items.GLASS_BOTTLE)) return;
		
		// Cancelling event, because we will handle it manually (doesnt do much, except preventing filling bottles)
		e.setCanceled(true);
		e.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
		
		BlockEntity entity = level.getBlockEntity(pos);
		// Checking if entity is the righ type, otherwise placing
		if (entity == null || !(entity instanceof GreedCauldronBlockEntity)) {
			level.setBlockEntity(new GreedCauldronBlockEntity(pos, state));
		}
		
		entity = level.getBlockEntity(pos);
		if (entity instanceof GreedCauldronBlockEntity cauldron) {
			if (stack.getItem() == Items.GLASS_BOTTLE) {
				// Taking all
				if (!cauldron.isEmpty()) {
					CauldronTakingPotionEvent event = new CauldronTakingPotionEvent(player, e.getHand(), pos, cauldron);
					MinecraftForge.EVENT_BUS.post(event);
					if (!event.isCanceled()) {
						boolean success = cauldron.onTakingPotion(player);
						if (success) { stack.shrink(1); }
					}
				}
			} else if (stack.getItem() instanceof PotionItem) {
				// adding
				Potion incoming = PotionUtils.getPotion(stack);
				if (!cauldron.isFull() && incoming != null) {
					CauldronAddingPotionEvent event = new CauldronAddingPotionEvent(player, e.getHand(), pos, cauldron, incoming);
					MinecraftForge.EVENT_BUS.post(event);
					if (!event.isCanceled()) {
						boolean success = cauldron.onAddingPotion(incoming);
						if (success) { stack.shrink(1); }
					}
				}
			}
		}
	}
	
	private static void updateCauldronAppearance(Level level, BlockPos pos, BlockState state, int potionLevel) {
		if (state.hasProperty(LayeredCauldronBlock.LEVEL)) {
			level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, potionLevel), 3);
		}
	}
	
}
