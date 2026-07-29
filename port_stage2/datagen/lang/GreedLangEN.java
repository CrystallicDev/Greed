package greed.datagen.lang;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class GreedLangEN extends LanguageProvider {

	public GreedLangEN(PackOutput output) {
		super(output, Greed.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("enchantment.greed.curse_of_absorption", "Curse of Absorption");
		add("enchantment.greed.curse_of_combination", "Curse of Combination");
		add("enchantment.greed.curse_of_creeping", "Curse of Creeping");
		add("enchantment.greed.curse_of_scarcity", "Curse of Scarcity");
		add("enchantment.greed.curse_of_the_sponge", "Curse of The Sponge");
		add("enchantment.greed.curse_of_voiding", "Curse of Voiding");
		add("enchantment.greed.curse_of_heavyweight", "Curse of Heavyweight");
		add("enchantment.greed.light", "Light");
		add("enchantment.greed.reeling", "Reeling");
		add("enchantment.greed.grappling", "Grappling");
		add("enchantment.greed.stretched", "Stretched");
		
		add("map.greed.mesa", "Mesa Explorer Map");
		add("map.greed.jungle", "Jungle Explorer Map");
		add("map.greed.taiga", "Taiga Explorer Map");
		add("map.greed.jungle_temple", "Lost Temple Explorer Map");
		add("map.greed.nether_fortress", "Nether Fortress Explorer Map");
		add("map.greed.bastion", "Bastion Explorer Map");
		add("map.greed.end_city", "End City Explorer Map");

		add("item.greed.trade.1_random_potion", "Mysterious Potion");
		add("item.greed.trade.1_random_splash_potion", "Mysterious Splash Potion");
		add("item.greed.trade.2_random_potion", "Mysterious Potion");
		add("item.greed.trade.2_random_splash_potion", "Mysterious Splash Potion");
		add("item.greed.trade.splash_weakness", "Vaccine Potion");
		
		add(GreedBlocks.CAULDRON.get(), "Cauldron");

		add("message.greed.raw_meat", "Raw meat must be cut and cooked before being eaten!");
		add("message.greed.plain_bread", "Plain bread cannot be eaten as is: use it in a meal!");
	}

}
