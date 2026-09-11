package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Writes an {@code equipment_asset} for every armour material. Without one, armour renders
 * untextured on the player and nothing warns. Vanilla's {@code EquipmentAssetProvider} hardcodes
 * its own materials, so it cannot be extended.
 */
public class ToolsEquipmentAssetProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public ToolsEquipmentAssetProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> assets = new HashMap<>();

        for (ToolsArmorMaterials material : ToolsArmorMaterials.values()) {
            assets.put(material.assetId(), EquipmentClientInfo.builder()
                    .addHumanoidLayers(Identifier.fromNamespaceAndPath(Constants.MOD_ID, material.getName()))
                    .build());
        }

        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, assets);
    }

    @Override
    public String getName() {
        return "Equipment Asset Definitions: " + Constants.MOD_ID;
    }
}
