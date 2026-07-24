package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.common.level.block.GreedCauldronBlock;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GreedBlocks {

	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Greed.MODID);

	public static final DeferredBlock<Block> CAULDRON = BLOCKS.register("greed_cauldron",
			() -> new GreedCauldronBlock());

}
