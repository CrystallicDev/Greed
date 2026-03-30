package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.common.level.block.GreedCauldronBlock;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GreedBlocks {

public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Greed.MODID);
	
	public static final RegistryObject<Block> CAULDRON = BLOCKS.register("greed_cauldron", 
			() -> new GreedCauldronBlock() );
	
}
