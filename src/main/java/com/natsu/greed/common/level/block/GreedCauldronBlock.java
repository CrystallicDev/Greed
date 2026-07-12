package com.natsu.greed.common.level.block;

import java.util.Map;

import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

// Chaudron à potions ; interactions eau/pluie/feu vanilla neutralisées
public class GreedCauldronBlock extends LayeredCauldronBlock implements EntityBlock {

	private static final Map<Item, CauldronInteraction> NO_INTERACTIONS = CauldronInteraction.newInteractionMap();

	public GreedCauldronBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.WATER_CAULDRON), precipitation -> false, NO_INTERACTIONS);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GreedCauldronBlockEntity(pos, state);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		// applique les effets du chaudron aux entités qui y baignent, sans le vider
		if (level.isClientSide() || !(entity instanceof LivingEntity living)) {
			return;
		}
		int seconds = ServerConfig.CAULDRONS_EFFECT_ON_ENTER_SECONDS.get();
		if (seconds <= 0 || !this.isEntityInsideContent(state, pos, entity)) {
			return;
		}
		if (!(level.getBlockEntity(pos) instanceof GreedCauldronBlockEntity cauldron)) {
			return;
		}
		for (MobEffectInstance stored : cauldron.getEffects()) {
			if (stored.getEffect().isInstantenous()) {
				continue; // un effet instantané serait réappliqué à chaque tick
			}
			MobEffectInstance current = living.getEffect(stored.getEffect());
			if (current == null || (current.getDuration() <= 20 && current.getAmplifier() <= stored.getAmplifier())) {
				living.addEffect(new MobEffectInstance(stored.getEffect(), seconds * 20, stored.getAmplifier()));
			}
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return new ItemStack(Items.CAULDRON);
	}

}
