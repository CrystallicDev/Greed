package greed.datagen;

import com.natsu.greed.Greed;

import greed.datagen.lang.GreedLangEN;
import greed.datagen.lang.GreedLangFR;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Greed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GreedDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new GreedLangEN(gen));
            gen.addProvider(new GreedLangFR(gen));
        }
    }
}
