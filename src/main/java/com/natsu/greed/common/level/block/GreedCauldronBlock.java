package com.natsu.greed.common.level.block;

import java.util.Map;
import java.util.function.Predicate;

import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class GreedCauldronBlock extends LayeredCauldronBlock implements EntityBlock {

	public GreedCauldronBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.WATER_CAULDRON), precipitation -> precipitation == Biome.Precipitation.RAIN, CauldronInteraction.WATER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GreedCauldronBlockEntity(pos, state);
	}
	
}
