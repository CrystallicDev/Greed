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

// potion cauldron - the vanilla water/rain/fire interactions are switched off
public class GreedCauldronBlock extends LayeredCauldronBlock implements EntityBlock {

	// empty interaction map, no vanilla bucket/bottle interactions
	private static final CauldronInteraction.InteractionMap NO_INTERACTIONS =
			CauldronInteraction.newInteractionMap("greed_empty");

	// parent's codec() is locked to MapCodec<LayeredCauldronBlock>, so we type our CODEC to match.
	public static final MapCodec<LayeredCauldronBlock> CODEC = simpleCodec(properties -> new GreedCauldronBlock());

	// precipitation is an enum here, NONE means rain never fills it.
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
		// dumps the stored effects on entities standing in it, without draining it
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
				continue; // an instant effect would just re-fire every tick
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
