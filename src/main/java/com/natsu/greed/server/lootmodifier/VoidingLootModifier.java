package com.natsu.greed.server.lootmodifier;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.natsu.greed.common.registry.GreedEnchants;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class VoidingLootModifier extends LootModifier {

	public static final Supplier<Codec<VoidingLootModifier>> CODEC = Suppliers.memoize(
			() -> RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, VoidingLootModifier::new)));

	protected VoidingLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
		if (tool == null) return generatedLoot;

		int level = EnchantmentHelper.getItemEnchantmentLevel(GreedEnchants.CURSE_OF_VOIDING.get(), tool);
		if (level == 0) return generatedLoot;

		ObjectArrayList<ItemStack> result = new ObjectArrayList<>();
		float keepChance = (float) Math.pow(0.67, level); // ~ -33% / -50% / -75%
		for (ItemStack stack : generatedLoot) {
			int newCount = 0;
			for (int i = 0; i < stack.getCount(); i++) {
				if (context.getRandom().nextFloat() < keepChance) newCount++;
			}
			if (newCount > 0) {
				ItemStack reduced = stack.copy();
				reduced.setCount(newCount);
				result.add(reduced);
			}
		}
		return result;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}

}
