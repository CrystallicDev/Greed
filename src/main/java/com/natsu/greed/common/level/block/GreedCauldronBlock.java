package com.natsu.greed.common.level.block;

import com.mojang.serialization.MapCodec;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
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
	// 26.1 : InteractionMap → CauldronInteraction.Dispatcher (non sérialisé par le codec du bloc).
	private static final CauldronInteraction.Dispatcher NO_INTERACTIONS =
			new CauldronInteraction.Dispatcher();

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

	// 26.1 : entityInside gagne (InsideBlockEffectApplier, boolean isPrecise). On garde l'override
	// complet (chaudron à potions, pas d'extinction de feu comme le vanilla).
	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
			InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		// applique les effets du chaudron aux entités qui y baignent, sans le vider
		if (level.isClientSide() || !(entity instanceof LivingEntity living)) {
			return;
		}
		int seconds = ServerConfig.CAULDRONS_EFFECT_ON_ENTER_SECONDS.get();
		if (seconds <= 0 || !greed$isInContent(state, pos, entity)) {
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

	// 26.1 : isEntityInsideContent supprimé d'AbstractCauldronBlock → réimplémenté via getContentHeight.
	private boolean greed$isInContent(BlockState state, BlockPos pos, Entity entity) {
		return entity.getY() < pos.getY() + this.getContentHeight(state)
				&& entity.getBoundingBox().maxY > pos.getY() + 0.25;
	}

	// 26.1 : getCloneItemStack gagne un boolean includeData.
	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(Items.CAULDRON);
	}

}
