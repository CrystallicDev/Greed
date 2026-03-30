package com.natsu.greed.server.villager.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.eventbus.api.Event;

public class GreedFillingTradesEvent extends Event {

	private final MinecraftServer server;
	private final VillagerProfession profession;
	private HashMap<Integer, List<VillagerTrades.ItemListing>> map;
	
	public GreedFillingTradesEvent(VillagerProfession profession, MinecraftServer server, HashMap<Integer, List<VillagerTrades.ItemListing>> map) {
		this.profession = profession;
		this.map = map;
		this.server = server;
	}
	
	public void addTradeTo(ProfessionLevel level, VillagerTrades.ItemListing trade) {
		if (map.get(level.toInt()) == null) {
			map.put(level.toInt(), new ArrayList<>());
		}
		map.get(level.toInt()).add(trade);
	}
	
	public void clearTradeOf(ProfessionLevel level) {
		if (map.get(level.toInt()) != null) {
			map.get(level.toInt()).clear();
		} else {
			map.put(level.toInt(), new ArrayList<>());
		}
	}
	
	public MinecraftServer getServer() {
		return server;
	}

	public VillagerProfession getProfession() {
		return this.profession;
	}
	
	public HashMap<Integer, List<VillagerTrades.ItemListing>> getTrades() {
		return map;
	}
	
	static enum ProfessionLevel {
		NOVICE(1),
		APPRENTICE(2),
		JOURNEYMAN(3),
		EXPERT(4),
		MASTER(5);
		
		private final int toInt;
		
		ProfessionLevel(int i) {
			this.toInt = i;
		}
		
		public int toInt() { return this.toInt; }
	}
	
}
