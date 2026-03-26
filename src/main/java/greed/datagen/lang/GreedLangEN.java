package greed.datagen.lang;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedEnchants;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class GreedLangEN extends LanguageProvider {

	public GreedLangEN(DataGenerator gen) {
		super(gen, Greed.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add(GreedEnchants.CURSE_OF_ABSORPTION.get(), "Curse of Absorption");
		add(GreedEnchants.CURSE_OF_COMBINATION.get(), "Curse of Combination");
		add(GreedEnchants.CURSE_OF_CREEPING.get(), "Curse of Creeping");
		add(GreedEnchants.CURSE_OF_SCARCITY.get(), "Curse of Scarcity");
		add(GreedEnchants.CURSE_OF_THE_SPONGE.get(), "Curse of The Sponge");
		add(GreedEnchants.CURSE_OF_VOIDING.get(), "Curse of Voiding");
		add(GreedEnchants.CURSE_OF_HEAVYWEIGHT.get(), "Curse of Heavyweight");
		add(GreedEnchants.LIGHT.get(), "Light");
		add(GreedEnchants.REELING.get(), "Reeling");
		add(GreedEnchants.GRAPPLING.get(), "Grappling");
		add(GreedEnchants.STRETCHED.get(), "Stretched");
	}

}
