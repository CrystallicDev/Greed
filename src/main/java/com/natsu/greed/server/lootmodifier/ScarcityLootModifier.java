package com.natsu.greed.server.lootmodifier;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.natsu.greed.common.registry.GreedEnchants;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class ScarcityLootModifier extends LootModifier {

	public static final Supplier<Codec<ScarcityLootModifier>> CODEC = Suppliers.memoize(
			() -> RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, ScarcityLootModifier::new)));

	protected ScarcityLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		Entity entity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
		if (!(entity instanceof Player player)) return generatedLoot;

		int level = EnchantmentHelper.getItemEnchantmentLevel(GreedEnchants.CURSE_OF_SCARCITY.get(), player.getMainHandItem());
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
