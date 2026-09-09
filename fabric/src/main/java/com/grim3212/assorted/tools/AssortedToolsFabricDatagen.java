package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.data.FabricBlockTagProvider;
import com.grim3212.assorted.lib.data.FabricItemTagProvider;
import com.grim3212.assorted.lib.data.FabricWorldGenProvider;
import com.grim3212.assorted.tools.data.ToolsBlockTagProvider;
import com.grim3212.assorted.tools.data.ToolsChestLoot;
import com.grim3212.assorted.tools.data.ToolsEnchantmentData;
import com.grim3212.assorted.tools.data.ToolsItemTagProvider;
import com.grim3212.assorted.tools.data.ToolsRecipes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

public class AssortedToolsFabricDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        // Recipe providers are not data providers any more - the Runner owns the output.
        pack.addProvider((output, registriesFuture) -> new ToolsRecipes.Runner(output, registriesFuture));
        FabricBlockTagProvider provider = pack.addProvider((output, registriesFuture) -> new FabricBlockTagProvider(output, registriesFuture, new ToolsBlockTagProvider(output, registriesFuture)));
        pack.addProvider((output, registriesFuture) -> new FabricItemTagProvider(output, registriesFuture, provider.contentsGetter(), new ToolsItemTagProvider(output, registriesFuture, provider.contentsGetter())));
        pack.addProvider((output, registriesFuture) -> new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(registries -> new ToolsChestLoot(), LootContextParamSets.CHEST)), registriesFuture));
        pack.addProvider((output, registriesFuture) -> new FabricWorldGenProvider(output, registriesFuture, Constants.MOD_ID, getEnchantmentData()));
    }

    /**
     * Fabric needs both halves: the provider above writes the files, and this declares the registry
     * entries so they exist at generate time. Registering only the provider produces an empty run
     * with a successful build.
     */
    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        getEnchantmentData().addToWorldGem(registryBuilder);
    }

    private ToolsEnchantmentData enchantmentData;

    private ToolsEnchantmentData getEnchantmentData() {
        if (this.enchantmentData == null) {
            this.enchantmentData = new ToolsEnchantmentData();
        }
        return this.enchantmentData;
    }
}
