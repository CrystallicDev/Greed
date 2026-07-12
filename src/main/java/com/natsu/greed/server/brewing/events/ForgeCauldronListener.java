package com.natsu.greed.server.brewing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.common.level.block.GreedCauldronBlock;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;
import com.natsu.greed.utils.PotionCreatorUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Swap chaudron vanilla <-> chaudron Greed selon qu'on y verse/retire une potion
@Mod.EventBusSubscriber(modid = Greed.MODID)
public class ForgeCauldronListener {

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
		if (!ServerConfig.USE_CUSTOM_CAULDRONS.get()) {
			return;
		}

		Level level = e.getLevel();
		BlockState state = level.getBlockState(e.getPos());

		if (state.is(Blocks.CAULDRON) && e.getItemStack().getItem() == Items.POTION) {
			Potion potion = PotionUtils.getPotion(e.getItemStack());
			if (potion.getEffects().isEmpty()) {
				return; // eau, awkward, etc. : comportement vanilla
			}
			consumeEvent(e, level);
			if (!level.isClientSide()) {
				fillVanillaCauldron(e, level, state, potion);
			}
		} else if (state.getBlock() instanceof GreedCauldronBlock) {
			if (e.getItemStack().getItem() == Items.POTION) {
				Potion potion = PotionUtils.getPotion(e.getItemStack());
				if (potion.getEffects().isEmpty()) {
					return;
				}
				if (state.getValue(LayeredCauldronBlock.LEVEL) >= 3) {
					return; // plein : on laisse le comportement vanilla (boire la potion)
				}
				consumeEvent(e, level);
				if (!level.isClientSide()) {
					addPotionToCauldron(e, level, state, potion);
				}
			} else if (e.getItemStack().getItem() == Items.GLASS_BOTTLE) {
				consumeEvent(e, level);
				if (!level.isClientSide()) {
					takePotionFromCauldron(e, level);
				}
			}
		}
	}

	private static void consumeEvent(PlayerInteractEvent.RightClickBlock e, Level level) {
		e.setCanceled(true);
		e.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
	}

	private static void fillVanillaCauldron(PlayerInteractEvent.RightClickBlock e, Level level, BlockState oldState, Potion potion) {
		BlockPos pos = e.getPos();
		level.setBlock(pos, GreedBlocks.CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1), 3);
		if (!(level.getBlockEntity(pos) instanceof GreedCauldronBlockEntity cauldron)) {
			return;
		}

		CauldronAddingPotionEvent event = new CauldronAddingPotionEvent(e.getEntity(), e.getHand(), pos, cauldron, potion);
		if (!MinecraftForge.EVENT_BUS.post(event) && cauldron.addPotion(potion)) {
			exchangeBottle(e, level, pos, new ItemStack(Items.GLASS_BOTTLE), SoundEvents.BOTTLE_EMPTY);
		} else {
			level.setBlock(pos, oldState, 3); // annulé : on restaure le chaudron vanilla
		}
	}

	private static void addPotionToCauldron(PlayerInteractEvent.RightClickBlock e, Level level, BlockState state, Potion potion) {
		BlockPos pos = e.getPos();
		if (!(level.getBlockEntity(pos) instanceof GreedCauldronBlockEntity cauldron)) {
			return;
		}

		CauldronAddingPotionEvent event = new CauldronAddingPotionEvent(e.getEntity(), e.getHand(), pos, cauldron, potion);
		if (!MinecraftForge.EVENT_BUS.post(event) && cauldron.addPotion(potion)) {
			level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, state.getValue(LayeredCauldronBlock.LEVEL) + 1), 3);
			exchangeBottle(e, level, pos, new ItemStack(Items.GLASS_BOTTLE), SoundEvents.BOTTLE_EMPTY);
		}
	}

	private static void takePotionFromCauldron(PlayerInteractEvent.RightClickBlock e, Level level) {
		BlockPos pos = e.getPos();
		if (!(level.getBlockEntity(pos) instanceof GreedCauldronBlockEntity cauldron) || !cauldron.hasEffects()) {
			return;
		}

		CauldronTakingPotionEvent event = new CauldronTakingPotionEvent(e.getEntity(), e.getHand(), pos, cauldron);
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			ItemStack potionStack = PotionCreatorUtils.makeIntoPotion(Items.POTION, cauldron.drain());
			level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3); // retour au chaudron vanilla vide
			exchangeBottle(e, level, pos, potionStack, SoundEvents.BOTTLE_FILL);
		}
	}

	private static void exchangeBottle(PlayerInteractEvent.RightClickBlock e, Level level, BlockPos pos, ItemStack result, SoundEvent sound) {
		Player player = e.getEntity();
		player.setItemInHand(e.getHand(), ItemUtils.createFilledResult(e.getItemStack(), player, result));
		level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

}
