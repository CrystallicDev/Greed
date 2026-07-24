package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.server.brewing.blockentity.GreedCauldronBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GreedBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Greed.MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GreedCauldronBlockEntity>> CAULDRON =
			BLOCK_ENTITIES.register("potion_cauldron",
					() -> BlockEntityType.Builder.of(GreedCauldronBlockEntity::new, GreedBlocks.CAULDRON.get()).build(null));

}
