package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.common.brewing.blockentity.GreedCauldonBlockEntity;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GreedBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Greed.MODID);
	
	public static final RegistryObject<BlockEntityType<GreedCauldonBlockEntity>> CAULDRON = BLOCK_ENTITIES.register("potion_cauldron", 
			() -> BlockEntityType.Builder.of(GreedCauldonBlockEntity::new, Blocks.CAULDRON).build(null)
			);
	
}
