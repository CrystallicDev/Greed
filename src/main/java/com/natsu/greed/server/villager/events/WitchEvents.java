package com.natsu.greed.server.villager.events;

import java.util.List;
import java.util.Random;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;
import com.natsu.greed.utils.PotionCreatorUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import com.google.common.collect.Lists;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Greed.MODID)
public class WitchEvents {

	@SubscribeEvent
	public static void onThrown(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide()) return;
		if (!ServerConfig.USE_CUSTOM_WITCHES_POTION.get()) return;
		if (!(event.getEntity() instanceof AbstractThrownPotion potion)) return;
		if (!(potion.getOwner() instanceof Witch)) return;
		
		ItemStack newPotion = convertPotion(potion.getItem());
		potion.setItem(newPotion);
	}
	
	public static ItemStack convertPotion(ItemStack potion) {
		PotionContents contents = potion.get(DataComponents.POTION_CONTENTS);
		List<MobEffectInstance> effects = contents == null ? List.of() : Lists.newArrayList(contents.getAllEffects());
		if (effects.isEmpty()) return potion;
		MobEffectInstance current = effects.get(0);
		
		// Poison I (45s) -> Poison II (21s) | Poison I (30s) + Blindness I (10s) | Lingering Poison (60s)
		// Slowness I (90s) -> Slowness II (90s) + Mining Fatigue II (30s) | Lingering Slowness IV (10s)
		// Weakness I (90s) -> Weakness II (90s) | Lingering Weakness IV (30s)
		// Damage I -> Lingering Damage I
		
		int rnd = new Random().nextInt(0, 101);
		if (current.getEffect().equals(MobEffects.POISON)) {
			if (rnd <= 25) { return PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, List.of(new MobEffectInstance(MobEffects.POISON, 30*20, 0), new MobEffectInstance(MobEffects.BLINDNESS, 10*20, 0))); }
			else if (rnd <= 50) { return PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, List.of(new MobEffectInstance(MobEffects.POISON, 21*20, 1))); }
			else if (rnd <= 75) { return PotionCreatorUtils.makeIntoPotion(Items.LINGERING_POTION, List.of(new MobEffectInstance(MobEffects.POISON, 60*20, 0))); }
			else { return potion; }
		} else if (current.getEffect().equals(MobEffects.SLOWNESS)) {
			if (rnd <= 33) { return PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, List.of(new MobEffectInstance(MobEffects.SLOWNESS, 90*20, 1), new MobEffectInstance(MobEffects.MINING_FATIGUE, 30*20, 1))); }
			else if (rnd <= 66) { return PotionCreatorUtils.makeIntoPotion(Items.LINGERING_POTION, List.of(new MobEffectInstance(MobEffects.SLOWNESS, 10*20, 3))); }
			else { return potion; }
		} else if (current.getEffect().equals(MobEffects.WEAKNESS)) {
			if (rnd <= 33) { return PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, List.of(new MobEffectInstance(MobEffects.WEAKNESS, 90*20, 1))); }
			else if (rnd <= 66) { return PotionCreatorUtils.makeIntoPotion(Items.LINGERING_POTION, List.of(new MobEffectInstance(MobEffects.WEAKNESS, 30*20, 3))); }
			else { return potion; }
		} else if (current.getEffect().equals(MobEffects.INSTANT_DAMAGE)) {
			if (rnd <= 50) { return PotionCreatorUtils.makeIntoPotion(Items.LINGERING_POTION, List.of(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 15*20, 1))); }
			else { return potion; }
		}
		
		
		return potion;
	}
	
	
	
}
