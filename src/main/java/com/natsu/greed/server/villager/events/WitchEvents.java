package com.natsu.greed.server.villager.events;

import java.util.List;
import java.util.Random;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class WitchEvents {

	@SubscribeEvent
	public static void onThrown(ProjectileImpactEvent event) {
		if (!ServerConfig.USE_CUSTOM_WITCHES_POTION.get()) return;
		if (!(event.getProjectile() instanceof ThrownPotion potion)) return;
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
				ItemStack newPotion = new ItemStack(Items.SPLASH_POTION);
				Potion pot = new Potion(new MobEffectInstance(MobEffects.POISON, 10*20, 1));
				return PotionUtils.setPotion(newPotion, pot);
			} else { return potion; }
		} else if (current.getEffect() == MobEffects.WEAKNESS) {
			if (new Random().nextInt(0, 100) <= 30) {
				ItemStack newPotion = new ItemStack(Items.LINGERING_POTION);
				Potion pot = new Potion(new MobEffectInstance(MobEffects.WEAKNESS, 60*20, 0));
				return PotionUtils.setPotion(newPotion, pot);
			} else { return potion; }
		} else if (current.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
			if (new Random().nextInt(0, 100) <= 30) {
				ItemStack newPotion = new ItemStack(Items.LINGERING_POTION);
				Potion pot = new Potion(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90*20, 1));
				return PotionUtils.setPotion(newPotion, pot);
			} else { return potion; }
		}
		
		
		return potion;
	}
	
}
