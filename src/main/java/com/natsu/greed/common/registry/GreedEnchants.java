package com.natsu.greed.common.registry;

import com.natsu.greed.Greed;
import com.natsu.greed.common.enchants.CurseOfAbsorption;
import com.natsu.greed.common.enchants.CurseOfCombination;
import com.natsu.greed.common.enchants.CurseOfCreeping;
import com.natsu.greed.common.enchants.CurseOfHeavyweight;
import com.natsu.greed.common.enchants.CurseOfScarcity;
import com.natsu.greed.common.enchants.CurseOfTheSponge;
import com.natsu.greed.common.enchants.CurseOfVoiding;
import com.natsu.greed.common.enchants.Grappling;
import com.natsu.greed.common.enchants.Light;
import com.natsu.greed.common.enchants.Reeling;
import com.natsu.greed.common.enchants.Stretched;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GreedEnchants {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, Greed.MODID);

	public static final DeferredHolder<Enchantment, CurseOfTheSponge> CURSE_OF_THE_SPONGE = ENCHANTMENTS.register("curse_of_the_sponge", CurseOfTheSponge::new);
	public static final DeferredHolder<Enchantment, CurseOfAbsorption> CURSE_OF_ABSORPTION = ENCHANTMENTS.register("curse_of_absorption", CurseOfAbsorption::new);
	public static final DeferredHolder<Enchantment, CurseOfVoiding> CURSE_OF_VOIDING = ENCHANTMENTS.register("curse_of_voiding", CurseOfVoiding::new);
	public static final DeferredHolder<Enchantment, CurseOfCreeping> CURSE_OF_CREEPING = ENCHANTMENTS.register("curse_of_creeping", CurseOfCreeping::new);
	public static final DeferredHolder<Enchantment, CurseOfCombination> CURSE_OF_COMBINATION = ENCHANTMENTS.register("curse_of_combination", CurseOfCombination::new);
	public static final DeferredHolder<Enchantment, CurseOfScarcity> CURSE_OF_SCARCITY = ENCHANTMENTS.register("curse_of_scarcity", CurseOfScarcity::new);
	public static final DeferredHolder<Enchantment, CurseOfHeavyweight> CURSE_OF_HEAVYWEIGHT = ENCHANTMENTS.register("curse_of_heavyweight", CurseOfHeavyweight::new);
	public static final DeferredHolder<Enchantment, Light> LIGHT = ENCHANTMENTS.register("light", Light::new);
	public static final DeferredHolder<Enchantment, Reeling> REELING = ENCHANTMENTS.register("reeling", Reeling::new);
	public static final DeferredHolder<Enchantment, Grappling> GRAPPLING = ENCHANTMENTS.register("grappling", Grappling::new);
	public static final DeferredHolder<Enchantment, Stretched> STRETCHED = ENCHANTMENTS.register("stretched", Stretched::new);

}
