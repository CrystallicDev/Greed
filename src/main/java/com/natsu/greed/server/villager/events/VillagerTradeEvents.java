package com.natsu.greed.server.villager.events;

import com.natsu.greed.Greed;
import com.natsu.greed.server.villager.VillagerTradeHandler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Greed.MODID)
public class VillagerTradeEvents {

	@SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    	event.enqueueWork(() -> {
    		VillagerTradeHandler.init();
    	});
    }
	
}
