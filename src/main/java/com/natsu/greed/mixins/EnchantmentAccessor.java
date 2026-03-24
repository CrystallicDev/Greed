package com.natsu.greed.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(Enchantment.class)
public interface EnchantmentAccessor {

    @Accessor("rarity")
    @Mutable
    void setRarity(Enchantment.Rarity rarity);

    @Accessor("rarity")
    Enchantment.Rarity getRarity();

}