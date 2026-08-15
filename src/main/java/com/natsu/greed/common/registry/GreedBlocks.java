package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.common.level.block.GreedCauldronBlock;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GreedBlocks {

	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Greed.MODID);

	public static final DeferredBlock<GreedCauldronBlock> CAULDRON = BLOCKS.registerBlock("greed_cauldron",
			GreedCauldronBlock::new,
			() -> BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON));

}
