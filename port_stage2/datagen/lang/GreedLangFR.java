package greed.datagen.lang;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class GreedLangFR extends LanguageProvider {

	public GreedLangFR(PackOutput output) {
		super(output, Greed.MODID, "fr_fr");
	}

	@Override
	protected void addTranslations() {
		add("enchantment.greed.curse_of_absorption", "Malédiction d'absorption");
		add("enchantment.greed.curse_of_combination", "Malédiction de fusion");
		add("enchantment.greed.curse_of_creeping", "Malédiction d'accroupissement");
		add("enchantment.greed.curse_of_scarcity", "Malédiction de diminution");
		add("enchantment.greed.curse_of_the_sponge", "Malédiction de l'Éponge");
		add("enchantment.greed.curse_of_voiding", "Malédiction d'annulation");
		add("enchantment.greed.curse_of_heavyweight", "Malédiction de lourdeur");
		add("enchantment.greed.light", "Légèreté");
		add("enchantment.greed.reeling", "Repêchage");
		add("enchantment.greed.grappling", "Grappin");
		add("enchantment.greed.stretched", "Corde Tendue");
		
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
		add("item.greed.trade.splash_weakness", "Potion de dé-zombification");

		add(GreedBlocks.CAULDRON.get(), "Chaudron");

		add("message.greed.raw_meat", "La viande crue doit être découpée et cuite avant d'être mangée !");
		add("message.greed.plain_bread", "Le pain ne se mange pas tel quel : utilisez-le dans un plat !");
	}

}