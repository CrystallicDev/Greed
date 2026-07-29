package greed.datagen;

import com.natsu.greed.Greed;

import greed.datagen.client.GreedBlockStateProvider;
import greed.datagen.client.GreedItemModelProvider;
import greed.datagen.lang.GreedLangEN;
import greed.datagen.lang.GreedLangFR;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Greed.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GreedDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(event.includeClient(), new GreedLangEN(gen.getPackOutput()));
            gen.addProvider(event.includeClient(), new GreedLangFR(gen.getPackOutput()));
            gen.addProvider(event.includeClient(), new GreedBlockStateProvider(gen.getPackOutput(), helper));
            gen.addProvider(event.includeClient(), new GreedItemModelProvider(gen.getPackOutput(), helper));
        }
    }
}
