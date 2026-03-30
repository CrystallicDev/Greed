package com.natsu.greed.server.brewing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.common.level.block.GreedCauldronBlock;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

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
		if (!ServerConfig.USE_CUSTOM_CAULDRONS.get()) return;
		
		Level level = e.getWorld();
		BlockPos pos = e.getPos();
		BlockState state = level.getBlockState(pos);
		Player player = e.getPlayer();
		ItemStack stack = e.getItemStack();

		if (level.isClientSide()) return;
		
		if (state.getBlock() instanceof GreedCauldronBlock cauldronBlock) {
			if (!(stack.getItem() instanceof PotionItem || stack.getItem() == Items.GLASS_BOTTLE)) return;
			
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof GreedCauldronBlockEntity cauldronBlockEntity) {
				if (stack.getItem() == Items.GLASS_BOTTLE) {
					// Taking all
					if (!cauldronBlockEntity.isEmpty()) {
						System.out.println("Cauldron is not Empty ! pass");
						CauldronTakingPotionEvent event = new CauldronTakingPotionEvent(player, e.getHand(), pos, cauldronBlockEntity);
						MinecraftForge.EVENT_BUS.post(event);
						if (!event.isCanceled()) {
							boolean success = cauldronBlockEntity.onTakingPotion(player);
							System.out.println("Cauldron Taking : "+success);
							if (success) { stack.shrink(1); }
						}
					}
				} else if (stack.getItem() instanceof PotionItem) {
					System.out.println("interact on Cauldron with Potion !");
					// adding
					Potion incoming = PotionUtils.getPotion(stack);
					if (!cauldronBlockEntity.isFull() && incoming != null) {
						System.out.println("Cauldron is not Full ! pass");
						CauldronAddingPotionEvent event = new CauldronAddingPotionEvent(player, e.getHand(), pos, cauldronBlockEntity, incoming);
						MinecraftForge.EVENT_BUS.post(event);
						if (!event.isCanceled()) {
							boolean success = cauldronBlockEntity.onAddingPotion(incoming);
							System.out.println("Cauldron Adding : "+success);
							if (success) { stack.shrink(1); }
						}
					}
				}
			}
		}
	}
	
	
	
}
