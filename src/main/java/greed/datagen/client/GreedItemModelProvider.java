package greed.datagen.client;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GreedItemModelProvider extends ItemModelProvider {

    public GreedItemModelProvider(DataGenerator gen, ExistingFileHelper fileHelper) {
        super(gen, Greed.MODID, fileHelper);
    }

	@Override
	protected void registerModels() {
		withExistingParent(GreedBlocks.CAULDRON.get().getRegistryName().getPath(), 
				mcLoc("item/cauldron")
				);
	}
}