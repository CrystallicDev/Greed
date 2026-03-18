package com.natsu.greed.server.lootmodifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class ScarcityLootModifier extends LootModifier {

    public ScarcityLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected List<ItemStack> doApply(
    		List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (entity == null) return generatedLoot;
        if (!(entity instanceof Player player)) return generatedLoot;
        ItemStack weapon = player.getMainHandItem();
       
        int level = EnchantmentHelper.getItemEnchantmentLevel(
            GreedEnchants.CURSE_OF_SCARCITY.get(), weapon);
        if (level == 0) return generatedLoot;


        List<ItemStack> result = new ArrayList<>();
        Random rand = new Random();

        for (ItemStack stack : generatedLoot) {
            int originalCount = stack.getCount();
            // should roughly be -33%, -50% and -75% (roughly)
            float keepChance = (float) Math.pow(0.67, level);
            int newCount = 0;
            for (int i = 0; i < originalCount; i++) {
                if (rand.nextFloat() < keepChance) newCount++;
            }
            if (newCount > 0) {
                ItemStack reduced = stack.copy();
                reduced.setCount(newCount);
                result.add(reduced);
            }
        }
        return result;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ScarcityLootModifier> {
        @Override
        public ScarcityLootModifier read(ResourceLocation loc, JsonObject obj,
                                       LootItemCondition[] conditions) {
            return new ScarcityLootModifier(conditions);
        }
        @Override
        public JsonObject write(ScarcityLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }

}