package greed.datagen.client;

import com.natsu.greed.Greed;
import com.natsu.greed.common.registry.GreedBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GreedItemModelProvider extends ItemModelProvider {

    public GreedItemModelProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, Greed.MODID, fileHelper);
    }

	@Override
	protected void registerModels() {
		withExistingParent(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(GreedBlocks.CAULDRON.get()).getPath(), 
				mcLoc("item/cauldron")
				);
	}
}