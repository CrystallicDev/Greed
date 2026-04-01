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
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class WitchEvents {

	@SubscribeEvent
	public static void onThrown(EntityJoinWorldEvent event) {
		if (!ServerConfig.USE_CUSTOM_WITCHES_POTION.get()) return;
		if (!(event.getEntity() instanceof ThrownPotion potion)) return;
		if (!(potion.getOwner() instanceof Witch)) return;
		
		ItemStack newPotion = convertPotion(potion.getItem());
		potion.setItem(newPotion);
	}
	
	public static ItemStack convertPotion(ItemStack potion) {
		List<MobEffectInstance> effects = PotionUtils.getMobEffects(potion);
		if (effects.isEmpty()) return potion;
		MobEffectInstance current = effects.get(0);
		
		if (current.getEffect() == MobEffects.POISON) {
			if (new Random().nextInt(0, 100) <= 30) {
				return PotionCreatorUtils.makeIntoPotion(Items.SPLASH_POTION, List.of(new MobEffectInstance(MobEffects.POISON, 15*20, 1)));
			} else { return potion; }
		} else if (current.getEffect() == MobEffects.WEAKNESS) {
			if (new Random().nextInt(0, 100) <= 30) {
				return PotionCreatorUtils.makeIntoPotion(Items.LINGERING_POTION, List.of(new MobEffectInstance(MobEffects.WEAKNESS, 120*20, 0), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120*20, 1)));
			} else { return potion; }
		} else if (current.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
			if (new Random().nextInt(0, 100) <= 30) {
				return PotionCreatorUtils.makeIntoPotion(Items.LINGERING_POTION, List.of(new MobEffectInstance(MobEffects.HUNGER, 90*20, 1), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180*20, 1)));
			} else { return potion; }
		}
		
		
		return potion;
	}
	
	
	
}
