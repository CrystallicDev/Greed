package greed.datagen.lang;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class GreedLangFR extends LanguageProvider {

	public GreedLangFR(DataGenerator gen) {
		super(gen, Greed.MODID, "fr_fr");
	}

	@Override
	protected void addTranslations() {
		add(GreedEnchants.CURSE_OF_ABSORPTION.get(), "Malédiction d'absorption");
		add(GreedEnchants.CURSE_OF_COMBINATION.get(), "Malédiction de fusion");
		add(GreedEnchants.CURSE_OF_CREEPING.get(), "Malédiction d'accroupissement");
		add(GreedEnchants.CURSE_OF_SCARCITY.get(), "Malédiction de diminution");
		add(GreedEnchants.CURSE_OF_THE_SPONGE.get(), "Malédiction de l'Éponge");
		add(GreedEnchants.CURSE_OF_VOIDING.get(), "Malédiction d'annulation");
		add(GreedEnchants.CURSE_OF_HEAVYWEIGHT.get(), "Malédiction de lourdeur");
		add(GreedEnchants.LIGHT.get(), "Légèreté");
		add(GreedEnchants.REELING.get(), "Repêchage");
		add(GreedEnchants.GRAPPLING.get(), "Grappin");
		add(GreedEnchants.STRETCHED.get(), "Corde Tendue");
		
		add("map.greed.mesa", "Carte d'exploration de mésa");
		add("map.greed.jungle", "Carte d'exploration de la jungle");
		add("map.greed.taiga", "Carte d'exploration de la taiga");
		add("map.greed.jungle_temple", "Carte d'exploration du temple perdu");
		add("map.greed.nether_fortress", "Carte d'exploration de forteresse");
		add("map.greed.bastion", "Carte d'exploration de bastion");
		add("map.greed.end_city", "Carte d'exploration de citée de l'end");

		add("item.greed.trade.1_random_potion", "Potion mystérieuse");
		add("item.greed.trade.1_random_splash_potion", "Potion jetable mystérieuse");
		add("item.greed.trade.2_random_potion", "Potion mystérieuse");
		add("item.greed.trade.2_random_splash_potion", "Potion jetable mystérieuse");
		add("item.greed.trade.trade.splash_weakness", "Potion de dé-zombification");
		
		add(GreedBlocks.CAULDRON.get(), "Chaudron");
	}

}