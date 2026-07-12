package com.natsu.greed.server.brewing.events;

import com.natsu.greed.Greed;
import com.natsu.greed.config.ServerConfig;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Empêche de remplir une bouteille d'eau à une source (il faut un chaudron)
@Mod.EventBusSubscriber(modid = Greed.MODID)
public class WaterBottleListener {

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (!ServerConfig.PREVENT_WATER_BOTTLE_FILLING.get()) return;
		if (event.getItemStack().getItem() != Items.GLASS_BOTTLE) return;

		Player player = event.getPlayer();
		Level level = event.getWorld();
		Vec3 eye = player.getEyePosition(1.0F);
		Vec3 end = eye.add(player.getViewVector(1.0F).scale(5.0D));
		BlockHitResult hit = level.clip(new ClipContext(eye, end,
				ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));

		if (hit.getType() == HitResult.Type.BLOCK
				&& level.getFluidState(hit.getBlockPos()).is(FluidTags.WATER)) {
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.FAIL);
		}
	}

}
