package com.natsu.greed.common.level.block;

import com.mojang.serialization.MapCodec;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

// Chaudron à potions ; interactions eau/pluie/feu vanilla neutralisées
public class GreedCauldronBlock extends LayeredCauldronBlock implements EntityBlock {

	// map d'interactions vide : aucune interaction seau/bouteille vanilla
	private static final CauldronInteraction.InteractionMap NO_INTERACTIONS =
			CauldronInteraction.newInteractionMap("greed_empty");

	// codec() du parent est invariant en MapCodec<LayeredCauldronBlock> : on type le CODEC dessus.
	public static final MapCodec<LayeredCauldronBlock> CODEC = simpleCodec(properties -> new GreedCauldronBlock());

	// 1.21 : ctor (précipitation, interactions, properties) ; la précipitation est un enum (NONE = jamais rempli par la pluie).
	public GreedCauldronBlock() {
		super(Biome.Precipitation.NONE, NO_INTERACTIONS, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON));
	}

	@Override
	public MapCodec<LayeredCauldronBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GreedCauldronBlockEntity(pos, state);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
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
			if (stored.getEffect().value().isInstantenous()) {
				continue; // un effet instantané serait réappliqué à chaque tick
			}
			MobEffectInstance current = living.getEffect(stored.getEffect());
			if (current == null || (current.getDuration() <= 20 && current.getAmplifier() <= stored.getAmplifier())) {
				living.addEffect(new MobEffectInstance(stored.getEffect(), seconds * 20, stored.getAmplifier()));
			}
		}
	}

	// 1.21 : getCloneItemStack a perdu HitResult/Player et ne prend plus qu'un LevelReader.
	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return new ItemStack(Items.CAULDRON);
	}

}
