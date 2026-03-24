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

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GreedEnchants {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Greed.MODID);
	
	public static final RegistryObject<Enchantment> CURSE_OF_THE_SPONGE = ENCHANTMENTS.register("curse_of_the_sponge", 
			() -> new CurseOfTheSponge());
	public static final RegistryObject<Enchantment> CURSE_OF_ABSORPTION = ENCHANTMENTS.register("curse_of_absorption", 
			() -> new CurseOfAbsorption());
	public static final RegistryObject<Enchantment> CURSE_OF_VOIDING = ENCHANTMENTS.register("curse_of_voiding", 
			() -> new CurseOfVoiding());
	public static final RegistryObject<Enchantment> CURSE_OF_CREEPING = ENCHANTMENTS.register("curse_of_creeping", 
			() -> new CurseOfCreeping());
	public static final RegistryObject<Enchantment> CURSE_OF_COMBINATION = ENCHANTMENTS.register("curse_of_combination", 
			() -> new CurseOfCombination());
	public static final RegistryObject<Enchantment> CURSE_OF_SCARCITY = ENCHANTMENTS.register("curse_of_scarcity", 
			() -> new CurseOfScarcity());
	public static final RegistryObject<Enchantment> CURSE_OF_HEAVYWEIGHT = ENCHANTMENTS.register("curse_of_heavyweight", 
			() -> new CurseOfHeavyweight());
	public static final RegistryObject<Enchantment> LIGHT = ENCHANTMENTS.register("light", 
			() -> new Light());
	public static final RegistryObject<Enchantment> REELING = ENCHANTMENTS.register("reeling", 
			() -> new Reeling());
	public static final RegistryObject<Enchantment> GRAPPLING = ENCHANTMENTS.register("grappling", 
			() -> new Grappling());
	public static final RegistryObject<Enchantment> STRETCHED = ENCHANTMENTS.register("stretched", 
			() -> new Stretched());
}
