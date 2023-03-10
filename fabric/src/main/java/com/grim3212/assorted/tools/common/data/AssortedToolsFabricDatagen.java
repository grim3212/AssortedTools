package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.data.ToolsCommonRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AssortedToolsFabricDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider((output, registriesFuture) -> new ToolsCommonRecipeProvider(output));
        FabricBlockTagProvider provider = pack.addProvider(FabricBlockTagProvider::new);
        pack.addProvider((output, registriesFuture) -> new FabricItemTagProvider(output, registriesFuture, provider));
    }
}
