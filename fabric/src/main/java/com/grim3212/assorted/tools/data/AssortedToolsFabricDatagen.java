package com.grim3212.assorted.decor.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

public class AssortedDecorFabricDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider((output, registriesFuture) -> new DecorCommonRecipeProvider(output));
        FabricBlockTagProvider provider = pack.addProvider(FabricBlockTagProvider::new);
        pack.addProvider((output, registriesFuture) -> new FabricItemTagProvider(output, registriesFuture, provider));
        pack.addProvider((output, registriesFuture) -> new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(DecorBlockLoot::new, LootContextParamSets.BLOCK))));
    }
}
