package greed.datagen.client;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GreedBlockStateProvider extends BlockStateProvider {

    public GreedBlockStateProvider(DataGenerator gen, ExistingFileHelper fileHelper) {
        super(gen, Greed.MODID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        IntegerProperty LEVEL = LayeredCauldronBlock.LEVEL;

        getVariantBuilder(GreedBlocks.CAULDRON.get())
            .partialState().with(LEVEL, 1)
                .modelForState().modelFile(
                    new ModelFile.UncheckedModelFile("minecraft:block/cauldron_level1")
                ).addModel()
            .partialState().with(LEVEL, 2)
                .modelForState().modelFile(
                    new ModelFile.UncheckedModelFile("minecraft:block/cauldron_level2")
                ).addModel()
            .partialState().with(LEVEL, 3)
                .modelForState().modelFile(
                    new ModelFile.UncheckedModelFile("minecraft:block/cauldron_level3")
                ).addModel();
    }
}