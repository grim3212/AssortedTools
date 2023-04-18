package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.data.FabricBlockTagProvider;
import com.grim3212.assorted.lib.data.FabricItemTagProvider;
import com.grim3212.assorted.tools.data.ToolsBlockTagProvider;
import com.grim3212.assorted.tools.data.ToolsChestLoot;
import com.grim3212.assorted.tools.data.ToolsItemTagProvider;
import com.grim3212.assorted.tools.data.ToolsRecipes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

public class AssortedToolsFabricDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider((output, registriesFuture) -> new ToolsRecipes(output));
        FabricBlockTagProvider provider = pack.addProvider((output, registriesFuture) -> new FabricBlockTagProvider(output, registriesFuture, new ToolsBlockTagProvider(output, registriesFuture)));
        pack.addProvider((output, registriesFuture) -> new FabricItemTagProvider(output, registriesFuture, provider.contentsGetter(), new ToolsItemTagProvider(output, registriesFuture, provider.contentsGetter())));
        pack.addProvider((output, registriesFuture) -> new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ToolsChestLoot::new, LootContextParamSets.CHEST))));
    }
}
