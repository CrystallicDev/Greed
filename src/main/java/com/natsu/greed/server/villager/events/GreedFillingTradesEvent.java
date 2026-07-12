package com.natsu.greed.server.villager.events;

import java.util.List;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;

// Adaptateur autour du VillagerTradesEvent de Forge (posté par métier au démarrage serveur)
public class GreedFillingTradesEvent {

	private final VillagerTradesEvent event;

	public GreedFillingTradesEvent(VillagerTradesEvent event) {
		this.event = event;
	}

	public void addTradeTo(ProfessionLevel level, ItemListing trade) {
		event.getTrades().get(level.toInt()).add(trade);
	}

	public void clearTradeOf(ProfessionLevel level) {
		List<ItemListing> list = event.getTrades().get(level.toInt());
		if (list != null) list.clear();
	}

	public VillagerProfession getProfession() {
		return event.getType();
	}

	public enum ProfessionLevel {
		NOVICE(1), APPRENTICE(2), JOURNEYMAN(3), EXPERT(4), MASTER(5);

		private final int toInt;

		ProfessionLevel(int i) { this.toInt = i; }

		public int toInt() { return this.toInt; }
	}

}
